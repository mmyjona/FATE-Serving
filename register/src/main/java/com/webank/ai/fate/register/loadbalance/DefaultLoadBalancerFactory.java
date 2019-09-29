package com.webank.ai.fate.register.loadbalance;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultLoadBalancerFactory implements  LoadBalancerFactory {

    static ConcurrentMap<LoadBalanceModel,LoadBalancer>   loaderBalancerRegister  ;


    static {
        loaderBalancerRegister= new ConcurrentHashMap();
        loaderBalancerRegister.put(LoadBalanceModel.random_with_weight,new RandomLoadBalance());
        loaderBalancerRegister.put(LoadBalanceModel.random,new RandomLoadBalance());
    }

    @Override
    public LoadBalancer getLoaderBalancer(LoadBalanceModel model) {

        return loaderBalancerRegister.get(model);
    }
}
