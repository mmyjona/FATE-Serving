package com.webank.ai.fate.register.common;

import com.webank.ai.fate.register.url.URL;
import com.webank.ai.fate.register.zookeeper.ZookeeperRegistry;
import com.webank.ai.fate.register.zookeeper.ZookeeperRegistryFactory;

/**
 * @Description TODO
 * @Author kaideng
 **/
public class Test {


    public  static  void main(String[]  args){

        URL registryUrl = URL.valueOf("zookeeper://localhost:" + 2181);

         URL serviceUrl = URL.valueOf("zookeeper://zookeeper/" + "mytest" + "?notify=false&methods=test1,test2");

        ZookeeperRegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory();
       zookeeperRegistryFactory.setZookeeperTransporter(new CuratorZookeeperTransporter());
       ZookeeperRegistry zookeeperRegistry = (ZookeeperRegistry) zookeeperRegistryFactory.createRegistry(registryUrl);

       //  zookeeperRegistry.register(serviceUrl);

    }
}
