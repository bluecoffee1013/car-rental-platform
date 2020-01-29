package com.yepit.mapp.crp.mongo.entity;

import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

import java.io.Serializable;
import java.util.List;

/**
 * 用户在流程中的权限
 *
 * @author qianlong
 */
@Data
@Entity("user_privilege")
public class UserPrivilege implements Serializable{

    private static final long serialVersionUID = 7642305226511796343L;
    @Indexed
    private Long userId;

    private List<FlowPrivilege> privileges;
}
