package com.webank.ai.fate.register.common;


public class ServiceVersionUtil {


    static final String  BIGER="BIGER";
    static final String  BIGER_OR_EQUAL="BIGTHAN_OR_EQUAL";
    static final String  SMALLER="SMALLER";
    static final String  EQUALS="EQUALS";



    public  static  boolean  march(String  versionModel ,String  serverVersion,String  clientVersion) {

        Integer serverVersionInteger = new Integer(serverVersion);
        Integer clientVersionInteger = new Integer(clientVersion);

        switch (versionModel) {

            case BIGER_OR_EQUAL:
                if (clientVersionInteger >= serverVersionInteger) {
                    return true;
                }

            case BIGER:
                if (clientVersionInteger > serverVersionInteger) {
                    return true;
                }
            case SMALLER:
                if (clientVersionInteger < serverVersionInteger) {
                    return true;
                }

            default:
                return false;
        }

    }

}
