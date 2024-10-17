package com.ericlmao.redisjoin.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerJoinPacket implements RedisPacket{

    private UUID uuid;
    private String server;

    @Override
    public @NotNull String toJson() {
        return toString();
    }
}
