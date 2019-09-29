//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.webank.ai.fate.serving.core.manager;


import java.util.Arrays;

import com.webank.ai.fate.serving.core.bean.FederatedRoles;
import org.apache.commons.lang3.StringUtils;

public class SceneUtils {
    private static final String sceneKeySeparator = "#";

    public SceneUtils() {
    }

    public static String genSceneKey(String role, String partyId, FederatedRoles federatedRoles) {
        return StringUtils.join(Arrays.asList(role, partyId, FederatedUtils.federatedRolesIdentificationString(federatedRoles)), "#");
    }
}
