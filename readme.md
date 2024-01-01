# DevAuth

![WakaTime badge](https://wakatime.com/badge/user/a7885461-d6b5-4541-b841-a07642af2cfd/project/d488cdfd-0654-421b-abcb-7478b4256185.svg)

Safely authenticate Microsoft and Mojang accounts in development environments.

# Minecraft Version Support

| Versions             | Module         | Supported |
|----------------------|----------------|:---------:|
| 1.14 - 1.20.4 Fabric | `fabric`       |     ✅     |
| 1.8.9 - 1.12.2 Forge | `forge-legacy` |     ✅     |
| 1.14 - 1.20.1 Forge  | `forge-latest` |     ✅     |
| 1.20.4 NeoForge      | `neoforge`     |     ✅     |

**Note:** If a version isn't listed above as supported, just try it.
Additionally, the fabric module may work on other fabric-based loaders (such as legacy-fabric).

# Usage

DevAuth can be used either by placing a jar in your mods folder or adding a
maven dependency. Details about the two methods follow.

## Jar

Download a DevAuth jar from the [releases](https://github.com/DJtheRedstoner/DevAuth/releases),
place it in your mods folder and configure it using the configuration section below.

## Maven dependency

Add the DevAuth repository
```gradle
repositories {
    maven { url = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1" }
}
```

Add the DevAuth dependency

[![DevAuth badge](https://img.shields.io/maven-metadata/v?label=DevAuth&metadataUrl=https%3A%2F%2Fpkgs.dev.azure.com%2Fdjtheredstoner%2FDevAuth%2F_packaging%2Fpublic%2Fmaven%2Fv1%2Fme%2Fdjtheredstoner%2FDevAuth-common%2Fmaven-metadata.xml)][azurePackages]

```kt
dependencies {
    // moduleName is based on your mod loader and minecraft version, see the table above
    // version is the DevAuth version you are adding, check releases on GitHub or the badge above
    // With loom use the modRuntimeOnly configuration
    // With archloom and the forge-legacy module use the runtimeOnly configuration to avoid warnings
    // With ForgeGradle 5 or NeoGradle, use the runtimeOnly configuration
    // With ForgeGradle 2, use the implementation configuration as runtimeOnly appears to be broken
    modRuntimeOnly("me.djtheredstoner:DevAuth-${moduleName}:${version}")
}
```
**If you use `gg.essential.loom` and the `forge-legacy` module, you must use version
`0.10.0.2` or newer of `gg.essential.loom`**

You can now enable and configure DevAuth. See the section below for details on how to do this.

# Configuration

DevAuth is configured through JVM properties and a configuration file.
JVM Properties can be by adding `-D<propertyName>=<value>` to your JVM arguments
or by using [`System.setProperty`][setProperty] before DevAuth is initialized 
(fabric `preLaunch` entrypoint for example).

## JVM Properties:

|       Property        | Description                    | Default                             |
|:---------------------:|:-------------------------------|:------------------------------------|
|   `devauth.enabled`   | Enables DevAuth                | `false`                             |
|  `devauth.configDir`  | Selects the config directory   | `.devauth` folder in home directory |
|   `devauth.account`   | Select the account to log into | none                                |

## Configuration File:

**NEW: The `devauth.enabled` property now defaults to false and a default configuration wil not be created
until DevAuth has been enabled once. Then you can set the `defaultEnabled` config property to make DevAuth
active by default in every project.**

The configuration file is called `config.toml` and is located in your DevAuth config
folder.

### Default config folder locations

|   OS    | Default config directory                                      |
|:-------:|---------------------------------------------------------------|
| Windows | `C:\Users\<user>\.devauth`                                    |
|  MacOS  | `/Users/<user>/.devauth`                                      |
|  Linux  | `$XDG_CONFIG_HOME/devauth`, defaulting to `~/.config/devauth` |

### Config file format

```toml
# chose if DevAuth should be on by default in new projects
defaultEnabled = true

# choose which account to use when devauth.account property is not specified
defaultAccount = "main"

# a mojang account
[accounts.main]
type = "mojang"
username = "example@example.com"
password = "hunter12"

# a microsoft account
# note that setting username and password IS NOT required and does nothing
[accounts.alt]
type = "microsoft"
```
When the `devauth.account` property is specified it takes precedence over the
`defaultAccount` config option.

A default config will be automatically created when DevAuth is first enabled.

# Microsoft Accounts

When logging in with a microsoft account for the first time, you will be given a
link to open in a browser to complete OAuth, after that the token will be stored
in a file called `microsoft_accounts.json` in your config directory. Future logins
will use and refresh the stored tokens as necessary. You will be prompted to go through
OAuth again once your refresh token expires (my research leads me to believe it should last 90 days)
or is revoked.

# Safety and Security

I like to think of DevAuth as safe and somewhat secure. Yes, Mojang account passwords are
stored in plain text, however they ~~will soon be~~ are now deprecated, so I decided investing time and effort
into encrypting them would be silly. The Microsoft account tokens stored locally in `microsoft_accounts.json`
only provide limited access to your account and can be revoked by removing the DevAuth app [here][manageConsent].
If you don't think this is secure enough, make an issue or pull request.

# Discord
[<img src="https://inv.wtf/widget/djl" width="500" alt="Discord Widget"/>](https://inv.wtf/djl)

[setProperty]: https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/lang/System.html#setProperty(java.lang.String,java.lang.String)
[manageConsent]: https://account.live.com/consent/Manage
[azurePackages]: https://dev.azure.com/djtheredstoner/DevAuth/_artifacts/feed/public