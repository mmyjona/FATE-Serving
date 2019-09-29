package com.webank.ai.fate.serving.core.bean;


import com.webank.ai.fate.core.bean.ReturnResult;
import com.webank.ai.fate.serving.core.bean.Context;
import org.springframework.context.ApplicationContext;


import java.util.Map;

public interface CacheManager {


    static    CacheManager  getInstance(){
        return (CacheManager) ApplicationHolder.applicationContext.getBean(CacheManager.class);
    };



    public  void store(Context  context,String  key,  Object  object);

    public  <T> T restore(Context  context,String  key, Class<T>  dataType);

    public  void putInferenceResultCache(Context context , String partyId, String caseid, ReturnResult returnResult) ;
//    {
//
//        long  beginTime =System.currentTimeMillis();
//        try {
//
//            String inferenceResultCacheKey = generateInferenceResultCacheKey(partyId, caseid);
//            boolean putCacheSuccess = putIntoCache(inferenceResultCacheKey, CacheType.INFERENCE_RESULT, returnResult);
//            if (putCacheSuccess) {
//                LOGGER.info("Put {} inference result into cache", inferenceResultCacheKey);
//            }
//        }finally {
//            long  end =System.currentTimeMillis();
//            LOGGER.info("caseid {} putInferenceResultCache cost {}",context.getCaseId(),end - beginTime);
//
//        }
//    }

    public  ReturnResult getInferenceResultCache(String partyId, String caseid) ;
//    {
//        String inferenceResultCacheKey = generateInferenceResultCacheKey(partyId, caseid);
//        ReturnResult returnResult = getFromCache(inferenceResultCacheKey, CacheType.INFERENCE_RESULT);
//        if (returnResult != null) {
//            LOGGER.info("Get {} inference result from cache.", inferenceResultCacheKey);
//        }
//        return returnResult;
//    }

    public  void putRemoteModelInferenceResult(FederatedParty remoteParty, FederatedRoles federatedRoles, Map<String, Object> featureIds, ReturnResult returnResult);
//    {
//        if (! Boolean.parseBoolean(Configuration.getProperty("remoteModelInferenceResultCacheSwitch"))){
//            return;
//        }
//        String remoteModelInferenceResultCacheKey = generateRemoteModelInferenceResultCacheKey(remoteParty, federatedRoles, featureIds);
//        boolean putCacheSuccess = putIntoCache(remoteModelInferenceResultCacheKey, CacheType.REMOTE_MODEL_INFERENCE_RESULT, returnResult);
//        if (putCacheSuccess) {
//            LOGGER.info("Put {} remote model inference result into cache.", remoteModelInferenceResultCacheKey);
//        }
//    }

    public  ReturnResult getRemoteModelInferenceResult(FederatedParty remoteParty, FederatedRoles federatedRoles, Map<String, Object> featureIds);
//    {
//        if (! Boolean.parseBoolean(Configuration.getProperty("remoteModelInferenceResultCacheSwitch"))){
//            return null;
//        }
//        String remoteModelInferenceResultCacheKey = generateRemoteModelInferenceResultCacheKey(remoteParty, federatedRoles, featureIds);
//        ReturnResult returnResult = getFromCache(remoteModelInferenceResultCacheKey, CacheType.REMOTE_MODEL_INFERENCE_RESULT);
//        if (returnResult != null) {
//            LOGGER.info("Get {} remote model inference result from cache.", remoteModelInferenceResultCacheKey);
//        }
//        return returnResult;
//    }


}
