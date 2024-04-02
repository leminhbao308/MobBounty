package me.leminhbao.mobbounty.commands.subCommands;

import me.leminhbao.mobbounty.MobBounty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand {

    private final MobBounty plugin;

    public ReloadCommand(MobBounty plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("mobbounty.reload")) {
            sender.sendMessage("You do not have permission to use this command!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("mobbounty") || command.getName().equalsIgnoreCase("mb")) {
            if (args[0].equalsIgnoreCase("reload")) {
                long startTime = System.currentTimeMillis();

                plugin.reloadConfig();

                long endTime = System.currentTimeMillis();
                sender.sendMessage("Config reloaded in " + (endTime - startTime) + "ms!");
                return true;
            }
        }

        return false;
    }
}
