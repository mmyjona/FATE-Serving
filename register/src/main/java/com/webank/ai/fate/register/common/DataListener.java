
package com.webank.ai.fate.register.common;


public interface DataListener {

    void dataChanged(String path, Object value, EventType eventType);
}
