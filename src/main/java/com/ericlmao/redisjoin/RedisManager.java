package com.ericlmao.redisjoin;

import com.ericlmao.redisjoin.config.Config;
import com.ericlmao.redisjoin.event.RedisPacketReceiveEvent;
import com.ericlmao.redisjoin.packet.PlayerJoinPacket;
import com.ericlmao.redisjoin.packet.PlayerQuitPacket;
import com.ericlmao.redisjoin.packet.RedisPacket;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import games.negative.alumina.event.Events;
import games.negative.alumina.logger.Logs;
import games.negative.alumina.util.Tasks;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

public class RedisManager {

    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    private final Jedis subscriber;
    private final Jedis publisher;
    private final Map<String, Class<? extends RedisPacket>> byChannel;
    private final Map<Class<? extends RedisPacket>, String> byClass;

    public RedisManager(@NotNull Config config) {
        this.subscriber = new Jedis(config.getRedisHost(), config.getRedisPort());
        String auth = this.subscriber.auth(config.getRedisPassword());

        this.publisher = new Jedis(config.getRedisHost(), config.getRedisPort());
        this.publisher.auth(config.getRedisPassword());
        Logs.log("connection auth code: " + auth, true);

        Logs.log("Connected to Redis server at " + config.getRedisHost() + ":" + config.getRedisPort(), true);

        this.byChannel = Maps.newConcurrentMap();
        this.byClass = Maps.newConcurrentMap();

        registerPacket("player-join", PlayerJoinPacket.class);
        registerPacket("player-quit", PlayerQuitPacket.class);
    }

    private <T extends RedisPacket> void registerPacket(@NotNull String channel, @NotNull Class<T> clazz) {
        Logs.info("Registering packet " + clazz.getSimpleName() + " to channel " + channel, true);
        Tasks.async(new RedisSubscriber<T>(clazz, channel));
        Logs.info("Registered packet " + clazz.getSimpleName() + " to channel " + channel, true);
    }

    public <T extends RedisPacket> void publish(@NotNull T packet) {
        String channel = byClass.get(packet.getClass());
        String json = gson.toJson(packet);

        Logs.info("Sending packet to channel " + channel + ": " + json, true);

        publisher.publish(channel, json);
    }

    public void close() {
        subscriber.close();
        publisher.close();
    }

    @RequiredArgsConstructor
    private class RedisSubscriber<T extends RedisPacket> extends BukkitRunnable {

        private final Class<T> clazz;
        private final String channel;

        @Override
        public void run() {
            byChannel.put(channel, clazz);
            byClass.put(clazz, channel);

            JedisPubSub pubSub = new JedisPubSub() {

                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    Logs.info("Subscribed to channel " + channel, true);
                }

                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    Logs.info("Unsubscribed from channel " + channel, true);
                }

                @Override
                public void onMessage(String channel, String message) {
                    Logs.info("Received packet from channel " + channel + ": " + message, true);
                    Class<? extends RedisPacket> clazz = byChannel.get(channel);
                    if (clazz == null) return;

                    RedisPacket packet = gson.fromJson(message, clazz);
                    if (packet == null) {
                        Logs.warning("Failed to parse packet from channel " + channel, true);
                        return;
                    }

                    Tasks.run(new RedisPacketCaller(channel, packet));
                }
            };

            subscriber.subscribe(pubSub, channel);
        }
    }

    @RequiredArgsConstructor
    private static class RedisPacketCaller extends BukkitRunnable {

        private final String channel;
        private final RedisPacket packet;

        @Override
        public void run() {
            RedisPacketReceiveEvent event = new RedisPacketReceiveEvent(channel, packet);
            Events.call(event);
        }
    }
}
