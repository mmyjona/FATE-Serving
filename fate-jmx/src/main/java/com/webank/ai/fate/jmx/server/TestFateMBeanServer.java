package com.webank.ai.fate.jmx.server;

import com.webank.ai.fate.jmx.mbean.Sample;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class TestFateMBeanServer {

    public static void main(String[] args) {
        try {
            FateMBeanServer fateMBeanServer = new FateMBeanServer(ManagementFactory.getPlatformMBeanServer(), false);
            fateMBeanServer.registerMBean(new Sample(), new ObjectName("com.webank.ai.fate.jmx:name=sample,type=standard"));

            // use default domain
//            FateMBeanServer fateMBeanServer = new FateMBeanServer(MBeanServerFactory.createMBeanServer("com.webank.ai.fate.jmx"), false);
//            fateMBeanServer.registerMBean(new Sample(), new ObjectName(":name=sample,type=standard"));
            fateMBeanServer.openJMXServer("fate");

            // service:jmx:rmi:///jndi/rmi://10.56.224.80:9999/fate
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
