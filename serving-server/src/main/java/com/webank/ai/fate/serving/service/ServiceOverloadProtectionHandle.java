package com.webank.ai.fate.serving.service;

import com.webank.ai.fate.serving.core.bean.Dict;
import com.webank.ai.fate.serving.core.monitor.WatchDog;
import io.grpc.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceOverloadProtectionHandle implements ServerInterceptor {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        Metadata.Key<String> serviceNameKey = Metadata.Key.of(Dict.SERVICE_NAME, Metadata.ASCII_STRING_MARSHALLER);

       // String serviceName = metadata.get(serviceNameKey);
        String serviceName = "TestServiceName";
        if (StringUtils.isBlank(serviceName)) {
            serverCall.close(Status.DATA_LOSS, metadata);
        }

        ServerCall.Listener<ReqT> delegate = serverCallHandler.startCall(serverCall, metadata);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {

            @Override
            public void onHalfClose() {
                try {
                    WatchDog.enter(serviceName);
                    super.onHalfClose();
                } catch (Exception e) {
                    LOGGER.info("ServiceException:", e);
                    serverCall.close(Status.CANCELLED.withCause(e).withDescription(e.getMessage()), metadata);
                }
            }

            @Override
            public void onCancel() {
                WatchDog.quit(serviceName);
                super.onCancel();
            }

            @Override
            public void onComplete() {
                WatchDog.complete(serviceName);
                super.onComplete();
//                WatchDog.getCount();
            }
        };
    }
}
