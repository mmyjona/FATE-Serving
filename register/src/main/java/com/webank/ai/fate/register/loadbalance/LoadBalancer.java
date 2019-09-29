package com.webank.ai.fate.register.loadbalance;

import com.webank.ai.fate.register.url.URL;

import java.util.List;

public interface LoadBalancer {
        public  List<URL>   select(List<URL> urls);
}
