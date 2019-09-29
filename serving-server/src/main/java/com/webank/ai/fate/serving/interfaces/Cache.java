package com.webank.ai.fate.serving.interfaces;

import com.webank.ai.fate.serving.core.bean.Context;

public interface Cache {

    public  void put(Context context, String  key, Object  object);

    public  <T> T get(Context  context,String  key, Class<T>  dataType);

}
