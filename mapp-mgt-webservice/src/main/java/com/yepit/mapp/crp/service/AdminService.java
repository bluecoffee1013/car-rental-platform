package com.yepit.mapp.crp.service;

import com.github.pagehelper.PageHelper;
import com.yepit.mapp.crp.cache.AdminCache;
import com.yepit.mapp.crp.constant.AdminStateEnum;
import com.yepit.mapp.crp.domain.system.SysAdmin;
import com.yepit.mapp.crp.domain.system.SysAdminRole;
import com.yepit.mapp.crp.domain.system.SysMenu;
import com.yepit.mapp.crp.domain.system.SysRole;
import com.yepit.mapp.crp.dto.system.*;
import com.yepit.mapp.crp.mapper.system.*;
import com.yepit.mapp.framework.constant.SessionConstant;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageArg;
import com.yepit.mapp.framework.dto.common.PageResult;
import com.yepit.mapp.framework.exception.ServiceException;
import com.yepit.mapp.framework.util.JsonUtils;
import com.yepit.mapp.framework.util.RedisUtils;
import com.yepit.mapp.framework.util.token.JWTUtils;
import com.yepit.mapp.framework.util.token.TokenDetailImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by qianlong on 2017/8/19.
 */
@Service
public class AdminService {

    final static Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Value("${system.passwordtype}")
    private Integer passwordType;

    @Value("${token.expiration}")
    private Long expiration;

    @Autowired
    private SysAdminMapper sysAdminMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysAdminRoleMapper sysAdminRoleMapper;

    @Autowired
    private SysRoleAuthMapper sysRoleAuthMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private HttpSession session;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AdminCache adminCache;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取所有管理员信息
     *
     * @return
     * @throws Exception
     */
    public List<SysAdmin> getAllSysAdmin() throws Exception {
        return sysAdminMapper.listByCond(new SysAdmin());
    }

    /**
     * 操作员登录鉴权
     *
     * @param adminLoginRequest
     * @return
     * @throws Exception
     */
    public BaseResponse<AdminLoginResponse> adminLogin(AdminLoginRequest adminLoginRequest) throws Exception {
        BaseResponse<AdminLoginResponse> resp = new BaseResponse<AdminLoginResponse>();
        SysAdmin cond = new SysAdmin();
        cond.setLoginName(adminLoginRequest.getLoginName());
        cond.setLoginPwd(adminLoginRequest.getLoginPwd());

        List<SysAdmin> adminList = sysAdminMapper.listByCond(cond);
        if (adminList == null || adminList.size() == 0) {
            resp = resp.setFailResp("000001", "登录帐号或密码错误");
            return resp;
        }

        //获取该操作员的所有权限
        List<SysMenu> allMenu = new ArrayList<SysMenu>();
        SysAdmin admin = adminList.get(0);
        if (admin.getStatus() != 1) {
            resp = resp.setFailResp("000002", "帐号状态不正常,请联系系统管理员");
            return resp;
        }

        SysAdminRole adminRoleCond = new SysAdminRole();
        adminRoleCond.setAdminId(admin.getAdminId());
        List<SysAdminRole> adminRoleList = sysAdminRoleMapper.listByCond(adminRoleCond);
        LinkedHashMap<String, SysMenu> allMenuMap = new LinkedHashMap<String, SysMenu>();

        if (adminRoleList != null && adminRoleList.size() > 0) {
            for (SysAdminRole adminRole : adminRoleList) {//根据roleId查询对应的功能项
                List<SysMenu> menuList = sysRoleAuthMapper.listByRoleId(adminRole.getRoleId());
                if (menuList != null && menuList.size() > 0) {
                    for (SysMenu menu : menuList) {
                        allMenuMap.put(menu.getMenuId(), menu);
                        if (!allMenu.contains(menu)) {//去重
                            allMenu.add(menu);
                        }
                    }
                }
            }
        }

        logger.info("用户拥有的菜单权限:" + JsonUtils.Object2Json(allMenu));

        //获取当前系统所有菜单项
        List<SysMenu> systemMenus = sysMenuMapper.listByCond(new SysMenu());

        List<MenuDTO> adminMenus = new ArrayList<MenuDTO>();
        //将功能项转化为树结构
        for (SysMenu menu : allMenu) {
            if (menu.getParentMenuId().equals("0")) {//根据一级菜单获取对应的二级菜单
                MenuDTO menuDTO = new MenuDTO();
                menuDTO.setParentMenu(menu);
                List<SysMenu> childrenMenus = new ArrayList<SysMenu>();
                //查询下级菜单
                for (SysMenu childrenMenu : systemMenus) {
                    if (childrenMenu.getParentMenuId().equals(menu.getMenuId()) && allMenuMap.get(childrenMenu.getMenuId()) != null) {
                        childrenMenus.add(childrenMenu);
                    }
                }
                menuDTO.setChildrenMenu(childrenMenus);
                adminMenus.add(menuDTO);
            }
        }

        //生成token
        TokenDetailImpl tokenDetail = new TokenDetailImpl(admin.getLoginName());
        String accessToken = jwtUtils.generateToken(tokenDetail);

        AdminLoginResponse adminLoginResp = new AdminLoginResponse();
        adminLoginResp.setAdmin(admin);
        adminLoginResp.setAccessToken(accessToken);
        adminLoginResp.setAdminMenus(adminMenus);

        resp = resp.setSuccessfulResp("管理员登录鉴权成功");
        resp.setResult(adminLoginResp);

        //将登录信息放入session中
        session.setAttribute(SessionConstant.CURRENT_ADMIN, admin);

        //token放入缓存中
        redisUtils.set(accessToken, admin.getLoginName(), expiration);
        return resp;
    }

