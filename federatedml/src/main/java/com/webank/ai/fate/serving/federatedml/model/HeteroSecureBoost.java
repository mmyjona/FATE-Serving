package com.webank.ai.fate.serving.federatedml.model;

import com.google.common.collect.Maps;
import com.webank.ai.fate.core.constant.StatusCode;
import com.webank.ai.fate.core.mlmodel.buffer.BoostTreeModelParamProto;
import com.webank.ai.fate.core.mlmodel.buffer.BoostTreeModelParamProto.BoostingTreeModelParam;
import com.webank.ai.fate.core.mlmodel.buffer.BoostTreeModelParamProto.DecisionTreeModelParam;
import com.webank.ai.fate.core.mlmodel.buffer.BoostTreeModelParamMeta.BoostingTreeModelMeta;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.FederatedParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class HeteroSecureBoost extends BaseModel {
    protected List<Map<Integer, Double> > split_maskdict;
    protected Map<String,Integer> featureNameFidMapping = Maps.newHashMap();
    public static final Logger LOGGER = LogManager.getLogger();
    protected int treeNum;
    protected List<Double> initScore;
    protected List<DecisionTreeModelParam> trees;
    protected int numClasses;
    protected List<String> classes;
    protected int treeDim;
    protected  double learningRate;

    @Override
    public int initModel(byte[] protoMeta, byte[] protoParam) {
        LOGGER.info("start init HeteroLR class");
        try {

            BoostingTreeModelParam param = this.parseModel(BoostingTreeModelParam.parser(), protoParam);
            BoostingTreeModelMeta meta = this.parseModel(BoostingTreeModelMeta.parser(), protoMeta);

            java.util.Map<java.lang.Integer, java.lang.String>   featureNameMapping =param.getFeatureNameFidMapping();

            featureNameMapping.forEach((k,v)->{

                featureNameFidMapping.put(v,k);


            });


            this.treeNum = param.getTreeNum();
            this.initScore = param.getInitScoreList();
            this.trees = param.getTreesList();
            this.numClasses = param.getNumClasses();
            this.classes = param.getClassesList();
            this.treeDim = param.getTreeDim();
            this.learningRate = meta.getLearningRate();

        } catch (Exception ex) {
            ex.printStackTrace();
            return StatusCode.ILLEGALDATA;
        }
        LOGGER.info("Finish init HeteroSecureBoost class");
        return StatusCode.OK;
    }

    protected String getSite(int treeId, int treeNodeId) {
        return this.trees.get(treeId).getTree(treeNodeId).getSitename().split(":", -1)[0];
    }

    protected String generateTag(String caseId, String modelId, int communicationRound) {
        return caseId + "_" + modelId + "_" + String.valueOf(communicationRound);
    }

    protected String[] parseTag(String  tag){

        return   tag.split("_");
    }

    protected int gotoNextLevel(int treeId, int treeNodeId, Map<String, Object> input) {
        int nextTreeNodeId;
        int fid = this.trees.get(treeId).getTree(treeNodeId).getFid();
        double splitValue = this.trees.get(treeId).getSplitMaskdict().get(treeNodeId);
        String fidStr = String.valueOf(fid);
        if (input.containsKey(fidStr)) {
            if (Double.parseDouble(input.get(fidStr).toString()) < splitValue) {
                nextTreeNodeId = this.trees.get(treeId).getTree(treeNodeId).getLeftNodeid();
            } else {
                nextTreeNodeId =  this.trees.get(treeId).getTree(treeNodeId).getRightNodeid();
            }
        } else {
            if (this.trees.get(treeId).getMissingDirMaskdict().containsKey(treeNodeId)) {
                int missingDir = this.trees.get(treeId).getMissingDirMaskdict().get(treeNodeId);
                if (missingDir == 1) {
                    nextTreeNodeId = this.trees.get(treeId).getTree(treeNodeId).getRightNodeid();
                } else {
                    nextTreeNodeId = this.trees.get(treeId).getTree(treeNodeId).getLeftNodeid();
                }
            } else {
                nextTreeNodeId = this.trees.get(treeId).getTree(treeNodeId).getRightNodeid();
            }
        }

        return nextTreeNodeId;

    }

    @Override
    public abstract Map<String, Object> handlePredict(Context context , List<Map<String, Object> > inputData, FederatedParams predictParams);





}

