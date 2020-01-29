package com.yepit.mapp.crp.service;

import com.yepit.mapp.crp.domain.system.SequenceDef;
import com.yepit.mapp.crp.mapper.system.SequenceDefMapper;
import com.yepit.mapp.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qianlong on 2017/8/17.
 */
@Service
public class SequenceDefService {

    @Autowired
    private SequenceDefMapper sequenceDefMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public synchronized String nextId(String tableName, String columnName) {
        SequenceDef cond = new SequenceDef();
        cond.setTableName(tableName);
        cond.setColumnName(columnName);

        //当前序列号+1
        int updateSize = sequenceDefMapper.getNextId(cond);
        if (updateSize == 0) {
            throw new ServiceException("999997","没有定义主键序列");
        }

        List<SequenceDef> list = sequenceDefMapper.listSequencDef(cond);
        if (list == null || list.size() == 0) {
            throw new ServiceException("999997","没有定义主键序列");
        }

        SequenceDef sequenceDef = list.get(0);
        String currentValue = sequenceDef.getValue().toString();

        int length = sequenceDef.getLength();
        return String.format("%0" + length + "d", Integer.parseInt(currentValue));
    }

    /**
     * 不够位数的在前面补0，保留code的长度位数字
     *
     * @param code
     * @return
     */
    static String autoGenericCode(String code) {
        String result = "";
        // 保留code的位数
        result = String.format("%0" + code.length() + "d", Integer.parseInt(code) + 1);

        return result;
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param code
     * @return
     */
    static String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code));

        return result;
    }

    public static void main(String[] args) {
        System.out.println(autoGenericCode("10011"));
        System.out.println(autoGenericCode("1",10));
        System.out.println(autoGenericCode("1",10).length());
    }
}
