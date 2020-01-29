package com.yepit.mapp.crp.mapper.system;


import com.yepit.mapp.crp.domain.system.BatchOperationLog;
import com.yepit.mapp.crp.dto.system.SearchBatchLogRequest;

import java.util.List;

public interface BatchOperationLogMapper {
    int deleteByPrimaryKey(String batchId);

    int insert(BatchOperationLog record);

    int insertSelective(BatchOperationLog record);

    BatchOperationLog selectByPrimaryKey(String batchId);

    int updateByPrimaryKeySelective(BatchOperationLog record);

    int updateByPrimaryKey(BatchOperationLog record);

    public List<BatchOperationLog> listByCond(SearchBatchLogRequest request);
}