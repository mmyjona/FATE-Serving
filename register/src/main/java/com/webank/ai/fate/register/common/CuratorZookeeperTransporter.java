package com.webank.ai.fate.register.common;


import com.webank.ai.fate.register.url.URL;
import com.webank.ai.fate.register.zookeeper.ZookeeperClient;


public class CuratorZookeeperTransporter extends AbstractZookeeperTransporter {
    @Override
    public ZookeeperClient createZookeeperClient(URL url) {
        return new CuratorZookeeperClient(url);
    }


}
