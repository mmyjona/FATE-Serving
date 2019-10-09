package com.webank.ai.fate.serving.interfaces;


import com.webank.ai.fate.core.bean.ReturnResult;
import com.webank.ai.fate.serving.bean.ModelNamespaceData;
import com.webank.ai.fate.serving.core.bean.FederatedParty;
import com.webank.ai.fate.serving.core.bean.FederatedRoles;
import com.webank.ai.fate.serving.core.bean.ModelInfo;
import com.webank.ai.fate.serving.federatedml.PipelineTask;

import java.util.Map;

public interface ModelManager {

    public ReturnResult publishLoadModel(FederatedParty federatedParty, FederatedRoles federatedRoles, Map<String, Map<String, ModelInfo>> federatedRolesModel) ;

    public ReturnResult publishOnlineModel(FederatedParty federatedParty, FederatedRoles federatedRoles, Map<String, Map<String, ModelInfo>> federatedRolesModel);

    public PipelineTask getModel(String name, String namespace);

    public ModelNamespaceData getModelNamespaceData(String namespace);

    public  String getModelNamespaceByPartyId(String partyId);

    public ModelInfo getModelInfoByPartner(String partnerModelName, String partnerModelNamespace);

    public  PipelineTask pushModelIntoPool(String name, String namespace);




}
