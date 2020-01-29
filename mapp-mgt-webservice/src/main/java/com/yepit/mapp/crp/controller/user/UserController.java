package com.yepit.mapp.crp.controller.user;

import com.yepit.mapp.crp.common.annotation.AdminLogin;
import com.yepit.mapp.crp.common.annotation.SystemLoginUser;
import com.yepit.mapp.crp.constant.AdminStateEnum;
import com.yepit.mapp.crp.constant.OTPTypeEnum;
import com.yepit.mapp.crp.domain.system.SysAdmin;
import com.yepit.mapp.crp.dto.system.AdminDTO;
import com.yepit.mapp.crp.dto.system.PageSearchAdminRequest;
import com.yepit.mapp.crp.dto.system.PageSearchAdminResponse;
import com.yepit.mapp.crp.dto.system.UpdateAdminRequest;
import com.yepit.mapp.crp.dto.user.*;
import com.yepit.mapp.crp.service.AdminService;
import com.yepit.mapp.crp.service.UserService;
import com.yepit.mapp.framework.constant.ResultCodeConstant;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageResult;
import com.yepit.mapp.framework.dto.common.SimpleUserInfo;
import com.yepit.mapp.framework.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "用户管理")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "用户登录")
    @PostMapping("/user/login")
    @ResponseBody
    public BaseResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    /**
     * 用户注册
     *
     * @param registerRequest
     * @return
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/user/register")
    @ResponseBody
    public BaseResponse register(@RequestBody RegisterRequest registerRequest) throws Exception {
        return userService.register(registerRequest);
    }

    @AdminLogin
    @ApiOperation(value = "分页查询用户信息")
    @PostMapping(value = "/users", produces = "application/json")
    @ResponseBody
    public BaseResponse<PageResult<PageSearchAdminResponse>> pageSearchUser(@RequestBody PageSearchAdminRequest request) {
        BaseResponse<PageResult<PageSearchAdminResponse>> resp = userService.searchUserByCond(request);
        return resp;
    }

    @AdminLogin
    @ApiOperation(value = "查询用户详细信息")
    @PostMapping(value = "/user/{userId}", produces = "application/json")
    @ResponseBody
    public BaseResponse<GetUserInfoResp> getUserInfoDetail(@PathVariable Long userId) {
        return userService.getUserInfoDetail(userId);
    }

    @AdminLogin
    @ApiOperation(value = "更新自己的个人信息")
    @PutMapping(value = "/user/profile", produces = "application/json")
    @ResponseBody
    public BaseResponse updateProfile(@RequestBody UpdateProfileRequest request,
                                      @ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) {
        SysAdmin user = new SysAdmin();
        try {
            BeanUtils.copyProperties(request, user);
            user.setAdminId(Long.valueOf(currentUser.getUserId()));
            BaseResponse resp = adminService.updateAdmin(user, null);
            return resp;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }
    }

    @AdminLogin
    @ApiOperation(value = "用户激活帐号")
    @PostMapping(value = "/user/{userId}/active", produces = "application/json")
    @ResponseBody
    public BaseResponse userActive(@PathVariable Long userId, @RequestBody UserActiveRequest request,
                                   @ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) {
        request.setUserId(userId);
        request.setOperId(currentUser.getUserId());
        request.setOperName(currentUser.getLoginName());
        return userService.userActive(request);
    }

    @AdminLogin
    @ApiOperation(value = "用户注销帐号")
    @PostMapping(value = "/user/{userId}/inactive", produces = "application/json")
    @ResponseBody
    public BaseResponse userInActive(@PathVariable Long userId) {
        return userService.changeUserStatus(userId, AdminStateEnum.Cancel.getValue());
    }

    @AdminLogin
    @ApiOperation(value = "修改用户的角色")
    @PutMapping(value = "/user/{userId}/role", produces = "application/json")
    @ResponseBody
    public BaseResponse changeUserRole(@PathVariable Long userId, @RequestBody ChangeRoleRequest request) {
        request.setUserid(userId);
        return userService.changeUserRole(request);
    }

    @AdminLogin
    @ApiOperation(value = "用户修改登录密码")
    @PostMapping(value = "/user/password", produces = "application/json")
    @ResponseBody
    public BaseResponse updateLoginPwd(@RequestBody UpdateLoginPwdRequest request) {
        return userService.updateLoginPwd(request);
    }

    @ApiOperation(value = "根据角色随机获取任务执行人")
    @GetMapping(value = "/task/assigner", produces = "application/json")
    @ResponseBody
    public AdminDTO getTaskAssigner(@RequestParam String nodeName) {
        return userService.getTaskAssigner(nodeName);
    }

    @ApiOperation(value = "根据角色获取岗位职能")
    @GetMapping(value = "/role/{roleId}/privileges", produces = "application/json")
    @ResponseBody
    public BaseResponse<List<RolePrivilege>> getTaskAssigner(@PathVariable Long roleId) {
        return userService.getPrivilegesByRole(roleId);
    }

    @ApiOperation(value = "修改登录密码时获取验证码")
    @GetMapping(value = "/user/forgotPwd/otp", produces = "application/json")
    @ResponseBody
    public BaseResponse<String> getModifyPwdOTP(@RequestParam String mobile) {
        return userService.getOTP(OTPTypeEnum.ModifyPwd.getValue(), mobile);
    }

    @ApiOperation(value = "修改登录密码时校验验证码")
    @GetMapping(value = "/user/forgotPwd/{mobile}/verifyOTP", produces = "application/json")
    @ResponseBody
    public BaseResponse verifyOTP(@PathVariable String mobile, @RequestParam String otp) {
        return userService.verifyOTP(OTPTypeEnum.ModifyPwd.getValue(), mobile, otp);
    }

    @ApiOperation(value = "重新修改登录密码")
    @PutMapping(value = "/user/resetPwd", produces = "application/json")
    @ResponseBody
    public BaseResponse resetPwd(@RequestBody ResetPwdRequest resetPwdRequest) {
        return userService.resetLoginPwd(resetPwdRequest);
    }
}
