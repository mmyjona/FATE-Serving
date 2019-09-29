
package com.webank.ai.fate.register.common;


public class ConfigChangeEvent {
    private final String key;

    private final String value;
    private final ConfigChangeType changeType;

    public ConfigChangeEvent(String key, String value) {
        this(key, value, ConfigChangeType.MODIFIED);
    }

    public ConfigChangeEvent(String key, String value, ConfigChangeType changeType) {
        this.key = key;
        this.value = value;
        this.changeType = changeType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public ConfigChangeType getChangeType() {
        return changeType;
    }

    @Override
    public String toString() {
        return "ConfigChangeEvent{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", changeType=" + changeType +
                '}';
    }
}
