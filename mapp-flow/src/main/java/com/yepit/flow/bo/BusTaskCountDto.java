package com.yepit.flow.bo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/28.
 */
public class BusTaskCountDto implements Serializable {
    private Integer todoNumber;

    public Integer getTodoNumber() {
        return todoNumber;
    }

    public void setTodoNumber(Integer todoNumber) {
        this.todoNumber = todoNumber;
    }

    public Integer getDoneNumber() {
        return doneNumber;
    }

    public void setDoneNumber(Integer doneNumber) {
        this.doneNumber = doneNumber;
    }

    private Integer doneNumber;
}
