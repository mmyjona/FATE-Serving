
package com.webank.ai.fate.register.loadbalance;

import com.webank.ai.fate.register.url.CollectionUtils;
import com.webank.ai.fate.register.url.URL;

import java.util.List;

import static com.webank.ai.fate.register.common.Constants.WEIGHT_KEY;


public abstract class AbstractLoadBalancer implements LoadBalancer {

    public  static  int  DEFAULT_WEIGHT =100;
    @Override
    public List<URL> select(List<URL> urls) {
        if (CollectionUtils.isEmpty(urls)) {
            return null;
        }
        if (urls.size() == 1) {
            return urls;
        }



        return doSelect(urls);
    }
    protected abstract List<URL> doSelect(List<URL> url);
    protected int getWeight(URL url) {
        int weight = url.getParameter( WEIGHT_KEY, DEFAULT_WEIGHT);
        return  weight;
    }

}
