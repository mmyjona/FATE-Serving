package com.webank.ai.fate.serving.federatedml.model;

import com.webank.ai.fate.core.constant.StatusCode;
import com.webank.ai.fate.core.mlmodel.buffer.FeatureBinningMetaProto.FeatureBinningMeta;
import com.webank.ai.fate.core.mlmodel.buffer.FeatureBinningMetaProto.TransformMeta;
import com.webank.ai.fate.core.mlmodel.buffer.FeatureBinningParamProto.FeatureBinningParam;
import com.webank.ai.fate.core.mlmodel.buffer.FeatureBinningParamProto.FeatureBinningResult;
import com.webank.ai.fate.core.mlmodel.buffer.FeatureBinningParamProto.IVParam;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.FederatedParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HeteroFeatureBinning extends BaseModel {
    private FeatureBinningParam featureBinningParam;
	private FeatureBinningMeta featureBinningMeta;
    private Map<String, List> splitPoints;
    private List<Long> transformCols;
    private List<String> header;
	private boolean needRun;
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public int initModel(byte[] protoMeta, byte[] protoParam) {
        LOGGER.info("start init Feature Binning class");
		this.needRun = false;
		this.splitPoints = new HashMap<>();

        try {
			this.featureBinningMeta = this.parseModel(FeatureBinningMeta.parser(), protoMeta);
			this.needRun = this.featureBinningMeta.getNeedRun();
			TransformMeta transformMeta = this.featureBinningMeta.getTransformParam();
			this.transformCols = transformMeta.getTransformColsList();

            this.featureBinningParam = this.parseModel(FeatureBinningParam.parser(), protoParam);
            this.header = this.featureBinningParam.getHeaderList();
            FeatureBinningResult featureBinningResult = this.featureBinningParam.getBinningResult();
            Map<String, IVParam> binningResult = featureBinningResult.getBinningResultMap();
            for (String key: binningResult.keySet()) {
                IVParam oneColResult = binningResult.get(key);
                List<Double> splitPoints = oneColResult.getSplitPointsList();
                this.splitPoints.put(key, splitPoints);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return StatusCode.ILLEGALDATA;
        }
        LOGGER.info("Finish init Feature Binning class");
        return StatusCode.OK;
    }

    @Override
    public Map<String, Object> handlePredict(Context context , List<Map<String, Object> > inputData, FederatedParams predictParams) {
        LOGGER.info("Start Feature Binning predict");
        HashMap<String, Object> outputData = new HashMap<>();
        Map<String, Object> firstData = inputData.get(0);
		if (!this.needRun) {
			return firstData;
		}

        for (String colName : firstData.keySet()) {
            List<Double> splitPoint = this.splitPoints.get(colName);
            Double colValue = Double.valueOf(firstData.get(colName).toString());
            int colIndex;
            for (colIndex = 0; colIndex < splitPoint.size(); colIndex ++) {
                if (colValue <= splitPoint.get(colIndex)) {
                    break;
                }
            }
            outputData.put(colName, colIndex);
        }
        
        return outputData;
    }

}
