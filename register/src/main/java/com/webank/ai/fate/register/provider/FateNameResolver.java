package com.webank.ai.fate.register.provider;

import io.grpc.NameResolver;

/**
 * @Description TODO
 * @Author kaideng
 **/
public class FateNameResolver extends NameResolver {
    @Override
    public String getServiceAuthority() {
        return null;
    }

    @Override
    public void start(Listener listener) {

    }

    @Override
    public void shutdown() {

    }
}