    /**
     * 操作员鉴权
     *
     * @param adminLoginRequest
     * @return
     * @throws Exception
     */
    public BaseResponse<AdminAuthResponse> adminAuth(AdminLoginRequest adminLoginRequest) throws Exception {
        BaseResponse<AdminAuthResponse> resp = new BaseResponse<AdminAuthResponse>();
        SysAdmin cond = new SysAdmin();
        cond.setLoginName(adminLoginRequest.getLoginName());
        cond.setLoginPwd(adminLoginRequest.getLoginPwd());

        List<SysAdmin> adminList = sysAdminMapper.listByCond(cond);
        if (adminList == null || adminList.size() == 0) {
            resp = resp.setFailResp("000001", "登录帐号或密码错误");
            return resp;
        }

        //获取该操作员的所有权限
        SysAdmin admin = adminList.get(0);
        if (admin.getStatus() != 1) {
            resp = resp.setFailResp("000002", "帐号已注销,请联系系统管理员");
            return resp;
        }

        //生成token
        TokenDetailImpl tokenDetail = new TokenDetailImpl(admin.getLoginName());
        String accessToken = jwtUtils.generateToken(tokenDetail);

        //token放入缓存中
        redisUtils.set(accessToken, admin.getLoginName(), expiration);

        AdminAuthResponse adminAuthResp = new AdminAuthResponse();
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        adminAuthResp.setAdminInfo(adminDTO);
        adminAuthResp.setAccessToken(accessToken);

        resp = resp.setSuccessfulResp("鉴权成功");
        resp.setResult(adminAuthResp);
        return resp;
    }

