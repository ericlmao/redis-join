package com.ericlmao.redisjoin.packet;

import org.jetbrains.annotations.NotNull;

public interface RedisPacket {

    @NotNull
    String toJson();

}
