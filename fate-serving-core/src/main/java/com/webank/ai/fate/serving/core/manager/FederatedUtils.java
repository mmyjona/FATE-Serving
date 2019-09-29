//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.webank.ai.fate.serving.core.manager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.webank.ai.fate.serving.core.bean.FederatedRoles;
import org.apache.commons.lang3.StringUtils;

public class FederatedUtils {
    public FederatedUtils() {
    }

    public static String federatedRolesIdentificationString(FederatedRoles federatedRoles) {
        if (federatedRoles == null) {
            return "all";
        } else {
            Object[] roleNames = federatedRoles.getRoleMap().keySet().toArray();
            Arrays.sort(roleNames);
            List<String> allPartyTmp = new ArrayList();

            for(int i = 0; i < roleNames.length; ++i) {
                Object[] partys = (new ArrayList(new HashSet((Collection)federatedRoles.getRoleMap().get(roleNames[i])))).toArray();
                Arrays.sort(partys);
                allPartyTmp.add(StringUtils.join(Arrays.asList(roleNames[i], StringUtils.join(partys, "_")), "-"));
            }

            return StringUtils.join(allPartyTmp, "#");
        }
    }
}
