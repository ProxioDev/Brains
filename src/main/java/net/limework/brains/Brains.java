package net.limework.brains;

import com.imaginarycode.minecraft.redisbungee.AbstractRedisBungeeAPI;
import io.prometheus.client.exporter.HTTPServer;
import net.limework.brains.redisbungee.RedisBungeeHook;
import net.limework.brains.redisbungee.RedisClearTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Brains {
    private final RedisBungeeHook hook = new RedisBungeeHook();

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private final Console console = new Console(this);

    private HTTPServer httpServer;

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
        scheduledExecutorService.scheduleAtFixedRate(new RedisClearTask(hook), 1, 1, TimeUnit.DAYS);
    }

    void start() {
        logger.info("Starting RedisBungee Brains By Limework.net");
        try {
            redisHookStart();
            printRedisBungeeNetworkStats();
            initRepeatableCleanTask();
            httpServer = new HTTPServer("localhost", 8000);
        } catch (Throwable e) {
            shutdown(e);
            return;
        }
        logger.info("Brains started successfully!");
        initConsoleCommands();
    }

    void shutdown(Throwable e) {
        if (e != null) {
            logger.fatal("Failed to start Brains", e);
        }
        logger.info("stopping brains...");
        logger.info("stopping exporter....");
        if (this.httpServer != null) {
            this.httpServer.close();
        }
        hook.logInfo("Shutting down RedisBungee hook....");
        hook.stop();
        logger.info("Shutting down execution service.....");
        scheduledExecutorService.shutdown();
        logger.info("Goodbye! :)");
    }

    void initConsoleCommands() {
        scheduledExecutorService.submit(console);
    }

    private static class Console implements Runnable {
        private final Logger logger = LogManager.getLogger(this.getClass());
        private final Scanner scanner = new Scanner(System.in);
        // This very basic / buggy console handling.

        private final Brains brains;

        public Console(Brains brains) {
            this.brains = brains;
        }

        private void printHelp() {
            logger.info("=======[ Help page ]=======");
            logger.info("1. \"help\": prints this page");
            logger.info("2. \"shutdown\", \"stop\": shutdown Brains.");
            logger.info("3, \"forcecleanuuid\": forces the uuid clean up. node it wont reset the timer.");
        }

        private final List<String> shutdownArgs = Arrays.asList("shutdown", "stop");

        @Override
        public void run() {
            while (scanner.hasNext()) {
                String[] args = scanner.nextLine().split(" ");
                if (args[0].equalsIgnoreCase("help")) {
                    printHelp();
                } else if (shutdownArgs.contains(args[0].toLowerCase())) {
                    scanner.close();
                    brains.shutdown(null);
                    break;
                } else if (args[0].equalsIgnoreCase("forcecleanuuid")) {
                    new RedisClearTask(brains.hook).execute();
                } else {
                    logger.info("Unknown command, type \"help\" for the help page.");
                }
            }
        }
    }

}