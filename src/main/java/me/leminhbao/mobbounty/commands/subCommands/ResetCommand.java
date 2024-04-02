package me.leminhbao.mobbounty.commands.subCommands;

import me.leminhbao.mobbounty.MobBounty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ResetCommand {

    private final MobBounty plugin;

    public ResetCommand(MobBounty plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            return false;
        }

        if (!sender.hasPermission("mobbounty.reset")) {
            sender.sendMessage("You don't have permission to use this command");
            return true;
        }

        // Reset all mobs data
        if (args[1].equalsIgnoreCase("all")) {
            plugin.getPlayerKillTrack().resetAllPlayersKill(true);
            plugin.getLeaderboardHandler().updateLeaderboard(plugin.getPlayerKillTrack());
            sender.sendMessage("All mobs data has been reset");
            return true;
        }

        // Reset a specific mob data
        plugin.getPlayerKillTrack().resetAllPlayerKill(args[1], true);
        plugin.getLeaderboardHandler().updateLeaderboard(plugin.getPlayerKillTrack());
        sender.sendMessage("Mob data has been reset");
        return true;
    }
}
