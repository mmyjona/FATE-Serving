package com.webank.ai.fate.register.interfaces;


import com.webank.ai.fate.register.url.URL;

public interface Node {


    URL getUrl();

    boolean isAvailable();

    void destroy();

}