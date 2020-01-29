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

package com.yepit.mapp.crp.common.interceptor;


import com.yepit.mapp.crp.common.annotation.AdminLogin;
import com.yepit.mapp.framework.constant.ResultCodeConstant;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.util.JsonUtils;
import com.yepit.mapp.framework.util.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户权限(Token)验证
 *
 * @author qianlong
 * @date 2018-09-24
 */
@Component
public class UserAuthInterceptor extends HandlerInterceptorAdapter {

    public static final String USER_KEY = "currentUser";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AdminLogin annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AdminLogin.class);
        } else {
            return true;
        }

        if (annotation == null) {
            return true;
        }

        //从header中获取token
        String loginCode = request.getHeader("accessToken");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(loginCode)) {
            loginCode = request.getParameter("accessToken");
        }

        //token为空
        if (StringUtils.isBlank(loginCode)) {
            this.setNoAuthority(response, "token不能为空");
            return false;
        }

        //从redis中获取code,看是否存在
        Object tokenValue = redisUtils.get(loginCode);
        if (tokenValue == null) {
            this.setNoAuthority(response, "token失效,请重新登录");
            return false;
        }

        //设置tokenValue到request里，后续根据tokenValue，获取用户信息
        request.setAttribute(USER_KEY, tokenValue);

        return true;
    }

    private void setNoAuthority(HttpServletResponse httpServletResponse, String errMsg) throws Exception {
        BaseResponse resp = new BaseResponse().fail(ResultCodeConstant.LOGIN_TIMEOUT, errMsg);
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getOutputStream().write(JsonUtils.Object2Json(resp).getBytes());
        httpServletResponse.setStatus(401);
    }
}