    /**
     * 创建管理员
     *
     * @param admin
     * @param roleId
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    public BaseResponse createAdmin(SysAdmin admin, Long roleId) throws Exception {
        try {
            BaseResponse resp = new BaseResponse();

            //检查Admin必填信息
            if (StringUtils.isBlank(admin.getLoginName())) {
                return resp.setFailResp("000001", "请输入登录用户名");
            }
            if (StringUtils.isBlank(admin.getLoginPwd())) {
                return resp.setFailResp("000002", "请输入登录密码");
            }

            //校验login_name是否重复
            List<SysAdmin> adminList = sysAdminMapper.listByCond(admin);
            if (adminList != null && adminList.size() > 0) {
                return resp.setFailResp("000004", "登录名不能重复");
            }

            //根据roleId检查是否有role
            if (roleId == null) {
                return resp.setFailResp("000003", "请选择一个角色");
            }
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
            if (sysRole == null) {
                throw new ServiceException("0000003", "请选择一个角色");
            }

            //创建管理员
            admin.setCreateTime(new Date());
            admin.setPwdType(passwordType);
            //正常状态
            admin.setStatus(AdminStateEnum.Normal.getValue());
            sysAdminMapper.insert(admin);

            //关联角色
            SysAdminRole sysAdminRole = new SysAdminRole();
            sysAdminRole.setAdminId(admin.getAdminId());
            sysAdminRole.setRoleId(roleId);
            sysAdminRoleMapper.insert(sysAdminRole);

            //刷新缓存
            adminCache.reload();

            resp = resp.setSuccessfulResp("创建操作员成功");
            return resp;
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 修改操作员信息
     *
     * @param admin
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, Exception.class})
    public BaseResponse updateAdmin(SysAdmin admin, Long newRoleId) throws Exception {
        BaseResponse resp = new BaseResponse();
        try {
            //查询用户是否存在
            SysAdmin adminTmp = sysAdminMapper.selectByPrimaryKey(admin.getAdminId());
            if (adminTmp == null) {
                return resp.setFailResp("000003", "要修改的管理员不存在");
            }

            //登录名不能修改
            admin.setLoginName(adminTmp.getLoginName());
            admin.setUpdateTime(new Date());

            sysAdminMapper.updateByPrimaryKeySelective(admin);

            if (newRoleId != null) {
                SysAdminRole currentRole = this.getAdminRole(admin.getAdminId());
                if (currentRole != null) {
                    currentRole.setRoleId(newRoleId);
                    sysAdminRoleMapper.updateByPrimaryKeySelective(currentRole);
                }
            }

            //刷新缓存
            adminCache.reload();

            resp = resp.setSuccessfulResp("修改操作员信息成功");
            return resp;
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 更新管理员角色
     *
     * @param adminId
     * @param newRoleId
     * @return
     * @throws ServiceException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, Exception.class})
    public BaseResponse updateAdminRole(Long adminId, Long newRoleId) throws ServiceException {
        //查询用户是否存在
        SysAdmin adminTmp = sysAdminMapper.selectByPrimaryKey(adminId);
        if (adminTmp == null) {
            throw new ServiceException("000003", "要修改的管理员不存在");
        }

        SysAdminRole currentRole = this.getAdminRole(adminId);
        if (currentRole != null) {
            currentRole.setRoleId(newRoleId);
            sysAdminRoleMapper.updateByPrimaryKeySelective(currentRole);
        } else {//角色不存在则新增一个
            SysAdminRole newRole = new SysAdminRole();
            newRole.setAdminId(adminId);
            newRole.setAdminRoleId(newRoleId);
            sysAdminRoleMapper.insertSelective(newRole);
        }
        BaseResponse resp = new BaseResponse();
        resp = resp.setSuccessfulResp("更改角色成功");
        return resp;
    }

    /**
     * 获取操作员详细信息
     *
     * @param adminId
     * @return
     */
    public BaseResponse<SysAdmin> getAdminById(Long adminId) {
        BaseResponse<SysAdmin> resp = new BaseResponse<SysAdmin>();

        SysAdmin admin = sysAdminMapper.selectByPrimaryKey(adminId);
        if (admin == null) {
            resp = resp.setFailResp("000001", "操作员不存在");
            return resp;
        }

        resp = resp.setSuccessfulResp("获取操作员信息成功");
        resp.setResult(admin);
        return resp;
    }

    /**
     * 分页查询管理员信息
     *
     * @param request
     * @return
     */
    public BaseResponse<PageResult<PageSearchAdminResponse>> listAdminByCond(PageSearchAdminRequest request) {
        BaseResponse<PageResult<PageSearchAdminResponse>> resp = new BaseResponse<PageResult<PageSearchAdminResponse>>();
        PageArg pageArg = request.getPage();
        if (pageArg != null) {
            PageHelper.startPage(pageArg.getPageNum(), pageArg.getPageSize());
        }

        SysAdmin cond = new SysAdmin();
        BeanUtils.copyProperties(request, cond);
        List<PageSearchAdminResponse> adminList = sysAdminMapper.pageListByCond(cond);

        PageResult<PageSearchAdminResponse> searchResult = new PageResult<PageSearchAdminResponse>(adminList);
        resp = resp.setSuccessfulResp("查询管理员信息成功");
        resp.setResult(searchResult);
        return resp;
    }

    /**
     * 根据主键查询唯一角色
     *
     * @param adminId
     * @return
     */
    public SysAdminRole getAdminRole(Long adminId) {
        SysAdminRole queryCond = new SysAdminRole();
        queryCond.setAdminId(adminId);
        List<SysAdminRole> sysAdminRoles = sysAdminRoleMapper.listByCond(queryCond);
        if (sysAdminRoles == null || sysAdminRoles.size() == 0) {
            return null;
        }
        return sysAdminRoles.get(0);
    }
}
