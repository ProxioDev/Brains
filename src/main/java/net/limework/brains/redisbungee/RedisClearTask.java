package net.limework.brains.redisbungee;

import com.google.gson.Gson;
import com.imaginarycode.minecraft.redisbungee.api.RedisBungeePlugin;
import com.imaginarycode.minecraft.redisbungee.api.tasks.RedisTask;
import com.imaginarycode.minecraft.redisbungee.api.util.uuid.CachedUUIDEntry;
import com.imaginarycode.minecraft.redisbungee.api.util.uuid.UUIDTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class RedisClearTask extends RedisTask<Void> {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final Gson gson = new Gson();

    public RedisClearTask(RedisBungeePlugin<?> plugin) {
        super(plugin);
    }

    // this code is inspired from https://github.com/minecrafter/redisbungeeclean
    @Override
    public Void unifiedJedisTask(UnifiedJedis unifiedJedis) {
        logger.info("==========[ Cached UUID entries cleanup ]==========");
        logger.info("Starting UUID cleaner.....");
        logger.info("fetching uuid cache...");
        try {
            final long number = unifiedJedis.hlen("uuid-cache");
            logger.info("Found {} entries", number);
            ArrayList<String> fieldsToRemove = new ArrayList<>();
            unifiedJedis.hgetAll("uuid-cache").forEach((field, data) -> {
                CachedUUIDEntry cachedUUIDEntry = gson.fromJson(data, CachedUUIDEntry.class);
                if (cachedUUIDEntry.expired()) {
                    fieldsToRemove.add(field);
                }
            });
            if (!fieldsToRemove.isEmpty()) {
                unifiedJedis.hdel("uuid-cache", fieldsToRemove.toArray(new String[0]));
            }
            logger.info("deleted {} entries", fieldsToRemove.size());
        } catch (JedisException e) {
            logger.error("There was an error fetching information", e);
        }
        logger.info("==========[ Cached UUID entries cleanup ]==========");
        return null;
    }
}
