package com.yepit.mapp.crp.mapper.system;


import com.yepit.mapp.crp.domain.system.SequenceDef;

import java.util.List;

public interface SequenceDefMapper {
    int deleteByPrimaryKey(Long sequenceId);

    int insert(SequenceDef record);

    int insertSelective(SequenceDef record);

    SequenceDef selectByPrimaryKey(Long sequenceId);

    int updateByPrimaryKeySelective(SequenceDef record);

    int updateByPrimaryKey(SequenceDef record);

    int getNextId(SequenceDef record);

    List<SequenceDef> listSequencDef(SequenceDef record);
}
