package me.leminhbao.mobbounty.utils.mainUtils;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.leminhbao.mobbounty.LeaderboardHandler;
import me.leminhbao.mobbounty.MobBounty;
import me.leminhbao.mobbounty.MobBountyPlaceholder;
import me.leminhbao.mobbounty.PlayerKillTrack;
import me.leminhbao.mobbounty.commands.CommandRouter;
import me.leminhbao.mobbounty.events.OnMythicMobKilled;
import me.leminhbao.mobbounty.events.OnPlayerJoin;
import me.leminhbao.mobbounty.events.OnPlayerLeave;
import me.leminhbao.mobbounty.utils.ColorCode;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class EnableUtils {
    // Check if all dependencies plugins are installed
    public static boolean checkDependencies(MobBounty plugin) {
        Logger logger = plugin.getLogger();

        // ItemsAdder
        if (plugin.getServer().getPluginManager().getPlugin("ItemsAdder") == null) {
            logger.severe(ColorCode.colorize("ItemsAdder not found! Please install it to use this plugin!", ColorCode.RED));
            return false;
        } else {
            // Check if ItemsAdder is enabled
            if (!plugin.getServer().getPluginManager().getPlugin("ItemsAdder").isEnabled()) {
                logger.severe(ColorCode.colorize("ItemsAdder is not enabled! Please enable it to use this plugin!", ColorCode.RED));
                return false;
            } else {
                logger.info(ColorCode.colorize("Hooked successfully to ItemsAdder!", ColorCode.GREEN));
            }
        }

        // PlaceholderAPI
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            logger.severe(ColorCode.colorize("PlaceholderAPI not found! Please install it to use this plugin!", ColorCode.RED));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } else {
            // Check if PlaceholderAPI is enabled
            if (!plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
                logger.severe(ColorCode.colorize("PlaceholderAPI is not enabled! Please enable it to use this plugin!", ColorCode.RED));
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            } else {
                // Register placeholders
                logger.info(ColorCode.colorize("Found PlaeholdersAPI! Registering placeholders!", ColorCode.GREEN));
                new MobBountyPlaceholder(plugin).register();
            }
        }

        // MythicMobs
        if (plugin.getServer().getPluginManager().getPlugin("MythicMobs") == null) {
            logger.severe(ColorCode.colorize("MythicMobs not found! Please install it to use this plugin!", ColorCode.RED));
            return false;
        } else {
            // Check if MythicMobs is enabled
            if (!plugin.getServer().getPluginManager().getPlugin("MythicMobs").isEnabled()) {
                logger.severe(ColorCode.colorize("MythicMobs is not enabled! Please enable it to use this plugin!", ColorCode.RED));
                return false;
            } else {
                plugin.setMythicBukkit(MythicBukkit.inst());
                logger.info(ColorCode.colorize("Hooked successfully to MythicMobs!", ColorCode.GREEN));
            }
        }

        // Everything is fine
        return true;
    }

    public static boolean validateConfig(MobBounty plugin) {
        // Check if config is valid


        // Everything is fine
        return true;
    }

    public static void registerEvents(MobBounty plugin) {
        // Register events here
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerJoin(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerLeave(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new OnMythicMobKilled(plugin), plugin);
    }

    public static void registerCommands(MobBounty plugin) {
        // Register commands here
        plugin.getCommand("mobbounty").setExecutor(new CommandRouter(plugin));
    }

    public static void registerTabCompleters(MobBounty plugin) {
        // Register tab completers here
    }

    public static void registerTasks(MobBounty plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getPlayerKillTrack().saveAllPlayersKills();
                plugin.getLeaderboardHandler().updateLeaderboard(plugin.getPlayerKillTrack());
            }
        }.runTaskTimer(plugin, 5 * 20L * 60L, 5 * 20L * 60L);
    }

    public static void openDatabase(MobBounty plugin) {
        // Open database connection
        plugin.setPlayerKillTrack(new PlayerKillTrack(plugin));
        plugin.setLeaderboardHandler(new LeaderboardHandler(plugin));
        plugin.getLeaderboardHandler().onPluginReload(plugin.getPlayerKillTrack());
    }
}
