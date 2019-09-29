package com.webank.ai.fate.register.router;

import com.webank.ai.fate.register.loadbalance.LoadBalanceModel;
import com.webank.ai.fate.register.url.URL;

import java.util.List;

public interface RouterService {


    List<URL> router(URL  url, LoadBalanceModel  loadBalanceModel);

    List<URL> router(URL  url);



}
