package com.ericlmao.redisjoin;

import com.ericlmao.redisjoin.config.Config;
import com.ericlmao.redisjoin.core.Locale;
import com.ericlmao.redisjoin.listener.PlayerListener;
import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import games.negative.alumina.AluminaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class RedisJoinPlugin extends AluminaPlugin {

    private static RedisJoinPlugin instance;

    private Config configuration;
    private YamlConfigurationStore<Config> store;

    private RedisManager redis;

    @Override
    public void load() {
        instance = this;
    }

    @Override
    public void enable() {
        reload();

        this.redis = new RedisManager(configuration);

        registerListener(new PlayerListener());
    }

    public void reload() {
        Locale.init(this);
        initConfig();
    }

    /**
     * Initializes the configuration for the plugin.
     * This method sets up the YamlConfigurationStore and updates the configuration from the "main.yml" file.
     * If the store is not null, it creates a new YamlConfigurationStore with the specified properties.
     * The configuration variable is then updated from the file using the store.
     */
    public void initConfig() {
        File file = new File(getDataFolder(), "main.yml");

        if (this.store == null) {
            YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder()
                    .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
                    .inputNulls(true).outputNulls(true)
                    .header("""
                        --------------------------------------------------------
                        RedisJoin Configuration
                        \s
                        Useful Resources:
                        - MiniMessage: https://docs.advntr.dev/minimessage/
                        - MiniMessage Web UI: https://webui.advntr.dev/
                        --------------------------------------------------------
                        """)
                    .footer("""
                        Authors: ericlmao
                        """).build();

            this.store = new YamlConfigurationStore<>(Config.class, properties);
        }

        this.configuration = store.update(file.toPath());
    }

    public void saveConfiguration() {
        File dir = getDataFolder();
        File file = new File(dir, "main.yml");

        store.save(configuration, file.toPath());
        initConfig();
    }

    @Override
    public void disable() {
        redis.close();
    }

    @NotNull
    public Config getConfiguration() {
        return configuration;
    }

    @NotNull
    public RedisManager getRedis() {
        return redis;
    }

    @NotNull
    public static RedisJoinPlugin instance() {
        return instance;
    }

    @NotNull
    public static Config config() {
        return instance().getConfiguration();
    }

    @NotNull
    public static RedisManager redis() {
        return instance().getRedis();
    }
}
