package com.example.plugin;

import com.example.plugin.config.Config;
import com.example.plugin.core.Locale;
import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import games.negative.alumina.AluminaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class PaperPlugin extends AluminaPlugin {

    private static PaperPlugin instance;

    private Config configuration;
    private YamlConfigurationStore<Config> store;

    @Override
    public void load() {
        instance = this;
    }

    @Override
    public void enable() {
        reload();
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
                        Configuration
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

    }

    @NotNull
    public Config getConfiguration() {
        return configuration;
    }


    @NotNull
    public static PaperPlugin instance() {
        return instance;
    }

    @NotNull
    public static Config config() {
        return instance().getConfiguration();
    }

}
