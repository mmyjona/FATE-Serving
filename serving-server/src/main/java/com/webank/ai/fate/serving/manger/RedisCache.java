package com.webank.ai.fate.serving.manger;

import com.webank.ai.fate.serving.core.bean.Context;
import com.webank.ai.fate.serving.interfaces.Cache;

/**
 * @Description TODO
 * @Author kaideng
 **/
public class RedisCache implements Cache{
    @Override
    public void put(Context context, String key, Object object) {

    }

    @Override
    public <T> T get(Context context, String key, Class<T> dataType) {
        return null;
    }
}
