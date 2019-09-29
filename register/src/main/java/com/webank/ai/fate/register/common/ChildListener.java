
package com.webank.ai.fate.register.common;

import java.util.List;

public interface ChildListener {

    void childChanged(String path, List<String> children);

}
