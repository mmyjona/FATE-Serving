package com.webank.ai.fate.serving.federatedml.model;

import com.webank.ai.fate.core.bean.ReturnResult;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.Dict;
import com.webank.ai.fate.serving.core.bean.FederatedParams;
import com.webank.ai.fate.core.mlmodel.buffer.OneHotMetaProto.OneHotMeta;
import com.webank.ai.fate.core.mlmodel.buffer.OneHotParamProto.OneHotParam;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneHotEncoder extends BaseModel {
    private static final Logger LOGGER = LogManager.getLogger();

    private List<String> cols;
    @Override
    public int initModel(byte[] protoMeta, byte[] protoParam) {
        LOGGER.info("start init HeteroLR class");
        try {
            OneHotMeta oneHotMeta = this.parseModel(OneHotMeta.parser(), protoMeta);

            this.cols = oneHotMeta.getColsList();
           // this.intercept = lrModelParam.getIntercept();
        } catch (Exception ex) {
            ex.printStackTrace();
           // return StatusCode.ILLEGALDATA;
        }
        //LOGGER.info("Finish init HeteroLR class, model weight is {}", this.weight);
        //return StatusCode.OK;
        return  1;
    }

    @Override
    public Map<String, Object> handlePredict(Context context, List<Map<String, Object>> inputData, FederatedParams predictParams) {
        return null;
    }

}
