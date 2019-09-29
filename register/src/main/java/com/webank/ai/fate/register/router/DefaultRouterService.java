package com.webank.ai.fate.register.router;

import com.webank.ai.fate.register.common.Constants;
import com.webank.ai.fate.register.loadbalance.LoadBalanceModel;
import com.webank.ai.fate.register.url.CollectionUtils;
import com.webank.ai.fate.register.url.URL;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class DefaultRouterService  extends  AbstractRouterService {
    @Override
    public List<URL> doRouter(URL url, LoadBalanceModel  loadBalanceModel) {

        List<URL>   urls =  registry.getCacheUrls(url);

        String  version= url.getParameter(Constants.VERSION_KEY);
        if(CollectionUtils.isNotEmpty(urls) && StringUtils.isNotBlank(version)) {
           urls= filterVersion(urls,version);
        }
//        else{
//            AtomicReference<List<URL>>  resultUrls = new AtomicReference<>();
//            registry.subscribe(url  , resultUrls::set);
//            urls =resultUrls.get();
//            urls= filterVersion(urls,version);
//        }

        if(CollectionUtils.isEmpty(urls)) {
            return null;
        }

        List<URL>  resultUrls = this.loadBalancer.select(urls);

        return  resultUrls;


    }



}



