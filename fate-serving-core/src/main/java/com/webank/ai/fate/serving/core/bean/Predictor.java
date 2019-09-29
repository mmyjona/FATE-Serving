package com.webank.ai.fate.serving.core.bean;

import java.util.List;
import java.util.Map;

public interface Predictor <Req_I,Req_P,Res>{

    public Res predict(Context context , Req_I inputData, Req_P predictParams) ;

    public  void preprocess(Context  context,Req_I inputData, Req_P predictParams);

    public Res  postprocess(Context  context,Req_I inputData, Req_P predictParams,Res  result);
}
