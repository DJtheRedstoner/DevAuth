package me.djtheredstoner.devauth.common;

import me.djtheredstoner.devauth.common.auth.IAuthProvider;
import me.djtheredstoner.devauth.common.auth.SessionData;
import me.djtheredstoner.devauth.common.config.Account;
import me.djtheredstoner.devauth.common.config.DevAuthConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class DevAuth {

    private static final Set<String> authOptions =
        new HashSet<>(Arrays.asList("--accessToken", "--uuid", "--username", "--userType", "--userProperties"));

    private final Properties.BooleanState enabled;
    private final Logger logger;
    private DevAuthConfig config;

    public DevAuth() {
        enabled = Properties.ENABLED.getBooleanValue();
        logger = LogManager.getLogger("DevAuth");
    }

    public String[] processArguments(String[] args) {
        DevAuthConfig maybeConfig = DevAuthConfig.load(false);
        if (!isEnabled(maybeConfig.getDefaultEnabled())) {
            logger.info("DevAuth disabled, not logging in!");
            return args;
        }

        config = DevAuthConfig.load(true);

        SessionData data = login();

        List<String> newArgs = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (authOptions.contains(arg)) {
                i++;
            } else {
                newArgs.add(arg);
            }
        }

        newArgs.add("--accessToken=" + data.getAccessToken());
        newArgs.add("--uuid=" + data.getUuid());
        newArgs.add("--username=" + data.getUsername());
        newArgs.add("--userType=" + data.getUserType());
        newArgs.add("--userProperties=" + data.getUserProperties());

        return newArgs.toArray(new String[0]);
    }

    public SessionData login() {
        Account account = getSelectedAccount();

        IAuthProvider authProvider = account.getType().getAuthProviderFactory().create(this);

        SessionData session = authProvider.login(account);

        logger.info("Successfully logged in as " + session.getUsername());
        return session;
    }

    public Account getSelectedAccount() {
        String commandLineAccount = Properties.ACCOUNT.getValue();
        String defaultAccount = config.getDefaultAccount();

        if (commandLineAccount != null) {
            if (config.getAccounts().containsKey(commandLineAccount)) {
                return config.getAccounts().get(commandLineAccount);
            }
            throw new RuntimeException("Account '" + commandLineAccount + "' not found, valid accounts are: " + getValidAccounts());
        } else if (defaultAccount != null) {
            if (config.getAccounts().containsKey(defaultAccount)) {
                return config.getAccounts().get(defaultAccount);
            }
            throw new RuntimeException("Account '" + defaultAccount + "' not found, valid accounts are: " + getValidAccounts());
        }
        throw new RuntimeException("No account specified, specify one with the defaultAccount config option or the " + Properties.ACCOUNT.getFullKey() + " property");
    }

    public boolean isEnabled(boolean defaultEnabled) {
        if (enabled == Properties.BooleanState.NOT_SET) {
            return defaultEnabled;
        } else {
            return enabled.toBoolean();
        }
    }

    public DevAuthConfig getConfig() {
        return config;
    }

    private String getValidAccounts() {
        StringJoiner joiner = new StringJoiner(", ");
        for (Account account : config.getAccounts().values()) {
            joiner.add(account.getName());
        }
        return joiner.toString();
    }

}
