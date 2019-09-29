package com.webank.ai.fate.serving.guest;

import com.webank.ai.fate.core.bean.ReturnResult;
import com.webank.ai.fate.serving.bean.InferenceRequest;
import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.core.bean.InferenceActionType;


public interface GuestInferenceProvider {

    public ReturnResult syncInference(Context context, InferenceRequest inferenceRequest);

    public ReturnResult asynInference(Context context, InferenceRequest inferenceRequest);

    public ReturnResult getResult(Context context, InferenceRequest inferenceRequest);
}
