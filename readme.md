# DevAuth

Safely authenticate Microsoft and Mojang accounts in development environments.

# Minecraft Version Support

| Versions | Module | Supported |
| ------ | -------- | :---------: |
| 1.14 - 1.17.1 fabric | `fabric` | ✅ |
| 1.8.9 - 1.12.2 forge | `forge-legacy` | ✅ |
| 1.14 - 1.17.1 forge | `forge-latest` | ✅ |

# Usage

Download a DevAuth jar from the [releases](https://github.com/DJtheRedstoner/DevAuth/releases),
place it in your mods folder and configure it uses the configuration section below.

# Configuration

DevAuth is configured through JVM properties and a configuration file.
JVM Properties can be by adding `-D<propertyName>=<value>` to your JVM arguments
or by using [`System.setProperty`][setProperty] before DevAuth initialized 
(fabric `preLaunch` entrypoint for example).

## JVM Properties:

| Property | Description | Default |
| :------: | :---------- | :----- |
| `devauth.enabled` | Enables DevAuth | `true` |
| `devauth.configDir` | Selects the config directory | `.devauth` folder in home directory |
| `devauth.account` | Select the account to log into | none

## Configuration File:

The configuration file is called `config.toml` and is located in your DevAuth config
folder (defaults to `.devauth` in home directory, `C:\Users\<user>\.devauth` for Windows users).

```toml
# choose which account to use when devauth.account property is not specified
defaultAccount = "main"

# a mojang account
[accounts.main]
type = "mojang"
username = "example@example.com"
password = "hunter12"

# a microsoft account
[accounts.alt]
type = "microsoft"
```
When the `devauth.account` property is specified it takes precedence over the
`defaultAccount` config option.

A default config will be automatically created when DevAuth is first loaded.

# Microsoft Accounts

When logging in with a microsoft account for the first time, you will be given a
link to open in a browser to complete OAuth, after that the token will be stored
in a file called `microsoft_accounts.json` in your config directory. Future logins
will use and refresh the stored tokens as necessary. You will be prompted to go through
OAuth again once your refresh token expires (my research leads me to think it should last 90 days)
or is revoked.

For some reason when clicking "Use Another account" in the OAuth account picker, microsoft
immediately redirects you to the redirect uri with the error 
`access_denied: The user has denied access to the scope requested by the client application.`.
Unless I am doing something wrong, this is caused by an issue on Microsoft's side. I recommend copying
the OAuth url into a private browser in order to log in.

# Safety and Security

I like to think of DevAuth as safe and somewhat secure. Yes, Mojang account passwords are
stored in plain text, however they will soon be deprecated so I decided investing time and effort
into encrypting them would be silly. The Microsoft account tokens stored in `microsoft_accounts.json`
only provide limited access to your account and can be revoked by removing the DevAuth app [here][manageConsent].
If you don't think this is secure enough, make an issue or pull request.

[setProperty]: https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/lang/System.html#setProperty(java.lang.String,java.lang.String)
[manageConsent]: https://account.live.com/consent/Manage