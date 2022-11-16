package net.limework.brains.redisbungee;

import com.imaginarycode.minecraft.redisbungee.api.AbstractDataManager;
import com.imaginarycode.minecraft.redisbungee.api.RedisBungeePlugin;
import net.limework.brains.models.RedisPlayer;

import java.util.UUID;

public class DataManager extends AbstractDataManager<RedisPlayer, Object, Object, Object> {

    public DataManager(RedisBungeePlugin<RedisPlayer> plugin) {
        super(plugin);
    }

    public void onPostLogin(Object event) {

    }

    public void onPlayerDisconnect(Object event) {

    }

    public void onPubSubMessage(Object event) {

    }

    public boolean handleKick(UUID target, String message) {
        return false;
    }
}
