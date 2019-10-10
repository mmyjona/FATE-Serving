package com.webank.ai.fate.jmx.server;

import com.webank.ai.fate.jmx.mbean.Sample;
import com.webank.ai.fate.jmx.util.IpAddressUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class FateMBeanServer {

    private static final Logger LOGGER = LogManager.getLogger(FateMBeanServer.class);

    private MBeanServer mBeanServer;

    private JMXConnectorServer jmxConnectorServer;

    /**
     * FateMBeanServer Constructor
     * @param mBeanServer
     * @param initMBeans  auto register define mbeans
     */
    public FateMBeanServer(MBeanServer mBeanServer, boolean initMBeans) {
        this.mBeanServer = mBeanServer;
        try {
            if (initMBeans) {
                init();
            }
        } catch (Exception e) {
            LOGGER.error("Fate MBean server init fail");
            e.printStackTrace();
        }
    }

    private void init() throws MalformedObjectNameException, MBeanRegistrationException, InstanceAlreadyExistsException, NotCompliantMBeanException {
        LOGGER.info("Init register all MBeans");

        // register any mbeans
        registerMBean(new Sample(), new ObjectName("com.webank.ai.fate.jmx:name=sample"));
        // ...
    }

    public void registerMBean(Object object, ObjectName objectName)
            throws NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        this.mBeanServer.registerMBean(object, objectName);

        LOGGER.info("Server registerMBean {}", objectName);
    }

    public String openJMXServer(String serverName) throws IOException {
        int port = Integer.valueOf(System.getProperty("jmx.port", "9999"));
        LocateRegistry.createRegistry(port);
        String url = "service:jmx:rmi:///jndi/rmi://" + IpAddressUtil.getInnetIp() + ":" + port + "/" + serverName;
        JMXServiceURL jmxServiceURL = new JMXServiceURL(url);
        jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mBeanServer);
        jmxConnectorServer.start();

        LOGGER.info("JMX Server started listening on port: {}, url: {}", port, url);
        return url;
    }

    public void stopJMXServer() throws IOException {

        if (jmxConnectorServer != null) {
            jmxConnectorServer.stop();
        }
    }

}
