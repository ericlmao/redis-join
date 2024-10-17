package com.ericlmao.redisjoin.event;

import com.ericlmao.redisjoin.packet.RedisPacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class RedisPacketReceiveEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String channel;
    private final RedisPacket packet;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
