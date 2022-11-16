package net.limework.brains.redisbungee;

import com.google.common.collect.Multimap;
import com.imaginarycode.minecraft.redisbungee.AbstractRedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.api.AbstractDataManager;
import com.imaginarycode.minecraft.redisbungee.api.PubSubListener;
import com.imaginarycode.minecraft.redisbungee.api.RedisBungeeMode;
import com.imaginarycode.minecraft.redisbungee.api.RedisBungeePlugin;
import com.imaginarycode.minecraft.redisbungee.api.config.ConfigLoader;
import com.imaginarycode.minecraft.redisbungee.api.config.RedisBungeeConfiguration;
import com.imaginarycode.minecraft.redisbungee.api.events.IPlayerChangedServerNetworkEvent;
import com.imaginarycode.minecraft.redisbungee.api.events.IPlayerJoinedNetworkEvent;
import com.imaginarycode.minecraft.redisbungee.api.events.IPlayerLeftNetworkEvent;
import com.imaginarycode.minecraft.redisbungee.api.events.IPubSubMessageEvent;
import com.imaginarycode.minecraft.redisbungee.api.summoners.Summoner;
import com.imaginarycode.minecraft.redisbungee.api.util.uuid.NameFetcher;
import com.imaginarycode.minecraft.redisbungee.api.util.uuid.UUIDFetcher;
import com.imaginarycode.minecraft.redisbungee.api.util.uuid.UUIDTranslator;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;
import net.limework.brains.models.RedisPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedisBungeeHook implements RedisBungeePlugin<RedisPlayer>, ConfigLoader {

    private AbstractRedisBungeeAPI redisBungeeAPI;
    private final AbstractDataManager<RedisPlayer, ?, ?, ?> dataManager = new DataManager(this);
    private final Path dataFolder = Path.of("");
    private Summoner<?> summoner;
    private RedisBungeeMode redisBungeeMode;
    private final OkHttpClient httpClient;
    private final UUIDTranslator uuidTranslator = new UUIDTranslator(this);

    private final RedisBungeeConfiguration configuration = new RedisBungeeConfiguration("Brains", new ArrayList<>(), false, false, null);

    public RedisBungeeHook() {
        this.httpClient = new OkHttpClient();
        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(6));
        this.httpClient.setDispatcher(dispatcher);
        NameFetcher.setHttpClient(httpClient);
        UUIDFetcher.setHttpClient(httpClient);
    }

    public void initialize() {
        try {
            loadConfig(this, this.dataFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        redisBungeeAPI = new RedisBungeeAPI(this);
    }

    public void stop() {
        this.httpClient.getDispatcher().getExecutorService().shutdown();
        try {
            this.summoner.close();
        } catch (Exception ignored) {
        }
    }

    public Summoner<?> getSummoner() {
        return this.summoner;
    }


    public RedisBungeeConfiguration getConfiguration() {
        return this.configuration;
    }

    public int getCount() {
        return getCurrentCount();
    }

    public Set<String> getLocalPlayersAsUuidStrings() {
        return null;
    }

    public AbstractDataManager<RedisPlayer, ?, ?, ?> getDataManager() {
        return this.dataManager;
    }

    public AbstractRedisBungeeAPI getAbstractRedisBungeeApi() {
        return this.redisBungeeAPI;
    }

    public UUIDTranslator getUuidTranslator() {
        return this.uuidTranslator;
    }

    public Multimap<String, UUID> serverToPlayersCache() {
        return serversToPlayers();
    }

    public List<String> getProxiesIds() {
        return getCurrentProxiesIds(false);
    }

    public PubSubListener getPubSubListener() {
        return null;
    }

    public void executeAsync(Runnable runnable) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public void executeAsyncAfter(Runnable runnable, TimeUnit timeUnit, int i) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public boolean isOnlineMode() {
        return true;
    }

    private final Logger logger = LogManager.getLogger(this.getClass());

    public void logInfo(String s) {
        logger.info(s);
    }

    public void logWarn(String s) {
        logger.warn(s);
    }

    public void logFatal(String s) {
        logger.fatal(s);
    }

    public RedisPlayer getPlayer(UUID uuid) {
        return null;
    }

    public RedisPlayer getPlayer(String s) {
        return null;
    }

    public UUID getPlayerUUID(String s) {
        return null;
    }

    public String getPlayerName(UUID uuid) {
        return null;
    }

    public String getPlayerServerName(RedisPlayer redisPlayer) {
        return null;
    }

    public boolean isPlayerOnAServer(RedisPlayer redisPlayer) {
        return false;
    }

    public InetAddress getPlayerIp(RedisPlayer redisPlayer) {
        return null;
    }

    public RedisBungeeMode getRedisBungeeMode() {
        return redisBungeeMode;
    }

    public void updateProxiesIds() {

    }

    public IPlayerChangedServerNetworkEvent createPlayerChangedServerNetworkEvent(UUID uuid, String s, String s1) {
        return null;
    }

    public IPlayerJoinedNetworkEvent createPlayerJoinedNetworkEvent(UUID uuid) {
        return null;
    }

    public IPlayerLeftNetworkEvent createPlayerLeftNetworkEvent(UUID uuid) {
        return null;
    }

    public IPubSubMessageEvent createPubSubEvent(String s, String s1) {
        return null;
    }

    public void fireEvent(Object o) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public void onConfigLoad(RedisBungeeConfiguration configuration, Summoner<?> summoner, RedisBungeeMode mode) {
        this.summoner = summoner;
        this.redisBungeeMode = mode;
    }
}
