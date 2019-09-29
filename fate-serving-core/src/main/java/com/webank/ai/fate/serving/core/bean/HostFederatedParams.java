package com.webank.ai.fate.serving.core.bean;

import com.webank.ai.fate.api.mlmodel.manager.ModelServiceProto;

import com.webank.ai.fate.core.utils.ObjectTransform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author kaideng
 **/
public class HostFederatedParams extends FederatedParams{

//    Map<String, Object> requestData = new HashMap<>();
//        Arrays.asList("caseid", "seqno").forEach((field -> {
//        requestData.put(field, federatedParams.get(field));
//    }));
//        requestData.put("partner_local", ObjectTransform.bean2Json(srcParty));
//        requestData.put("partner_model_info", ObjectTransform.bean2Json(federatedParams.get("model_info")));
//        requestData.put("feature_id", ObjectTransform.bean2Json(federatedParams.get("feature_id")));
//        requestData.put("local", ObjectTransform.bean2Json(dstParty));
//        requestData.put("role", ObjectTransform.bean2Json(federatedParams.get("role")));


//    private  String caseId;
//    private  String seqNo;
    protected  FederatedParty partnerLocal;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    protected  String  methodName;
 //   private  FederatedParty local;

    public ModelInfo getPartnerModelInfo() {
        return partnerModelInfo;
    }

    public void setPartnerModelInfo(ModelInfo partnerModelInfo) {
        this.partnerModelInfo = partnerModelInfo;
    }

    public FederatedParty getPartnerLocal() {
        return partnerLocal;
    }

    public void setPartnerLocal(FederatedParty partnerLocal) {
        this.partnerLocal = partnerLocal;
    }

    protected   ModelInfo partnerModelInfo;



    public HostFederatedParams(){

    }





    public HostFederatedParams(String caseId, String seqNo, FederatedParty partnerLocal, FederatedParty local, FederatedRoles role, Map<String, Object> featureIdMap) {
        this.caseId = caseId;
        this.seqNo = seqNo;
        this.partnerLocal = partnerLocal;
        this.local = local;
        this.role = role;
        this.featureIdMap = featureIdMap;
    }



}
