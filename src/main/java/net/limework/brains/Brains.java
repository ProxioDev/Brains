package net.limework.brains;

import com.imaginarycode.minecraft.redisbungee.AbstractRedisBungeeAPI;
import net.limework.brains.redisbungee.RedisBungeeHook;
import net.limework.brains.redisbungee.RedisClearTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Brains {
    private final RedisBungeeHook hook = new RedisBungeeHook();

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    void redisHookStart() {
        hook.logInfo("loading RedisBungee hook.....");
        hook.logInfo("==========[ RedisBungee hook ]==========");
        hook.logInfo("Note: Please ignore anything related to proxy id");
        hook.initialize();
        hook.logInfo("==========[ RedisBungee hook ]==========");
        hook.logInfo("RedisBungee hook load was successful");
    }
    void printRedisBungeeNetworkStats() {
        hook.logInfo("==========[ RedisBungee Stats ]==========");
        hook.logInfo(hook.getCount() + " players online.");
        hook.logInfo(hook.getCurrentProxiesIds(false).size() + " proxies were found");
        hook.logInfo("==========[ RedisBungee Stats ]==========");
    }

    void initRepeatableCleanTask() {
        // RIGHT NOW hard code the time/timeunit.
        scheduledExecutorService.scheduleAtFixedRate(new RedisClearTask(hook), 1,  1, TimeUnit.DAYS);
    }

    void start() {
        logger.info("Starting RedisBungee Brains By Limework.net");
        try {
            redisHookStart();
            printRedisBungeeNetworkStats();
        } catch (Throwable e) {
            shutdown(e);
            return;
        }
        logger.info("Brains started successfully!");
        initRepeatableCleanTask();
    }

    void shutdown(Throwable e) {
        if (e != null) {
            logger.fatal("Failed to start Brains", e);
        }
        logger.info("stopping brains...");
        hook.logInfo("Shutting down RedisBungee hook....");
        hook.stop();
        logger.info("Shutting down execution service.....");
        scheduledExecutorService.shutdown();
        logger.info("Goodbye! :)");
    }


}
