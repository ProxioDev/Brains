package net.limework.brains.prometheus;

import com.imaginarycode.minecraft.redisbungee.api.tasks.RedisTask;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import net.limework.brains.redisbungee.RedisBungeeHook;
import redis.clients.jedis.UnifiedJedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedisBungeeCollector extends Collector {

    private final RedisBungeeHook hook;

    public RedisBungeeCollector(RedisBungeeHook hook) {
        this.hook = hook;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();
        List<String> proxies = hook.getCurrentProxiesIds(false);
        mfs.add(new GaugeMetricFamily("redisbungee_online_proxies", "Shows current proxies that are online", proxies.size()));
        mfs.add(new GaugeMetricFamily("redisbungee_total_online", "Show total online players in RedisBungee network", hook.getCurrentCount()));
        GaugeMetricFamily gaugeMetricFamily = new GaugeMetricFamily("redisbungee_proxy_online_count", "shows how many players in a single proxy", List.of("proxy"));
        new PullCountPerProxy(proxies, hook).execute().forEach((proxy, count) -> gaugeMetricFamily.addMetric(List.of(proxy),  count));
        mfs.add(gaugeMetricFamily);
        return mfs;
    }
}
