package com.webank.ai.fate.jmx.mbean;

public class Sample implements SampleMBean {
    @Override
    public void call() {
        System.out.println("do SamlpeMBean call()");
    }
}
