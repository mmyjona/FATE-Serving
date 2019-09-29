package com.webank.ai.fate.serving.interfaces;

import com.webank.ai.fate.serving.federatedml.PipelineTask;

import java.util.List;
import java.util.Set;

public interface ModelCache {

    public  void put(String modelKey, PipelineTask model);
    public PipelineTask get(String modelKey);

    public  long getSize() ;

    public Set<String> getKeys();

    public PipelineTask loadModel(String modelKey);

}
