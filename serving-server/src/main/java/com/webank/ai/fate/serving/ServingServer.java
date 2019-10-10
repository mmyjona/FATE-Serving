/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.ai.fate.serving;

import com.google.common.collect.Sets;
import com.webank.ai.fate.core.network.grpc.client.GrpcClientPool;
import com.webank.ai.fate.core.utils.Configuration;
import com.webank.ai.fate.jmx.server.FateMBeanServer;
import com.webank.ai.fate.register.provider.FateServer;
import com.webank.ai.fate.register.provider.FateServerBuilder;
import com.webank.ai.fate.register.router.RouterService;
import com.webank.ai.fate.register.url.URL;
import com.webank.ai.fate.register.zookeeper.ZookeeperRegistry;
import com.webank.ai.fate.serving.core.bean.ApplicationHolder;
import com.webank.ai.fate.serving.core.bean.Dict;
import com.webank.ai.fate.serving.federatedml.model.BaseModel;
import com.webank.ai.fate.serving.manger.InferenceWorkerManager;
import com.webank.ai.fate.serving.service.*;
import com.webank.ai.fate.serving.utils.HttpClientPool;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServingServer implements InitializingBean{
    private static final Logger LOGGER = LogManager.getLogger();
    private Server server;
    private boolean  useRegister= false;


    private String confPath="";
    static ApplicationContext applicationContext;
    public  ServingServer(){

       // System.err.println("==============================");

    }

    public ServingServer(String confPath) {
        this.confPath = new File(confPath).getAbsolutePath();

        System.setProperty("configpath",confPath);
        new Configuration(confPath).load();
    }

    private void start(String[] args) throws IOException {
        this.initialize();
        applicationContext = SpringApplication.run(SpringConfig.class, args);


        ApplicationHolder.applicationContext=  applicationContext;
        int port = Integer.parseInt(Configuration.getProperty(Dict.PROPERTY_SERVER_PORT));
        //TODO: Server custom configuration
        Executor    executor= Executors.newCachedThreadPool();
        FateServerBuilder serverBuilder =(FateServerBuilder)ServerBuilder.forPort(port);
        serverBuilder.executor(executor);
        //new ServiceOverloadProtectionHandle()
        serverBuilder.addService(ServerInterceptors.intercept(applicationContext.getBean(InferenceService.class), new ServiceExceptionHandler(),new ServiceOverloadProtectionHandle() ),InferenceService.class);
        serverBuilder.addService(ServerInterceptors.intercept(applicationContext.getBean(ModelService.class), new ServiceExceptionHandler(),new ServiceOverloadProtectionHandle()),ModelService.class);
        serverBuilder.addService(ServerInterceptors.intercept(applicationContext.getBean(ProxyService.class), new ServiceExceptionHandler(),new ServiceOverloadProtectionHandle()),ProxyService.class);
        server = serverBuilder.build();

        LOGGER.info("Server started listening on port: {}, use configuration: {}", port, this.confPath);

        server.start();

        String  userRegisterString = Configuration.getProperty(Dict.USE_REGISTER);
        useRegister = Boolean.valueOf(userRegisterString);
        if(useRegister) {

            ZookeeperRegistry zookeeperRegistry = applicationContext.getBean(ZookeeperRegistry.class);
            zookeeperRegistry.subProject(Dict.PROPERTY_PROXY_ADDRESS);
            //zookeeperRegistry.addDynamicEnvironment("10001");
            BaseModel.routerService =  applicationContext.getBean(RouterService.class);
            FateServer.serviceSets.forEach(servie->{
                try {
                    String serviceName = servie.serviceName();
                    String weightKey = serviceName + ".weight";
                    HashMap properties = Configuration.getProperties();
                    if (properties.get(weightKey) != null) {
                        int weight = Integer.valueOf(properties.get(weightKey).toString());
                        if (weight > 0) {
                            zookeeperRegistry.getServieWeightMap().put(weightKey, weight);
                        }
                    }
                }catch(Throwable e){
                    LOGGER.error("parse interface weight error",e);
                }

            });
            zookeeperRegistry.register(FateServer.serviceSets);

            boolean useJMX = Boolean.valueOf(Configuration.getProperty(Dict.USE_JMX));
            if (useJMX) {
                String jmxServerName = Configuration.getProperty(Dict.JMX_SERVER_NAME, "serving");
                FateMBeanServer fateMBeanServer = new FateMBeanServer(ManagementFactory.getPlatformMBeanServer(), true);
                String jmxServerUrl = fateMBeanServer.openJMXServer(jmxServerName);
                // service:jmx:rmi:///jndi/rmi://10.56.224.80:9999/serving
//            /FATE-SERVICES/jmx/providers/service:jmx:rmi:///jndi/rmi://10.56.224.80:9999/serving
                URL jmxUrl = URL.parseJMXServiceUrl(jmxServerUrl);
                zookeeperRegistry.register(jmxUrl);
            }
        }


        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("*** shutting down gRPC server since JVM is shutting down");
                ServingServer.this.stop();
                LOGGER.info("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            if (useRegister) {
                ZookeeperRegistry zookeeperRegistry = applicationContext.getBean(ZookeeperRegistry.class);
                Set<URL> registered = zookeeperRegistry.getRegistered();
                Set<URL> urls = Sets.newHashSet();
                urls.addAll(registered);
                urls.forEach(url -> {
                    LOGGER.info("unregister {}", url);
                    zookeeperRegistry.unregister(url);
                });

                zookeeperRegistry.destroy();
            }
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void initialize() {
        this.initializeClientPool();
        HttpClientPool.initPool();
        InferenceWorkerManager.prestartAllCoreThreads();
    }

    private void initializeClientPool() {
        ArrayList<String> serverAddress = new ArrayList<>();
        serverAddress.add(Configuration.getProperty(Dict.PROPERTY_PROXY_ADDRESS));
        serverAddress.add(Configuration.getProperty(Dict.PROPERTY_ROLL_ADDRESS));
        new Thread(new Runnable() {
            @Override
            public void run() {
                GrpcClientPool.initPool(serverAddress);
            }
        }).start();
        LOGGER.info("Finish init client pool");
    }

    public static void main(String[] args) {
        try {


            Options options = new Options();
            Option option = Option.builder("c")
                    .longOpt("config")
                    .argName("file")
                    .required()
                    .hasArg()
                    .numberOfArgs(1)
                    .desc("configuration file")
                    .build();
            options.addOption(option);
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            ServingServer a = new ServingServer(cmd.getOptionValue("c"));

            a.start(args);


            //a.blockUntilShutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
