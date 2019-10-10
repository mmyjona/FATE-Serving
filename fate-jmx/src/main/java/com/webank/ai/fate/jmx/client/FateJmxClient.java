package com.webank.ai.fate.jmx.client;

import com.webank.ai.fate.jmx.mbean.SampleMBean;
import com.webank.ai.fate.jmx.util.IpAddressUtil;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Set;

public class FateJmxClient {
    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + IpAddressUtil.getInnetIp() + ":9999/fate");
        JMXConnector connector = JMXConnectorFactory.connect(url, null);

        MBeanServerConnection mbsc = connector.getMBeanServerConnection();

        for (String domain : mbsc.getDomains()) {
            System.out.println("domain = " + domain);
        }

        System.out.println("MBeanServer default domain = " + mbsc.getDefaultDomain());

        Set<ObjectName> objectNames = mbsc.queryNames(null, null);
        for (ObjectName objectName : objectNames) {
            System.out.println("objectName = " + objectName);
        }

        SampleMBean proxy = JMX.newMBeanProxy(mbsc, new ObjectName("com.webank.ai.fate.jmx:name=sample,type=standard"), SampleMBean.class, true);
        proxy.call();
    }
}
