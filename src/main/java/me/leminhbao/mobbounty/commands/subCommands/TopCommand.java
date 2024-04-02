package me.leminhbao.mobbounty.commands.subCommands;

import me.leminhbao.mobbounty.MobBounty;
import me.leminhbao.mobbounty.inventoryframework.TopMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCommand {
    private final MobBounty plugin;

    public TopCommand(MobBounty plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length != 2) {
            return false;
        }

        if (args[0].equalsIgnoreCase("top")) {
            if (!player.hasPermission("mobbounty.top")) {
                player.sendMessage("You do not have permission to use this command!");
                return true;
            }
            new TopMenu(plugin, args[1]).open(player, args[1]);
        }
        return true;
    }
}
