package com.webank.ai.fate.serving.federatedml.model;

import com.webank.ai.fate.core.constant.StatusCode;
import com.webank.ai.fate.core.mlmodel.buffer.ScaleMetaProto.ScaleMeta;
import com.webank.ai.fate.core.mlmodel.buffer.ScaleParamProto.ScaleParam;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.Dict;
import com.webank.ai.fate.serving.core.bean.FederatedParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class Scale extends BaseModel {
    private ScaleMeta scaleMeta;
    private ScaleParam scaleParam;
    private boolean need_run;
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public int initModel(byte[] protoMeta, byte[] protoParam) {
        LOGGER.info("start init Scale class");
        try {
            this.scaleMeta = this.parseModel(ScaleMeta.parser(), protoMeta);
            this.scaleParam = this.parseModel(ScaleParam.parser(), protoParam);
            this.need_run = scaleParam.getNeedRun();
        } catch (Exception ex) {
            ex.printStackTrace();
            return StatusCode.ILLEGALDATA;
        }
        LOGGER.info("Finish init Scale class");
        return StatusCode.OK;
    }

    @Override
    public Map<String, Object> handlePredict(Context context, List<Map<String, Object>> inputDatas, FederatedParams predictParams) {
        Map<String, Object> outputData = inputDatas.get(0);
        if (this.need_run) {
            String scaleMethod = this.scaleMeta.getMethod();
            if (scaleMethod.toLowerCase().equals(Dict.MIN_MAX_SCALE)) {
                MinMaxScale minMaxScale = new MinMaxScale();
                outputData = minMaxScale.transform(inputDatas.get(0), this.scaleParam.getColScaleParamMap());
            } else if (scaleMethod.toLowerCase().equals(Dict.STANDARD_SCALE)) {
                StandardScale standardScale = new StandardScale();
                outputData = standardScale.transform(inputDatas.get(0), this.scaleParam.getColScaleParamMap());
            }
        }
        return outputData;
    }
}
