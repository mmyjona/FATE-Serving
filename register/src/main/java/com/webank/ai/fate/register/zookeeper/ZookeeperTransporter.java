
package com.webank.ai.fate.register.zookeeper;


import com.webank.ai.fate.register.url.URL;


public interface ZookeeperTransporter {

    ZookeeperClient connect(URL url);

}


