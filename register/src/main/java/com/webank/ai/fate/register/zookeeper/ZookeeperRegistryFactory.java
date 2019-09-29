package com.webank.ai.fate.register.zookeeper;


import com.webank.ai.fate.register.common.AbstractRegistryFactory;
import com.webank.ai.fate.register.interfaces.Registry;
import com.webank.ai.fate.register.url.URL;

/**
 * ZookeeperRegistryFactory.
 *
 */
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    private ZookeeperTransporter zookeeperTransporter;


    public void setZookeeperTransporter(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }

    @Override
    public Registry createRegistry(URL url) {
        return new ZookeeperRegistry(url, zookeeperTransporter);
    }

}
