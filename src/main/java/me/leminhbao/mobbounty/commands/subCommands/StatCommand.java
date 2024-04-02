package me.leminhbao.mobbounty.commands.subCommands;

import me.leminhbao.mobbounty.MobBounty;
import me.leminhbao.mobbounty.inventoryframework.MobViewMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatCommand {

    private final MobBounty plugin;

    public StatCommand(MobBounty plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("stat")) {
                if (!player.hasPermission("mobbounty.stat.view")) {
                player.sendMessage("You do not have permission to use this command!");
                return true;
            }
            new MobViewMenu(plugin).open(player);
            return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("stat")) {
                String targetName = args[1];
                Player target = plugin.getServer().getPlayer(targetName);

                if (target == null) {
                    sender.sendMessage("Player not found!");
                    return true;
                }

                if (!sender.hasPermission("mobbounty.stat.view.others")) {
                    sender.sendMessage("You do not have permission to use this command!");
                    return true;
                }

                new MobViewMenu(plugin).openOther(player, target.getUniqueId());
                return true;
            }
        }

        return false;
    }
}
