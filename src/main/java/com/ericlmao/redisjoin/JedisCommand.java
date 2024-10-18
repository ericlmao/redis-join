package com.ericlmao.redisjoin;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

@FunctionalInterface
public interface JedisCommand<T> {

    T run(@NotNull Jedis jedis);

}
