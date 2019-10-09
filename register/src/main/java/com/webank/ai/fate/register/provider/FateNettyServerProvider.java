package com.webank.ai.fate.register.provider;

import io.grpc.ServerBuilder;
import io.grpc.ServerProvider;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;


public class FateNettyServerProvider  extends ServerProvider {


    public  FateNettyServerProvider(){

    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 10;
    }

    @Override
    protected ServerBuilder<?> builderForPort(int port) {

        ServerBuilder<?> serverBuilder = NettyServerBuilder.forPort(port);

        FateServerBuilder fateServerBuilder = new FateServerBuilder(serverBuilder);

        return  fateServerBuilder;
    }
}