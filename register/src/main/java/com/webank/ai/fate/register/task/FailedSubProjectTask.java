package com.webank.ai.fate.register.task;


import com.webank.ai.fate.register.common.FailbackRegistry;
import com.webank.ai.fate.register.interfaces.NotifyListener;
import com.webank.ai.fate.register.interfaces.Timeout;
import com.webank.ai.fate.register.url.URL;

public class FailedSubProjectTask extends AbstractRetryTask {

    private static final String NAME = "retry subscribe project";



    public FailedSubProjectTask(URL url, FailbackRegistry registry) {
        super(url, registry, NAME);


    }

    @Override
    protected void doRetry(URL url, FailbackRegistry registry, Timeout timeout) {
        registry.doSubProject(url.getProject());
        registry.removeFailedSubscribedProjectTask(url.getProject());
    }
}