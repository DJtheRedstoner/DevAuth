package me.djtheredstoner.devauth.common.config;

public class Account {

    private final String name;
    private final AccountType type;
    private final String username;
    private final String password;

    public Account(String name, AccountType type, String username, String password) {
        this.name = name;
        this.type = type;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
