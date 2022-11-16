package net.limework.brains.models;

import java.util.UUID;

public class RedisPlayer {

    private final UUID uuid;

    public RedisPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
