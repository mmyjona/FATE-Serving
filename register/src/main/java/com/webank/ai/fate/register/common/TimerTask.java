
package com.webank.ai.fate.register.common;

import com.webank.ai.fate.register.interfaces.Timeout;


public interface TimerTask {

    void run(Timeout timeout) throws Exception;
}