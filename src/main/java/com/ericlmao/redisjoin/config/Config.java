package com.ericlmao.redisjoin.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class Config {

    @Comment("The name of the server.")
    private String serverName = "server";

    @Comment({"","The host of the Redis server."})
    private String redisHost = "localhost";

    @Comment({"", "The port of the Redis server."})
    private int redisPort = 6379;

    @Comment({"", "The password of the Redis server."})
    private String redisPassword = "password";

}
