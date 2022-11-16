package net.limework.brains.redisbungee;

import com.google.gson.Gson;
import com.imaginarycode.minecraft.redisbungee.api.RedisBungeePlugin;
import com.imaginarycode.minecraft.redisbungee.api.tasks.RedisTask;
import com.imaginarycode.minecraft.redisbungee.api.util.uuid.CachedUUIDEntry;
import com.imaginarycode.minecraft.redisbungee.api.util.uuid.UUIDTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;
public class RedisClearTask extends RedisTask<Void> {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final Gson gson = new Gson();

    public RedisClearTask(RedisBungeePlugin<?> plugin) {
        super(plugin);
    }

    @Override
    public Void unifiedJedisTask(UnifiedJedis unifiedJedis) {
        Thread.currentThread().setName("RedisBungee-UUIDCache-Cleaner");
        logger.info("==========[ Cached UUID entries cleanup ]==========");
        logger.info("Starting UUID cleaner.....");
        logger.info("fetching uuid cache...");
        Map<String, String> uuidCache = unifiedJedis.hgetAll("uuid-cache");
        logger.info("Found {} entries", uuidCache.size());
        logger.info("converting......");
        uuidCache.forEach((key, data) -> {
            CachedUUIDEntry cachedUUIDEntry = gson.fromJson(data, CachedUUIDEntry.class);
        });
        logger.info("==========[ Cached UUID entries cleanup ]==========");
        return null;
    }
}
