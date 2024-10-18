package com.ericlmao.redisjoin.command;

import com.ericlmao.redisjoin.RedisJoinPlugin;
import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandProperties;
import games.negative.alumina.command.Context;
import games.negative.alumina.logger.Logs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandMetaData extends Command {

    private static final String METADATA_KEY = "redisjoin:metadata";

    public CommandMetaData() {
        super(CommandProperties.builder().name("metadata").playerOnly(true).build());
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player().orElseThrow();

        String key = METADATA_KEY + "." + player.getUniqueId();

        if (context.args().length == 0) {
            String message = RedisJoinPlugin.redis().runCommand(jedis -> jedis.hget(key, "message"));
            Logs.info("Result of hget: " + message);
            if (message == null) {
                player.sendRichMessage("<red>Metadata not set.");
                return;
            }

            player.sendRichMessage("<green>Metadata: <white>" + message);
            return;
        }

        String message = String.join(" ", context.args());

        RedisJoinPlugin.redis().runCommand(jedis -> {
            jedis.hset(key, "uuid", player.getUniqueId().toString());
            jedis.hset(key, "message", message);
            return null;
        });

        player.sendMessage("Metadata set to: " + message);
    }
}
