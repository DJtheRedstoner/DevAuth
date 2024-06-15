# DevAuth

![WakaTime badge](https://wakatime.com/badge/user/a7885461-d6b5-4541-b841-a07642af2cfd/project/d488cdfd-0654-421b-abcb-7478b4256185.svg)

Safely authenticate Minecraft accounts in development environments.

# Minecraft Version Support

| Versions               | Module         | Supported |
|------------------------|----------------|:---------:|
| 1.14 - 1.21 Fabric     | `fabric`       |     ✅     |
| 1.8.9 - 1.12.2 Forge   | `forge-legacy` |     ✅     |
| 1.14 - 1.21 Forge      | `forge-latest` |     ✅     |
| 1.20.4 - 1.21 NeoForge | `neoforge`     |     ✅     |

**Note:** If a version isn't listed above as supported, just try it.
Additionally, the fabric module may work on other fabric-based loaders (such as legacy-fabric).

# Usage

DevAuth can be used either by placing a jar in your mods folder or adding a
maven dependency. Details about the two methods follow.

<details>
<summary>Jar</summary>

Download a DevAuth jar from the [releases](https://github.com/DJtheRedstoner/DevAuth/releases),
place it in your mods folder and configure it using the configuration section below.

</details>

<details>
<summary>Maven Dependency</summary>

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

</details>

You can now enable and configure DevAuth. See the next section for how to do this.

# Configuration

**DevAuth defaults to disabled**, in order to be unobtrusive. You must enable DevAuth in order for it to log you in.
Additionally, the configuration file will not be created if DevAuth is disabled. You should enable DevAuth once
via the JVM property, so that it creates the configuration file, then you may configure it via the file.

DevAuth is configured through JVM properties and a configuration file.
JVM Properties can set be by adding `-D<propertyName>=<value>` to your JVM arguments
or by using [`System.setProperty`][setProperty] before DevAuth is initialized 
(Fabric's `preLaunch` entrypoint for example). Additionally, your specific
toolchain/gradle plugins may have specific ways to configure JVM properties.

## JVM Properties

|       Property        | Description                    | Default                                          |
|:---------------------:|:-------------------------------|:-------------------------------------------------|
|   `devauth.enabled`   | Enables DevAuth                | `false`                                          |
|  `devauth.configDir`  | Selects the config directory   | [See below](#default-config-directory-locations) |
|   `devauth.account`   | Select the account to log into | none                                             |

## Configuration File

The configuration file is called `config.toml` and is located in your DevAuth config
folder.

### Default config directory locations

|   OS    | Default config directory                                      |
|:-------:|---------------------------------------------------------------|
| Windows | `C:\Users\<user>\.devauth`                                    |
|  MacOS  | `/Users/<user>/.devauth`                                      |
|  Linux  | `$XDG_CONFIG_HOME/devauth`, defaulting to `~/.config/devauth` |

### Config file format

```toml
# Choose if DevAuth should be enabled default. Overriden by the devauth.enabled property.
defaultEnabled = true

# Choose which account to use when devauth.account property is not specified
defaultAccount = "main"

# A Microsoft account
# You do not need to put any credentials in the configuration file, as OAuth is used to sign in
[accounts.main]
type = "microsoft"

# A second account, which can be selected by changing defaultAccount above or using the devauth.account property
[accounts.alt]
type = "microsoft"
```
When the `devauth.account` property is specified it takes precedence over the
`defaultAccount` config option.

A default config will be automatically created when DevAuth is first enabled.

# How it works

When logging in with a microsoft account for the first time, you will be given a
link to open in a browser to complete OAuth, after that the token will be stored
in a file called `microsoft_accounts.json` in your config directory. Future logins
will use and refresh the stored tokens as necessary. You will be prompted to go through
OAuth again once your refresh token expires (most likely to occur after a long period
without using DevAuth) or is revoked.

# Security

DevAuth stores all credentials locally on your machine. The Microsoft account tokens are stored in
`microsoft_accounts.json` inside the DevAuth configuration directory. The contents of this file are not
encrypted, so do not share it or open it when it may be seen. If you want to revoke DevAuth's permissions
or believe this file may have been compromised, DevAuth's permissions can be revoked [here][manageConsent].
Note that this does **not** immediately revoke all access tokens, due to design decisions by Microsoft.
See [here][tokenLifetimes] for more information.

# Discord
[<img src="https://inv.wtf/widget/djl" width="500" alt="Discord Widget"/>](https://inv.wtf/djl)

[setProperty]: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/System.html#setProperty(java.lang.String,java.lang.String)
[manageConsent]: https://account.live.com/consent/Manage
[tokenLifetimes]: https://learn.microsoft.com/en-us/entra/identity-platform/configurable-token-lifetimes#access-tokens
[azurePackages]: https://dev.azure.com/djtheredstoner/DevAuth/_artifacts/feed/public
