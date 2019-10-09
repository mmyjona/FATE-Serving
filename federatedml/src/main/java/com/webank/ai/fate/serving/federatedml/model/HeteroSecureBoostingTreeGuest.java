package com.webank.ai.fate.serving.federatedml.model;

import com.google.common.collect.Maps;
import com.webank.ai.fate.core.bean.ReturnResult;
import com.webank.ai.fate.core.constant.StatusCode;
import com.webank.ai.fate.core.mlmodel.buffer.BoostTreeModelParamProto.BoostingTreeModelParam;
import com.webank.ai.fate.core.mlmodel.buffer.BoostTreeModelParamProto.DecisionTreeModelParam;
import com.webank.ai.fate.core.mlmodel.buffer.BoostTreeModelParamMeta.BoostingTreeModelMeta;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.Dict;
import com.webank.ai.fate.serving.core.bean.FederatedParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;

public  class HeteroSecureBoostingTreeGuest extends HeteroSecureBoost {

    private final String site = "guest";

    private double sigmoid(double x) {
        return 1. / (1. + Math.exp(-x));
    }

    private Map<String, Object> softmax(double weights[]) {
        int n = weights.length;
        double min = weights[0];
        int maxIndex = 0;
        double denominator = 0.0;
        for (int i = 1; i < n; ++i) {
            if (weights[i] > weights[maxIndex]) {
                maxIndex = i;
            }
            min = Math.min(min, weights[i]);
            // denominator += Math.exp(weights[i] - min);
        }

        for (int i = 0; i < n; i++) {
            denominator += Math.exp(weights[i] - min);
        }

        ArrayList<Double> scores = new ArrayList<Double>();
        for (int i = 0; i < n; ++i) {
            scores.add(Math.exp(weights[i] - min) / denominator);
        }

        Map<String, Object> ret= Maps.newHashMap();
        ret.put("label", this.classes.get(maxIndex));
        ret.put("score", scores);

        return ret;
    }

    /*
    Map<String, Double> forward(List<Map<String, Object>> inputDatas) {
        Map<String, Object> inputData = inputDatas.get(0);
        HashMap<Integer, Object> fidValueMapping = new HashMap<Integer, Object>();
        int featureHit = 0;
        for (String key : inputData.keySet()) {
            if (this.featureNameFidMapping.containsKey(key)) {
                fidValueMapping.put(this.featureNameFidMappin.get(key), inputData.get(key));
                ++featureHit;
            }
        }
        LOGGER.info("feature hit rate : {}", 1.0 * featureHit / this.featureNameFidMapping.size());

    }
    */

    private boolean isLocateInLeaf(int treeId, int treeNodeId) {
        return this.trees.get(treeId).getTree(treeNodeId).getIsLeaf();
    }

    private boolean checkLeafAll(int[] treeNodeIds) {
        for (int i = 0; i < this.treeNum; ++i) {
            if (!isLocateInLeaf(i, treeNodeIds[i])) {
                return false;
            }
        }
        return true;
    }

    private double getTreeLeafWeight(int treeId, int treeNodeId) {
        return this.trees.get(treeId).getTree(treeNodeId).getWeight();
    }

    private int traverseTree(int treeId, int treeNodeId, Map<String, Object> input) {
        while (!this.isLocateInLeaf(treeId, treeNodeId) && this.getSite(treeId, treeNodeId).equals(this.site)) {
            treeNodeId = this.gotoNextLevel(treeId, treeNodeId, input);
        }

        return treeNodeId;
    }


    private Map<String, Object> getFinalPredict(double[] weights) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (this.numClasses == 2) {
            double sum = 0;
            for (int i = 0; i < this.treeNum; ++i) {
                sum += weights[i] * this.learningRate;
            }
            ret.put(Dict.SCORE, this.sigmoid(sum));
        } else if (this.numClasses > 2) {
            double[] sumWeights = new double[this.treeDim];
            for (int i = 0; i < this.treeNum; ++i) {
                sumWeights[i % this.treeDim] += weights[i] * this.learningRate;
            }

            for (int i = 0; i < this.treeDim; i++)
                sumWeights[i] += this.initScore.get(i);

            ret= softmax(sumWeights);
        } else {
            double sum = this.initScore.get(0);
            for (int i = 0; i < this.treeNum; ++i) {
                sum += weights[i] * this.learningRate;
            }
            ret.put(Dict.SCORE, sum);
        }

        return ret;
    }

    @Override
    public Map<String, Object> handlePredict(Context context , List<Map<String, Object> > inputData, FederatedParams predictParams) {

        LOGGER.info("HeteroSecureBoostingTreeGuest FederatedParams {}",predictParams);

        Map<String, Object> input = inputData.get(0);
        HashMap<String, Object> fidValueMapping = new HashMap<String, Object>();

        ReturnResult  returnResult = this.getFederatedPredict(context, predictParams,Dict.FEDERATED_INFERENCE,false);

        int featureHit = 0;
        for (String key : input.keySet()) {
            if (this.featureNameFidMapping.containsKey(key)) {
                fidValueMapping.put(this.featureNameFidMapping.get(key).toString(), input.get(key));
                ++featureHit;
            }
        }
        LOGGER.info("feature hit rate : {}", 1.0 * featureHit / this.featureNameFidMapping.size());
        int[] treeNodeIds = new int[this.treeNum];
        double[] weights = new double[this.treeNum];
        int communicationRound = 0;
        while (true) {
            HashMap<String, Object> treeLocation = new HashMap<String, Object>();
            for (int i = 0; i < this.treeNum; ++i) {
                if (this.isLocateInLeaf(i, treeNodeIds[i])) {
                    continue;
                }
                int nodeId = this.traverseTree(i, treeNodeIds[i], fidValueMapping);
                if (!this.isLocateInLeaf(i, nodeId)) {
                    treeLocation.put(String.valueOf(i), nodeId);
                }
            }
            if (treeLocation.size() == 0) {
                break;
            }
            //  String tag = this.generateTag(predictParams.getCaseId(), this.componentName, communicationRound++);

            // predictParams.getData().put(Dict.TAG,tag);

            predictParams.getData().put(Dict.COMPONENT_NAME, this.componentName);

            predictParams.getData().put(Dict.TREE_COMPUTE_ROUND, communicationRound++);

            predictParams.getData().put(Dict.TREE_LOCATION,treeLocation);

            try {
                LOGGER.info("begin to federated");

                ReturnResult  tempResult = this.getFederatedPredict(context, predictParams,Dict.FEDERATED_INFERENCE_FOR_TREE,false);

                Map<String, Object> afterLocation = tempResult.getData();

                LOGGER.info("after loccation is {}", afterLocation);
                for (String location : afterLocation.keySet()) {
                    treeNodeIds[new Integer(location)] = ((Number) afterLocation.get(location)).intValue();
                }

                if (afterLocation == null) {
                    LOGGER.info("receive predict result of host is null");
                    throw new Exception("Null Data");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        for (int i = 0; i < this.treeNum; ++i) {
            weights[i] = getTreeLeafWeight(i, treeNodeIds[i]);
        }

        LOGGER.info("tree leaf ids is {}", treeNodeIds);
        LOGGER.info("weights is {}", weights);

        return getFinalPredict(weights);
    }
}
