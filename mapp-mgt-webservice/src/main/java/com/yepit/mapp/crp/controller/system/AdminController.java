package com.yepit.mapp.crp.controller.system;

import com.yepit.mapp.crp.cache.RoleCache;
import com.yepit.mapp.crp.common.annotation.AdminLogin;
import com.yepit.mapp.crp.common.annotation.SystemLoginUser;
import com.yepit.mapp.crp.controller.BaseController;
import com.yepit.mapp.crp.domain.system.SysAdmin;
import com.yepit.mapp.crp.dto.system.*;
import com.yepit.mapp.crp.service.AdminService;
import com.yepit.mapp.framework.constant.ResultCodeConstant;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageResult;
import com.yepit.mapp.framework.util.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by qianlong on 2017/8/20.
 */
@Api(tags = "系统管理业务")
@RestController
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RoleCache roleCache;

    @ApiOperation(value = "管理员登录")
    @PostMapping(value = "/login", produces = "application/json")
    @ResponseBody
    public BaseResponse<AdminLoginResponse> adminLogin(@RequestBody AdminLoginRequest request) {
        try {
            return adminService.adminLogin(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse<AdminLoginResponse>(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }
    }

    @ApiOperation(value = "操作员鉴权")
    @PostMapping(value = "/auth", produces = "application/json")
    @ResponseBody
    public BaseResponse<AdminAuthResponse> adminAuth(@RequestBody AdminLoginRequest request) {
        try {
            return adminService.adminAuth(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse<AdminAuthResponse>(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }
    }

    @AdminLogin
    @ApiOperation(value = "创建操作员")
    @RequestMapping(value = "/admin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public BaseResponse createAdmin(@RequestBody CreateAdminRequest request,
                                    @ApiIgnore @SystemLoginUser AdminDTO currentAdmin) {
        try {
            SysAdmin sysAdmin = new SysAdmin();
            BeanUtils.copyProperties(request, sysAdmin);
            sysAdmin.setCreateOperator(currentAdmin.getLoginName());
            return adminService.createAdmin(sysAdmin, request.getRoleId());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse<AdminLoginResponse>(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }
    }

    @ApiOperation(value = "获取操作员详细信息")
    @GetMapping(value = "/admin/{adminId}", produces = "application/json")
    @ResponseBody
    public BaseResponse<SysAdmin> adminDetail(@PathVariable Long adminId) {
        try {
            return adminService.getAdminById(adminId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse<SysAdmin>(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }
    }

    @ApiOperation(value = "更新操作员信息")
    @PostMapping(value = "/admin/{adminId}", produces = "application/json")
    @ResponseBody
    public BaseResponse updateAdmin(@PathVariable Long adminId, @RequestBody UpdateAdminRequest request) {
        try {
            SysAdmin admin = new SysAdmin();
            BeanUtils.copyProperties(request, admin);
            admin.setAdminId(adminId);
            BaseResponse resp = adminService.updateAdmin(admin, request.getRoleId());
            return resp;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }
    }

    /**
     * 修改管理员角色
     *
     * @param adminId
     * @param request
     * @return
     */
    @ApiOperation(value = "修改管理员角色")
    @PostMapping(value = "/admin/{adminId}/role", produces = "application/json")
    @ResponseBody
    public BaseResponse updateAdminRole(@PathVariable Long adminId, @RequestBody UpdateAdminRoleRequest request) {
        try {
            return adminService.updateAdminRole(adminId, request.getNewRoleId());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }
    }

    @ApiOperation(value = "分页查询管理员信息")
    @PostMapping(value = "/admins", produces = "application/json")
    @ResponseBody
    public BaseResponse<PageResult<PageSearchAdminResponse>> pageSearchAdmin(@RequestBody PageSearchAdminRequest request) {
        BaseResponse<PageResult<PageSearchAdminResponse>> resp = new BaseResponse<PageResult<PageSearchAdminResponse>>();
        try {
            resp = adminService.listAdminByCond(request);
            return resp;
        } catch (Exception ex) {
            ex.printStackTrace();
            resp = new BaseResponse<PageResult<PageSearchAdminResponse>>();
            resp.setFailResp(ResultCodeConstant.UNKNOWERR, "查询管理员信息异常");
            return resp;
        }
    }

    /**
     * 获取除系统管理员之外的所有角色
     * @return
     */
    @ApiOperation(value = "获取除系统管理员之外的所有角色")
    @GetMapping(value = "/roles", produces = "application/json")
    @ResponseBody
    public BaseResponse<List<RoleDTO>> getAllRole(){
        List<RoleDTO> roleList = roleCache.getAllRole();
        BaseResponse<List<RoleDTO>> resp = new BaseResponse<List<RoleDTO>>().successful("查询角色成功");
        resp.setResult(roleList);
        return resp;
    }

}
