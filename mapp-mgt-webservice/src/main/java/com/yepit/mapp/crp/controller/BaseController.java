package com.yepit.mapp.crp.controller;

import com.yepit.mapp.crp.dto.system.AdminDTO;
import com.yepit.mapp.framework.constant.SessionConstant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

/**
 * Created by qianlong on 2017/8/20.
 */
public class BaseController {

    @Autowired
    private HttpSession httpSession;

    public AdminDTO getCurrentAdmin(){
        if(httpSession == null){
            return null;
        }
        Object admin = httpSession.getAttribute(SessionConstant.CURRENT_ADMIN);
        if(admin != null){
            return (AdminDTO)admin;
        }
        return null;
    }
}
