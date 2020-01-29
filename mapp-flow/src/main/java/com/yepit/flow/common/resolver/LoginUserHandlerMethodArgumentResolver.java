/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yepit.flow.common.resolver;

import com.yepit.flow.bo.SysAdmin;
import com.yepit.flow.cache.AdminCache;
import com.yepit.flow.common.annotation.SystemLoginUser;
import com.yepit.flow.common.interceptor.UserAuthInterceptor;
import com.yepit.flow.dto.AdminDTO;
import com.yepit.mapp.framework.dto.common.SimpleUserInfo;
import com.yepit.mapp.framework.exception.ServiceException;
import com.yepit.mapp.framework.util.JsonUtils;
import com.yepit.mapp.framework.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@SystemLoginUser注解的方法参数，注入当前登录用户
 *
 * @author qianlong
 * @date 2018-10-03
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AdminCache adminCache;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SimpleUserInfo.class) && parameter.hasParameterAnnotation(SystemLoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取tokenValue
        Object object = request.getAttribute(UserAuthInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }

//        SysAdmin userInfo = JsonUtils.jsonToJavaBean((String) object, SysAdmin.class);
        AdminDTO userInfo = JsonUtils.jsonToJavaBean((String) object, AdminDTO.class);

        AdminDTO adminInfo = adminCache.getAdminByLoginName(userInfo.getLoginName());
        if(adminInfo == null){
            throw new ServiceException("用户在系统中不存在,请联系系统管理员");
        }
        if(!adminInfo.getStatus().equals(1)){
            throw new ServiceException("用户状态不正常");
        }

        SimpleUserInfo simpleUserDTO = new SimpleUserInfo();
        simpleUserDTO.setLoginName(userInfo.getLoginName());
        simpleUserDTO.setRealName(userInfo.getRealname());
        simpleUserDTO.setUserId(userInfo.getAdminId().toString());
        simpleUserDTO.setRoleId(userInfo.getRoleId());

        return simpleUserDTO;
    }

}
