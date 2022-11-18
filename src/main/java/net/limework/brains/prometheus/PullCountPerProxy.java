package net.limework.brains.prometheus;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.imaginarycode.minecraft.redisbungee.api.RedisBungeePlugin;
import com.imaginarycode.minecraft.redisbungee.api.tasks.RedisTask;
import redis.clients.jedis.UnifiedJedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullCountPerProxy extends RedisTask<Map<String, Long>> {

    private final List<String> proxiesToPull;

    public PullCountPerProxy(List<String> proxiesToPull, RedisBungeePlugin<?> plugin) {
        super(plugin);
        this.proxiesToPull = proxiesToPull;
    }

    @Override
    public Map<String, Long> unifiedJedisTask(UnifiedJedis unifiedJedis) {
        HashMap<String, Long> playersPerProxy = new HashMap<>();
        proxiesToPull.forEach((proxy) -> playersPerProxy.put(proxy, unifiedJedis.scard("proxy:" + proxy + ":usersOnline")));
        return ImmutableMap.copyOf(playersPerProxy);
    }
}
