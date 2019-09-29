package com.webank.ai.fate.serving.host;

import com.webank.ai.fate.core.bean.ReturnResult;
import com.webank.ai.fate.serving.bean.InferenceRequest;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.HostFederatedParams;
import com.webank.ai.fate.serving.core.bean.InferenceActionType;

import java.util.Map;


public interface HostInferenceProvider {

    public ReturnResult federatedInference(Context context,HostFederatedParams federatedParams);

    public ReturnResult federatedInferenceForTree(Context  context,HostFederatedParams  federatedParams);



}
