
package com.webank.ai.fate.register.zookeeper;


import com.webank.ai.fate.register.common.ChildListener;
import com.webank.ai.fate.register.common.DataListener;
import com.webank.ai.fate.register.common.StateListener;
import com.webank.ai.fate.register.url.URL;

import java.util.List;
import java.util.concurrent.Executor;

public interface ZookeeperClient {

    void create(String path, boolean ephemeral);

    void delete(String path);

    List<String> getChildren(String path);

    List<String> addChildListener(String path, ChildListener listener);


    void addDataListener(String path, DataListener listener);


    void addDataListener(String path, DataListener listener, Executor executor);

    void removeDataListener(String path, DataListener listener);

    void removeChildListener(String path, ChildListener listener);

    void addStateListener(StateListener listener);

    void removeStateListener(StateListener listener);

    boolean isConnected();

    void close();

    URL getUrl();

    void create(String path, String content, boolean ephemeral);

    String getContent(String path);

}
