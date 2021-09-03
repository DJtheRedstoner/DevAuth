package me.djtheredstoner.devauth.common;

public enum Properties {

    ENABLED("enabled", "true"),
    CONFIG_DIR("configDir", null),
    ACCOUNT("account", null);

    private final String key;
    private final String defaultValue;

    Properties(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    String getFullKey() {
        return "devauth." + key;
    }

    public String getValue() {
        return System.getProperty(getFullKey(), defaultValue);
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(getValue());
    }

}
