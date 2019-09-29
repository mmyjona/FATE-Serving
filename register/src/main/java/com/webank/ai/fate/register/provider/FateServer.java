package com.webank.ai.fate.register.provider;

import com.google.common.collect.Maps;
import com.webank.ai.fate.register.annotions.RegisterService;
import com.webank.ai.fate.register.common.*;
import com.webank.ai.fate.register.interfaces.Registry;
import com.webank.ai.fate.register.url.URL;
import com.webank.ai.fate.register.utils.NetUtils;
import com.webank.ai.fate.register.utils.StringUtils;
import com.webank.ai.fate.register.zookeeper.ZookeeperRegistry;
import io.grpc.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public  class FateServer extends Server {


    public   String project;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    private    String environment;


    private static final Logger logger = LogManager.getLogger(FateServer.class);

    private  Registry registry;

    public static  Set <RegisterService>  serviceSets = new HashSet<>();

        Server server;


        public  FateServer(){
        }
        public  FateServer(Server server  ){
            this();

            this.server = server;

        }

        @Override
        public Server start() throws IOException {
             this.server.start();
            // register();
             return  this;
        }

        @Override
        public Server shutdown() {
            logger.info("grpc server prepare shutdown");
//            registry.destroy();
            this.server.shutdown();
            logger.info("grpc server shutdown!!!!!!!");
            return this;
        }

        @Override
        public Server shutdownNow() {
            this.server.shutdownNow();
            return  this;
        }

        @Override
        public boolean isShutdown() {
            return    this.server.isShutdown();

        }

        @Override
        public boolean isTerminated() {
            return this.server.isTerminated();
        }

        @Override
        public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
           return  this.server.awaitTermination(l,timeUnit);
        }

        @Override
        public void awaitTermination() throws InterruptedException {
              this.server.awaitTermination();
        }



//        public  static  void  main(String[]  args){
//
//            FateServer fateServer = new FateServer();
//
//            FateServer.serviceRegister.put("test1","");
//            FateServer.serviceRegister.put("test2","");
//
//            FateServer.register();
//            FateServer.lookup("test2");
//
//            List<URL>  cacheUrls =  ((AbstractRegistry)registry).getCacheUrls(URL.valueOf("/test1"));
//
//            System.err.println("cacheUrls==================="+cacheUrls);
//
//
//            while(true){
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
    }