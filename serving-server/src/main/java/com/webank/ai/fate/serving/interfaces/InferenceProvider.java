package com.webank.ai.fate.serving.interfaces;

import com.webank.ai.fate.core.bean.ReturnResult;
import com.webank.ai.fate.serving.bean.InferenceRequest;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.InferenceActionType;

import java.util.Map;

public interface InferenceProvider {


    public ReturnResult federatedInference(Context context,Map<String, Object> federatedParams);
}
