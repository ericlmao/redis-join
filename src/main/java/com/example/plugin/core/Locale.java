package com.example.plugin.core;

import com.example.plugin.PaperPlugin;
import games.negative.alumina.logger.Logs;
import games.negative.alumina.message.Message;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public enum Locale {

    ;

    private final String content;
    private Message message;

    Locale(@NotNull String content) {
        this.content = content;
        this.message = Message.of(content);
    }

    public static void init(@NotNull PaperPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        validateFile(file);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        boolean changed = false;
        for (Locale entry : values()) {
            if (config.isSet(entry.name())) continue;

            config.set(entry.name(), entry.content);
            changed = true;
        }

        if (changed) saveFile(file, config);

        for (Locale entry : values()) {
            entry.message = new Message(Objects.requireNonNull(config.getString(entry.name())));
        }
    }

    private static void saveFile(@NotNull File file, @NotNull FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            Logs.SEVERE.print("Could not save messages.yml file!", true);
        }
    }

    private static void validateFile(@NotNull File file) {
        if (!file.exists()) {
            boolean dirSuccess = file.getParentFile().mkdirs();
            if (dirSuccess) Logs.INFO.print("Created new plugin directory file!");

            try {
                boolean success = file.createNewFile();
                if (!success) return;

                Logs.INFO.print("Created messages.yml file!");
            } catch (IOException e) {
                Logs.SEVERE.print("Could not create messages.yml file!", true);
            }
        }
    }


    /**
     * Send the final message to a {@link Audience}.
     *
     * @param audience The recipient of the message.
     */
    public void send(@NotNull Audience audience, @Nullable String... placeholders) {
        message.send(audience, placeholders);
    }

    /**
     * Send the final message to a {@link Audience}.
     *
     * @param audience The recipient of the message.
     */
    @SafeVarargs
    public final void send(@NotNull Audience audience, @Nullable Map.Entry<String, Component>... placeholders) {
        message.send(audience, placeholders);
    }

    /**
     * Send the final message to a {@link Audience}.
     *
     * @param audience The recipient of the message.
     */
    public void send(@NotNull Audience audience) {
        message.send(audience);
    }

    /**
     * Send the final message to an iterable collection of a class that extends {@link Audience}
     * @param iterable The iterable collection of a class that extends {@link Audience}
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @param <T> The class that extends {@link Audience}
     */
    public <T extends Iterable<? extends Audience>> void send(@NotNull T iterable, @Nullable String... placeholders) {
        message.send(iterable, placeholders);
    }

    /**
     * Send the final message to an iterable collection of a class that extends {@link Audience}
     * @param iterable The iterable collection of a class that extends {@link Audience}
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @param <T> The class that extends {@link Audience}
     */
    public <T extends Iterable<? extends Audience>> void send(@NotNull T iterable, @Nullable Map.Entry<String, Component>... placeholders) {
        message.send(iterable, placeholders);
    }

    /**
     * Send the final message to an iterable collection of a class that extends {@link Audience}
     * @param iterable The iterable collection of a class that extends {@link Audience}
     * @param <T> The class that extends {@link Audience}
     */
    public <T extends Iterable<? extends Audience>> void send(@NotNull T iterable) {
        message.send(iterable);
    }

    /**
     * Broadcast the final message to the server.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     */
    public void broadcast(@Nullable String... placeholders) {
        message.broadcast(placeholders);
    }

    /**
     * Broadcast the final message to the server.
     * @param audience The audience to display the message to.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     */
    public void broadcast(@Nullable Audience audience, @Nullable String... placeholders) {
        message.broadcast(audience, placeholders);
    }

    /**
     * Broadcast the final message to the server.
     */
    public void broadcast() {
        broadcast(null, (String[]) null);
    }

    /**
     * Converts the message to a Component.
     * @param audience The audience to display the message to.
     * @return The Component representation of the message.
     */
    @NotNull
    public Component asComponent(@Nullable Audience audience) {
        return asComponent(audience, (String[]) null);
    }

    /**
     * Converts the message to a Component.
     *
     * @param audience     The audience to display the message to.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @return The Component representation of the message.
     * @throws NullPointerException     if the audience is null.
     * @throws IllegalArgumentException if the number of placeholders is not even.
     */
    @NotNull
    public Component asComponent(@Nullable Audience audience, @Nullable String... placeholders) {
        return message.asComponent(audience, placeholders);
    }

    /**
     * Converts the message to a Component.
     *
     * @param audience     The audience to display the message to.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @return The Component representation of the message.
     * @throws NullPointerException     if the audience is null.
     * @throws IllegalArgumentException if the number of placeholders is not even.
     */
    @SafeVarargs
    @NotNull
    public final Component asComponent(@Nullable Audience audience, @Nullable Map.Entry<String, Component>... placeholders) {
        return message.asComponent(audience, placeholders);
    }
}