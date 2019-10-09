package com.webank.ai.fate.serving.core.bean;


import com.google.common.collect.Maps;
import com.webank.ai.fate.serving.core.bean.Dict;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

/**
 * @Description TODO
 * @Author kaideng
 **/
public class FederatedParams {


//            federatedParams.put(Dict.CASEID, inferenceRequest.getCaseid());
//        federatedParams.put(Dict.SEQNO, inferenceRequest.getSeqno());
//        federatedParams.put("local", modelNamespaceData.getLocal());
//        federatedParams.put("model_info", new ModelInfo(modelName, modelNamespace));
//        federatedParams.put("role", modelNamespaceData.getRole());
//        federatedParams.put("feature_id", featureIds);

    String  caseId;

    String  seqNo;

    FederatedParty local;

    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    ModelInfo  modelInfo;

    FederatedRoles role;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public FederatedParty getLocal() {
        return local;
    }

    public void setLocal(FederatedParty local) {
        this.local = local;
    }



    public FederatedRoles getRole() {
        return role;
    }

    public void setRole(FederatedRoles role) {
        this.role = role;
    }

    public Map<String, Object> getFeatureIdMap() {
        return featureIdMap;
    }

    public void setFeatureIdMap(Map<String, Object> featureIdMap) {
        this.featureIdMap = featureIdMap;
    }

    Map<String, Object>  featureIdMap;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    Map<String,Object>  data = Maps.newHashMap();


    @Override
    public  String toString(){

        return  ToStringBuilder.reflectionToString(this);
    }


}