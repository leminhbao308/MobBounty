package me.leminhbao.mobbounty;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import lombok.Setter;
import me.leminhbao.mobbounty.utils.ColorCode;
import me.leminhbao.mobbounty.utils.mainUtils.DisableUtils;
import me.leminhbao.mobbounty.utils.mainUtils.EnableUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


public final class MobBounty extends JavaPlugin {

    @Setter @Getter
    private PlayerKillTrack playerKillTrack;
    @Setter @Getter
    private LeaderboardHandler leaderboardHandler;
    @Setter @Getter
    private MythicBukkit mythicBukkit;
    @Setter @Getter
    private List<String> mobList;

    @Override
    public void onEnable() {
        // Plugin startup logic
        long startTime = System.currentTimeMillis();

        getLogger().info(ColorCode.colorize("---------------------------------------------", ColorCode.YELLOW));
        getLogger().info("Author: " + ColorCode.colorize(getDescription().getAuthors().get(0), ColorCode.YELLOW));
        getLogger().info("Current version: " + ColorCode.colorize(getDescription().getVersion(), ColorCode.YELLOW));
        getLogger().info(ColorCode.colorize("---------------------------------------------", ColorCode.YELLOW));

        // Load config, if not exist, create new one from default config
        this.saveDefaultConfig();
        if (!EnableUtils.validateConfig(this)) {
            getLogger().severe(ColorCode.colorize("Config validation failed! Disabling plugin...", ColorCode.RED));
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            setMobList(getConfig().getStringList("display"));
        }

        if (!EnableUtils.checkDependencies(this)) {
            getLogger().severe(ColorCode.colorize("Missing dependencies. Disabling plugin...", ColorCode.RED));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Open database connection
        EnableUtils.openDatabase(this);

        // Register events
        EnableUtils.registerEvents(this);

        // Register commands
        EnableUtils.registerCommands(this);

        // Register tab completers
        EnableUtils.registerTabCompleters(this);

        // Register tasks
        EnableUtils.registerTasks(this);

        leaderboardHandler = new LeaderboardHandler(this);
        leaderboardHandler.onPluginReload(getPlayerKillTrack());

        long endTime = System.currentTimeMillis();
        String time = ColorCode.timeColor(endTime - startTime);

        getLogger().info(ColorCode.colorize("Plugin start up finished! (Took ", ColorCode.GREEN) + time + ColorCode.colorize(")", ColorCode.GREEN));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        long startTime = System.currentTimeMillis();

        // Unregister tasks
        DisableUtils.unregisterTasks(this);

        // Close database
        DisableUtils.closeDatabase(this);

        long endTime = System.currentTimeMillis();
        String time = ColorCode.timeColor(endTime - startTime);

        getLogger().info(ColorCode.colorize("Plugin shut down finished! (Took ", ColorCode.PURPLE) + time + ColorCode.colorize(")", ColorCode.PURPLE));
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        setMobList(getConfig().getStringList("display"));

        if (playerKillTrack == null) {
            playerKillTrack = new PlayerKillTrack(this);
        }

        if (leaderboardHandler == null) {
            leaderboardHandler = new LeaderboardHandler(this);
        }

        playerKillTrack.onConfigReload(getMobList());
        leaderboardHandler.onPluginReload(getPlayerKillTrack());
    }
}
