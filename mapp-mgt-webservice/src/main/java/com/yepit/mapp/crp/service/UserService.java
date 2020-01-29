package com.yepit.mapp.crp.service;

import com.aliyuncs.ram.model.v20150501.UpdateRoleRequest;
import com.github.pagehelper.PageHelper;
import com.yepit.mapp.crp.cache.AdminCache;
import com.yepit.mapp.crp.cache.RoleCache;
import com.yepit.mapp.crp.constant.AdminStateEnum;
import com.yepit.mapp.crp.constant.OTPTypeEnum;
import com.yepit.mapp.crp.domain.system.SysAdmin;
import com.yepit.mapp.crp.domain.system.SysAdminRole;
import com.yepit.mapp.crp.domain.system.SysRole;
import com.yepit.mapp.crp.dto.system.*;
import com.yepit.mapp.crp.dto.user.*;
import com.yepit.mapp.crp.mapper.system.SysAdminMapper;
import com.yepit.mapp.crp.mapper.system.SysAdminRoleMapper;
import com.yepit.mapp.crp.mapper.system.SysRoleMapper;
import com.yepit.mapp.crp.mongo.entity.FlowNode;
import com.yepit.mapp.crp.mongo.entity.FlowPrivilege;
import com.yepit.mapp.crp.mongo.entity.UserPrivilege;
import com.yepit.mapp.framework.constant.SessionConstant;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageArg;
import com.yepit.mapp.framework.dto.common.PageResult;
import com.yepit.mapp.framework.exception.ServiceException;
import com.yepit.mapp.framework.util.JsonUtils;
import com.yepit.mapp.framework.util.PasswordUtils;
import com.yepit.mapp.framework.util.RedisUtils;
import com.yepit.mapp.framework.util.sms.DianTaoSMSUtils;
import com.yepit.mapp.framework.util.token.JWTUtils;
import com.yepit.mapp.framework.util.token.TokenDetailImpl;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SysAdminMapper sysAdminMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysAdminRoleMapper sysAdminRoleMapper;

    @Autowired
    private Datastore ds;

    @Autowired
    private AdminCache adminCache;

    @Value("${system.passwordtype}")
    private Integer passwordType;

    @Autowired
    private FlowNodeService flowNodeService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private HttpSession session;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${token.expiration}")
    private Long expiration;

    @Value("${otp.expiration}")
    private Long otpExpiration;

    @Autowired
    private RoleCache roleCache;

    /**
     * 校验登录名是否存在
     *
     * @param loginName
     * @return
     */
    public BaseResponse vaildUser(String loginName) {
        SysAdmin queryCond = new SysAdmin();
        queryCond.setLoginName(loginName);
        List<SysAdmin> adminList = sysAdminMapper.listByCond(queryCond);
        if (adminList == null || adminList.size() == 0) {
            throw new ServiceException("000001", "用户不存在");
        }
        return new BaseResponse().successful("用户存在");
    }

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    public BaseResponse<LoginResp> login(LoginRequest request) {
        SysAdmin queryCond = new SysAdmin();
        queryCond.setLoginName(request.getLoginName());
        queryCond.setLoginPwd(request.getLoginPwd());

        //检查Admin必填信息
        if (StringUtils.isBlank(request.getLoginName())) {
            throw new ServiceException("000001", "请输入登录用户名");
        }
        if (StringUtils.isBlank(request.getLoginPwd())) {
            throw new ServiceException("000002", "请输入登录密码");
        }

        List<SysAdmin> adminList = sysAdminMapper.listByCond(queryCond);
        if (adminList == null || adminList.size() == 0) {
            throw new ServiceException("000004", "登录名或密码错误");
        }

        SysAdmin admin = adminList.get(0);
        if (admin.getStatus().equals(AdminStateEnum.Cancel.getValue())) {
            throw new ServiceException("000005", "帐号已注销,请联系系统管理员");
        } else if (admin.getStatus().equals(AdminStateEnum.WaitingForApprove.getValue())) {
            throw new ServiceException("000005", "帐号待审核,请联系系统管理员");
        }

        //获取用户角色
        SysAdminRole roleCond = new SysAdminRole();
        roleCond.setAdminId(admin.getAdminId());
        List<SysAdminRole> adminRoleList = sysAdminRoleMapper.listByCond(roleCond);

        //目前用户只可能有一个角色
        if (adminRoleList == null || adminRoleList.size() == 0) {
            throw new ServiceException("000006", "该用户没有角色,请联系系统管理员");
        }
        RoleDTO roleDTO = new RoleDTO();
        Long roleId = adminRoleList.get(0).getRoleId();
        roleDTO.setRoleId(roleId);
        roleDTO.setRoleName(roleCache.getRoleNameByRoleId(String.valueOf(roleId)));

        //获取职能
        List<RolePrivilege> rolePrivileges = new ArrayList<RolePrivilege>();
        UserPrivilege userPrivilege = ds.createQuery(UserPrivilege.class).filter("userId =", admin.getAdminId()).get();
        if (userPrivilege != null) {
            List<FlowPrivilege> flowPrivileges = userPrivilege.getPrivileges();
            if (flowPrivileges != null && flowPrivileges.size() > 0) {
                for (FlowPrivilege flowPrivilege : flowPrivileges) {
                    RolePrivilege rolePrivilege = new RolePrivilege();
                    BeanUtils.copyProperties(flowPrivilege, rolePrivilege);
                    rolePrivilege.setRoleId(roleId);
                    rolePrivileges.add(rolePrivilege);
                }
            }
        }

        //生成token
        TokenDetailImpl tokenDetail = new TokenDetailImpl(admin.getLoginName());
        String accessToken = jwtUtils.generateToken(tokenDetail);

        BaseResponse<LoginResp> resp = new BaseResponse<LoginResp>();

        LoginResp loginResp = new LoginResp();
        AdminDTO userInfo = new AdminDTO();
        BeanUtils.copyProperties(admin, userInfo);
        loginResp.setUserInfo(userInfo);
        loginResp.setRole(roleDTO);
        loginResp.setUserPrivileges(rolePrivileges);
        loginResp.setAccessToken(accessToken);

        resp = resp.setSuccessfulResp("用户登录鉴权成功");
        resp.setResult(loginResp);

        try {
            AdminDTO adminDTO = new AdminDTO();
            BeanUtils.copyProperties(admin,adminDTO);
            adminDTO.setRoleId(roleId);
            //token放入缓存中
            redisUtils.set(accessToken, JsonUtils.Object2Json(adminDTO), expiration);
        } catch (Exception ex) {
            throw new ServiceException("Token放入缓存异常:" + ex.getMessage());
        }

        return resp;
    }

    /**
     * 用户注册
     *
     * @param registerRequest
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    public BaseResponse register(RegisterRequest registerRequest) throws ServiceException, Exception {
        //校验短信验证码是否正确
        String authCode = registerRequest.getAuthCode();

        SysAdmin admin = new SysAdmin();
        admin.setLoginName(registerRequest.getPhoneNumber());
        admin.setMobile(registerRequest.getPhoneNumber());
        admin.setRealname(registerRequest.getRealName());
        admin.setLoginPwd(registerRequest.getPassword());
        admin.setStatus(AdminStateEnum.WaitingForApprove.getValue());

        //检查Admin必填信息
        if (StringUtils.isBlank(admin.getLoginName())) {
            throw new ServiceException("000001", "请输入登录用户名");
        }
        if (StringUtils.isBlank(admin.getLoginPwd())) {
            throw new ServiceException("000002", "请输入登录密码");
        }

        //校验login_name是否重复
        List<SysAdmin> adminList = sysAdminMapper.listByCond(admin);
        if (adminList != null && adminList.size() > 0) {
            throw new ServiceException("000004", "登录名不能重复");
        }

        //根据roleId检查是否有role
        Long roleId = registerRequest.getRoleId();
        if (roleId == null) {
            throw new ServiceException("000003", "请选择一个角色");
        }
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
        if (sysRole == null) {
            throw new ServiceException("0000003", "请选择一个角色");
        }

        //创建管理员
        admin.setCreateTime(new Date());
        admin.setPwdType(passwordType);
        //正常状态
        admin.setStatus(AdminStateEnum.WaitingForApprove.getValue());
        sysAdminMapper.insert(admin);

        //关联角色
        SysAdminRole sysAdminRole = new SysAdminRole();
        sysAdminRole.setAdminId(admin.getAdminId());
        sysAdminRole.setRoleId(roleId);
        sysAdminRoleMapper.insert(sysAdminRole);

        //刷新缓存
        adminCache.reload();

        BaseResponse resp = new BaseResponse().successful("注册成功");
        return resp;
    }

    /**
     * 分页查询用户信息
     *
     * @param queryCond
     * @return
     */
    public BaseResponse<PageResult<PageSearchAdminResponse>> searchUserByCond(PageSearchAdminRequest queryCond) {
        BaseResponse<PageResult<PageSearchAdminResponse>> resp = new BaseResponse<PageResult<PageSearchAdminResponse>>();
        PageArg pageArg = queryCond.getPage();
        if (pageArg != null) {
            PageHelper.startPage(pageArg.getPageNum(), pageArg.getPageSize());
        }

        List<PageSearchAdminResponse> adminList = sysAdminMapper.searchByCond(queryCond);

        PageResult<PageSearchAdminResponse> searchResult = new PageResult<PageSearchAdminResponse>(adminList);
        resp = resp.setSuccessfulResp("查询管理员信息成功");
        resp.setResult(searchResult);
        return resp;
    }

    /**
     * 获取用户详细信息
     *
     * @param adminId
     * @return
     */
    public BaseResponse<GetUserInfoResp> getUserInfoDetail(Long adminId) {
        SysAdmin admin = sysAdminMapper.selectByPrimaryKey(adminId);
        if (admin == null) {
            throw new ServiceException("用户不存在");
        }
        GetUserInfoResp userInfoResp = new GetUserInfoResp();
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        userInfoResp.setUserInfo(adminDTO);

        SysAdminRole adminRole = adminService.getAdminRole(adminId);
        if (adminRole != null) {
            List<FlowNode> flowNodeList = ds.createQuery(FlowNode.class)
                    .filter("roleId =", adminRole.getRoleId())
                    .asList();
            if (flowNodeList != null && flowNodeList.size() > 0) {
                List<UserPrivilegeDTO> privileges = new ArrayList<UserPrivilegeDTO>(flowNodeList.size());
                for (FlowNode flowNode : flowNodeList) {
                    UserPrivilegeDTO privilege = new UserPrivilegeDTO();
                    privilege.setRoleId(flowNode.getRoleId());
                    privilege.setRoleName(flowNode.getRoleName());
                    privilege.setNodeId(flowNode.getNodeId());
                    privilege.setNodeName(flowNode.getNodeName());
                    privilege.setAdminId(admin.getAdminId());
                    privileges.add(privilege);
                }
                userInfoResp.setUserPrivilegeList(privileges);
            }
        }
        BaseResponse<GetUserInfoResp> resp = new BaseResponse<GetUserInfoResp>().successful("查询用户信息成功");
        resp.setResult(userInfoResp);
        return resp;
    }

    /**
     * 用户激活
     *
     * @param request
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    public BaseResponse userActive(UserActiveRequest request) {
        Long userId = request.getUserId();
        SysAdmin admin = sysAdminMapper.selectByPrimaryKey(userId);
        if (admin == null) {
            throw new ServiceException("用户不存在");
        }
        admin.setStatus(AdminStateEnum.Normal.getValue());
        sysAdminMapper.updateByPrimaryKey(admin);

        //获取用户当前角色
        SysAdminRole queryCond = new SysAdminRole();
        queryCond.setAdminId(userId);
        List<SysAdminRole> roleList = sysAdminRoleMapper.listByCond(queryCond);
        Long roleId = null;
        if (roleList != null && roleList.size() > 0) {
            roleId = roleList.get(0).getRoleId();
        }
        List<Long> flowNodeIdList = request.getFlowNodeId();
        //设置审核权限
        this.insertUserPrivilege(userId, roleId, flowNodeIdList);
//        if (flowNodeIdList != null && flowNodeIdList.size() > 0) {
//            ds.delete(ds.createQuery(UserPrivilege.class).filter("userId =", userId));
//            UserPrivilege userPrivilege = new UserPrivilege();
//            userPrivilege.setUserId(userId);
//            List<FlowPrivilege> privilegeList = new ArrayList<FlowPrivilege>();
//            for (Long flowNodeId : flowNodeIdList) {
//                FlowNode flowNode = flowNodeService.getFlowNode(flowNodeId);
//                if (flowNode != null) {
//                    FlowPrivilege flowPrivilege = this.flowNode2Privilege(flowNode);
//                    privilegeList.add(flowPrivilege);
//                }
//            }
//            userPrivilege.setPrivileges(privilegeList);
//
//            ds.save(userPrivilege);
//        }

        //刷新缓存
        adminCache.reload();

        return new BaseResponse().successful("帐号激活成功");
    }

    /**
     * 修改状态
     *
     * @param userId
     * @param newStatus
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    public BaseResponse changeUserStatus(Long userId, Integer newStatus) {
        SysAdmin admin = sysAdminMapper.selectByPrimaryKey(userId);
        if (admin == null) {
            throw new ServiceException("用户不存在");
        }
        admin.setStatus(newStatus);
        sysAdminMapper.updateByPrimaryKey(admin);

        //刷新缓存
        adminCache.reload();
        String successfulMsg = "";
        if (newStatus.equals(AdminStateEnum.Normal.getValue())) {
            successfulMsg = "帐号激活成功";
        } else if (newStatus.equals(AdminStateEnum.Cancel.getValue())) {
            //同时删除用户处理任务的权限
            ds.delete(ds.createQuery(UserPrivilege.class).filter("userId =", userId));
            successfulMsg = "帐号注销成功";
        }
        return new BaseResponse().successful(successfulMsg);
    }

    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    public BaseResponse updateLoginPwd(UpdateLoginPwdRequest request) {
        String loginName = request.getLoginName();
        String oldPwd = request.getOldPwd();
        String newPwd = request.getNewPwd();

        //检查必填信息
        if (StringUtils.isBlank(loginName)) {
            throw new ServiceException("000001", "请输入登录用户名");
        }
        if (StringUtils.isBlank(oldPwd)) {
            throw new ServiceException("000002", "请输入旧密码");
        }
        if (StringUtils.isBlank(newPwd)) {
            throw new ServiceException("000003", "请输入新密码");
        }

        //检查旧密码是否正确
        SysAdmin queryCond = new SysAdmin();
        queryCond.setLoginName(loginName);
        queryCond.setLoginPwd(oldPwd);
        List<SysAdmin> adminList = sysAdminMapper.listByCond(queryCond);
        if (adminList == null || adminList.size() == 0) {
            throw new ServiceException("000004", "登录名或旧密码错误");
        }

        SysAdmin admin = adminList.get(0);
        if (admin.getStatus().equals(AdminStateEnum.Cancel.getValue())) {
            throw new ServiceException("000005", "帐号已注销,请联系系统管理员");
        } else if (admin.getStatus().equals(AdminStateEnum.WaitingForApprove.getValue())) {
            throw new ServiceException("000005", "帐号待审核,请联系系统管理员");
        }

        admin.setLoginPwd(newPwd);
        admin.setUpdateTime(new Date());
        admin.setUpdateOperator(loginName);

        sysAdminMapper.updateByPrimaryKey(admin);

        //刷新缓存
        adminCache.reload();

        return new BaseResponse().successful("修改登录密码成功");
    }

    /**
     * 重新设置密码
     *
     * @param request
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    public BaseResponse resetLoginPwd(ResetPwdRequest request) {
        String loginName = request.getLoginName();
        String newPwd = request.getNewPwd();
        String confirmPwd = request.getConfirmPwd();
        String otp = request.getOtp();

        //检查必填信息
        if (StringUtils.isBlank(loginName)) {
            throw new ServiceException("000001", "请输入登录用户名");
        }
        if (StringUtils.isBlank(newPwd)) {
            throw new ServiceException("000002", "请输入新密码");
        }
        if (StringUtils.isBlank(confirmPwd)) {
            throw new ServiceException("000003", "请输入确认密码");
        }
        if (StringUtils.isBlank(otp)) {
            throw new ServiceException("000004", "请输入验证码");
        }
        if (!confirmPwd.equals(newPwd)) {
            throw new ServiceException("000005", "两次密码输入不一致");
        }

        //校验验证码是正确
        BaseResponse verifyOTPResult = this.verifyOTP(OTPTypeEnum.ModifyPwd.getValue(),loginName,otp);
        if(!verifyOTPResult.isSuccess()){
            return verifyOTPResult;
        }

        //检查登录名是否正确
        SysAdmin queryCond = new SysAdmin();
        queryCond.setLoginName(loginName);
        List<SysAdmin> adminList = sysAdminMapper.listByCond(queryCond);
        if (adminList == null || adminList.size() == 0) {
            throw new ServiceException("000005", "登录名错误");
        }

        SysAdmin admin = adminList.get(0);
        if (admin.getStatus().equals(AdminStateEnum.Cancel.getValue())) {
            throw new ServiceException("000005", "帐号已注销,请联系系统管理员");
        } else if (admin.getStatus().equals(AdminStateEnum.WaitingForApprove.getValue())) {
            throw new ServiceException("000005", "帐号待审核,请联系系统管理员");
        }

        admin.setLoginPwd(newPwd);
        admin.setUpdateTime(new Date());
        admin.setUpdateOperator(loginName);

        sysAdminMapper.updateByPrimaryKey(admin);

        //修改成功则删除redis中的值
        String key = OTPTypeEnum.getDescByValue(OTPTypeEnum.ModifyPwd.getValue()) + "_" + loginName;
        redisUtils.remove(key);

        //刷新缓存
        adminCache.reload();

        return new BaseResponse().successful("重设登录密码成功");
    }

    public AdminDTO getTaskAssigner(String nodeName) {
        FlowPrivilege flowPrivilege = new FlowPrivilege();
        flowPrivilege.setNodeAliasName(nodeName);

        List<UserPrivilege> result = ds.createQuery(UserPrivilege.class)
                .field("privileges")
                .hasThisElement(flowPrivilege)
                .asList();
        if (result != null && result.size() > 0) {
            int random = this.getRandom(0, result.size());
            if (random == 1) {
                random = 0;
            } else if (random > result.size()) {
                random = result.size();
            }
            String userId = result.get(random).getUserId().toString();
            AdminDTO adminDTO = adminCache.getAdminById(userId);
            return adminDTO;
        } else {
            return null;
        }
    }

    private int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    public BaseResponse<List<RolePrivilege>> getPrivilegesByRole(Long roleId) {
        List<FlowNode> flowNodeList = ds.createQuery(FlowNode.class).filter("roleId =", roleId).asList();
        List<RolePrivilege> rolePrivileges = null;
        if (flowNodeList != null && flowNodeList.size() > 0) {
            rolePrivileges = new ArrayList<>(flowNodeList.size());
            for (FlowNode flowNode : flowNodeList) {
                RolePrivilege rolePrivilege = new RolePrivilege();
                rolePrivilege.setRoleId(roleId);
                rolePrivilege.setNodeId(flowNode.getNodeId());
                rolePrivilege.setNodeName(flowNode.getNodeName());
                rolePrivileges.add(rolePrivilege);
            }
        }
        BaseResponse<List<RolePrivilege>> resp = new BaseResponse<List<RolePrivilege>>().successful("查询成功");
        resp.setResult(rolePrivileges);
        return resp;
    }

    /**
     * 修改用户角色
     *
     * @param request
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    public BaseResponse changeUserRole(ChangeRoleRequest request) {
        Long userId = request.getUserid();
        AdminDTO userInfo = adminCache.getAdminById(String.valueOf(userId));
        if (userInfo == null) {
            throw new ServiceException("用户不存在");
        }

        Long newRoleId = request.getNewRoleId();
        SysAdminRole queryCond = new SysAdminRole();
        queryCond.setAdminId(userId);
        List<SysAdminRole> sysAdminRoleList = sysAdminRoleMapper.listByCond(queryCond);
        if (sysAdminRoleList != null && sysAdminRoleList.size() > 0) {
            SysAdminRole sysAdminRole = sysAdminRoleList.get(0);
            sysAdminRole.setRoleId(request.getNewRoleId());
            sysAdminRoleMapper.updateByPrimaryKey(sysAdminRole);
        }

        Integer userStatus = userInfo.getStatus();
        if (userStatus.equals(AdminStateEnum.Normal.getValue())) {
            this.insertUserPrivilege(userId, newRoleId, request.getFlowNodeIds());
        }
        return new BaseResponse().successful("修改部门角色成功");
    }

    /**
     * 对象转换
     *
     * @param flowNode
     * @return
     */
    private FlowPrivilege flowNode2Privilege(FlowNode flowNode) {
        if (flowNode != null) {
            FlowPrivilege flowPrivilege = new FlowPrivilege();
            flowPrivilege.setNodeId(flowNode.getNodeId());
            flowPrivilege.setNodeName(flowNode.getNodeName());
            flowPrivilege.setNodeAliasName(flowNode.getNodeAliasName());
            return flowPrivilege;
        }
        return null;
    }

    /**
     * 根据流程节点ID设置用户权限
     *
     * @param userId
     * @param roleId
     * @param flowNodeIds
     */
    private void insertUserPrivilege(Long userId, Long roleId, List<Long> flowNodeIds) {
        List<FlowNode> flowNodeList = null;
        UserPrivilege userPrivilege = new UserPrivilege();
        userPrivilege.setUserId(userId);
        List<FlowPrivilege> privilegeList = new ArrayList<FlowPrivilege>();
        if (roleId != null && (flowNodeIds == null || flowNodeIds.size() == 0)) {
            //取角色默认的所有审批节点权限
            flowNodeList = ds.createQuery(FlowNode.class).filter("roleId =", roleId).asList();
        } else {
            flowNodeList = new ArrayList<FlowNode>();
            for (Long flowNodeId : flowNodeIds) {
                FlowNode flowNode = flowNodeService.getFlowNode(flowNodeId);
                //校验FlowNode是否正确
                if (flowNode != null) {
                    flowNodeList.add(flowNode);
                }
            }
        }
        //先删除后重新添加
        for (FlowNode flowNode : flowNodeList) {
            FlowPrivilege flowPrivilege = this.flowNode2Privilege(flowNode);
            privilegeList.add(flowPrivilege);
        }
        ds.delete(ds.createQuery(UserPrivilege.class).filter("userId =", userId));
        userPrivilege.setPrivileges(privilegeList);
        ds.save(userPrivilege);
    }

    /**
     * 获取OTP
     *
     * @param otpType   验证码类型
     * @param loginName 用户号码
     * @return
     */
    public BaseResponse<String> getOTP(Integer otpType, String loginName) {
        SysAdmin queryCond = new SysAdmin();
        queryCond.setLoginName(loginName);
        List<SysAdmin> adminList = sysAdminMapper.listByCond(queryCond);
        if (adminList == null || adminList.size() == 0) {
            throw new ServiceException("000001", "用户不存在");
        }
        SysAdmin sysAdmin = adminList.get(0);
        String mobile = sysAdmin.getMobile();
        if (StringUtils.isBlank(mobile)) {
            throw new ServiceException("000002", "该用户没有设置手机号码,无法发送短信验证码");
        }

        //校验该号码的验证码是否在有效期内,只有不在有效期内才会重新发送
        String key = OTPTypeEnum.getDescByValue(otpType) + "_" + mobile;
        Object oldOtp = redisUtils.get(key);
        if (oldOtp != null) {
            throw new ServiceException("000003", "您的操作太快了,请稍候再试");
        }

        //生成6位随机OTP
        String otp = PasswordUtils.getNumberRandom(6);
        redisUtils.set(key, otp, otpExpiration);

        //发送短信验证码
        String smsConent = "您的短信验证码为" + otp + ",1分钟内有效.";
        try {
            String result = DianTaoSMSUtils.sendSms(mobile, smsConent);
            if (StringUtils.isBlank(result) || result.indexOf("ok") < 0) {
                throw new ServiceException("000004", "短信发送失败:" + result);
            }
        } catch (Exception ex) {
            throw new ServiceException("发送短信失败:" + ex.getMessage());
        }
        BaseResponse<String> resp = new BaseResponse<String>().successful("获取验证码成功");
        resp.setResult(smsConent);
        return resp;
    }

    /**
     * 校验验证码是否正确
     *
     * @param otpType
     * @param mobile
     * @param otp
     * @return
     */
    public BaseResponse verifyOTP(Integer otpType, String mobile, String otp) {
        if (StringUtils.isBlank(mobile)) {
            throw new ServiceException("000001", "用户号码不能为空");
        }
        if (StringUtils.isBlank(otp)) {
            throw new ServiceException("000002", "验证码不能为空");
        }
        String key = OTPTypeEnum.getDescByValue(otpType) + "_" + mobile;
        Object otpObj = redisUtils.get(key);
        if (otpObj == null) {
            throw new ServiceException("000003", "验证码已过期,请重新获取");
        }

        if (!otp.equals((String) otpObj)) {
            throw new ServiceException("000004", "验证码不正确");
        }

        return new BaseResponse().successful("验证通过");

    }
}
