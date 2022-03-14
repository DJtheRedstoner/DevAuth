package me.djtheredstoner.devauth.common;

public enum Properties {

    ENABLED("enabled", "false"),
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

    public BooleanState getBooleanValue() {
        String key = getFullKey();
        if (System.getProperty(key) == null) return BooleanState.NOT_SET;
        return BooleanState.of(Boolean.parseBoolean(getValue()));
    }

    public enum BooleanState {
        TRUE,
        FALSE,
        NOT_SET;

        public static BooleanState of(boolean state) {
            if (state) {
                return TRUE;
            } else {
                return FALSE;
            }
        }

        public boolean toBoolean() {
            if (this == NOT_SET) {
                throw new UnsupportedOperationException("NOT_SET cannot be converted to a boolean value");
            } else if (this == TRUE) {
                return true;
            } else {
                return false;
            }
        }
    }

}
