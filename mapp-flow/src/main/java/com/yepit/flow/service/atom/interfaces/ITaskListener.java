package com.yepit.flow.service.atom.interfaces;

import org.activiti.engine.delegate.DelegateTask;

/**
 * Created by Administrator on 2018/10/30.
 */
public interface ITaskListener {
    /**指定个人任务和组任务的办理人*/
    public void notify(DelegateTask delegateTask);

    /**
     *  前置任务
     *@author  Cavin
     **/
    public void notifyBefore(DelegateTask delegateTask,Long userId,String userName,Long busId)throws Exception;

    public void notifyAfter(DelegateTask delegateTask,Long userId,String userName,Long busId)throws Exception;

    /**
     *  end节点事件
     *@author  Cavin
     **/
    public void endNode(Long userId,String userName,Long busId)throws Exception;
}
