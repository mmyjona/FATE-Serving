package com.webank.ai.fate.register.loadbalance;

public interface LoadBalancerFactory  {
    LoadBalancer   getLoaderBalancer(LoadBalanceModel  model);
}
