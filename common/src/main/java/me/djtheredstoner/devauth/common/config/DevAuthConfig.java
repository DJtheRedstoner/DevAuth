package me.djtheredstoner.devauth.common.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import me.djtheredstoner.devauth.common.Properties;
import me.djtheredstoner.devauth.common.util.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.util.*;

public class DevAuthConfig {

    private final String defaultAccount;
    private final Map<String, Account> accounts;
    private final File configDir;

    private DevAuthConfig(String defaultAccount, Map<String, Account> accounts, File configDir) {
        this.defaultAccount = defaultAccount;
        this.accounts = accounts;
        this.configDir = configDir;
    }

    public static DevAuthConfig load() {
        String configDirPath = Properties.CONFIG_DIR.getValue();
        if (configDirPath == null) configDirPath = Util.getDefaultConfigDir().getAbsolutePath();

        File configDir = new File(configDirPath);

        if (configDir.exists() && !configDir.isDirectory()) throw new RuntimeException("Config directory is not a directory");

        File configFile = new File(configDir, "config.toml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();

            try (InputStream is = DevAuthConfig.class.getResourceAsStream("/assets/devauth/config.default.toml")) {
                if (is == null) {
                    throw new RuntimeException("Failed to locate default config file");
                }
                FileUtils.writeByteArrayToFile(configFile, IOUtils.toByteArray(is));
            } catch (Exception e) {
                throw new RuntimeException("Failed to write default config file", e);
            }
        }

        FileConfig config = FileConfig.of(configFile);
        config.load();

        String defaultAccount = config.get("defaultAccount");

        Map<String, Account> accounts = new LinkedHashMap<>();

        for (Config.Entry entry : config.<Config>get("accounts").entrySet()) {
            Config value = entry.getValue();
            accounts.put(entry.getKey(), new Account(
                entry.getKey(),
                AccountType.of(value.get("type")),
                value.get("username"),
                value.get("password")
            ));
        }

        return new DevAuthConfig(defaultAccount, accounts, configDir);
    }

    public String getDefaultAccount() {
        return defaultAccount;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public File getConfigDir() {
        return configDir;
    }
}
