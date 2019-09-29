

package com.webank.ai.fate.register.interfaces;


import com.webank.ai.fate.register.common.TimerTask;

public interface Timeout {


    Timer timer();


    TimerTask task();


    boolean isExpired();


    boolean isCancelled();


    boolean cancel();
}