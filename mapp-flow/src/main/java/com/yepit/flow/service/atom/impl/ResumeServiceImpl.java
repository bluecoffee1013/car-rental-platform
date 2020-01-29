package com.yepit.flow.service.atom.impl;


import com.yepit.flow.service.atom.interfaces.IResumeService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/10/26.
 */
@Service("resumeService")
public class ResumeServiceImpl implements IResumeService {
    @Override
    public void storeResume(String args) {
        System.out.println("任务已经执行....................................."+args);
    }
}
