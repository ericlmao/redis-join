package com.ericlmao.redisjoin.listener;

import com.ericlmao.redisjoin.RedisJoinPlugin;
import com.ericlmao.redisjoin.core.Locale;
import com.ericlmao.redisjoin.event.RedisPacketReceiveEvent;
import com.ericlmao.redisjoin.packet.PlayerJoinPacket;
import com.ericlmao.redisjoin.packet.PlayerQuitPacket;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onRegularJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerJoinPacket packet = new PlayerJoinPacket(player.getUniqueId(), RedisJoinPlugin.config().getServerName());
        RedisJoinPlugin.redis().publish(packet);
    }

    @EventHandler
    public void onRegularQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerQuitPacket packet = new PlayerQuitPacket(player.getUniqueId(), RedisJoinPlugin.config().getServerName());
        RedisJoinPlugin.redis().publish(packet);
    }

    @EventHandler
    public void onRedisJoin(@NotNull RedisPacketReceiveEvent event) {
        if (!(event.getPacket() instanceof PlayerJoinPacket packet)) return;

        UUID uuid = packet.getUuid();
        String server = packet.getServer();

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        Locale.PLAYER_JOIN.broadcast("%username%", player.getName(), "%server%", server);
    }

    @EventHandler
    public void onRedisQuit(@NotNull RedisPacketReceiveEvent event) {
        if (!(event.getPacket() instanceof PlayerQuitPacket packet)) return;

        UUID uuid = packet.getUuid();
        String server = packet.getServer();

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        Locale.PLAYER_QUIT.broadcast("%username%", player.getName(), "%server%", server);
    }

}
