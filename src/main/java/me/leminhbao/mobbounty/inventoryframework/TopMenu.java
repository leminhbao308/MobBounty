package me.leminhbao.mobbounty.inventoryframework;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.leminhbao.mobbounty.MobBounty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class TopMenu {
    private final MobBounty plugin;

    private final ChestGui gui;
    private final StaticPane displayPane;

    public TopMenu(MobBounty plugin, String mobName) {
        this.plugin = plugin;

        this.gui = new ChestGui(6, "Top Kill - " + mobName);
        this.displayPane = new StaticPane(0, 0, 9, 4);

        StaticPane backgroundPane = setupBackgroundPane();

        gui.addPane(backgroundPane);
        gui.addPane(displayPane);

        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnGlobalDrag(event -> event.setCancelled(true));
    }

    public void open(Player player, String mobName) {
        setupGui(player, mobName);
        gui.show(player);
    }

    private void setupGui(Player player, String mobName) {
        displayPane.clear();

        LinkedList<HashMap<UUID, Integer>> topTen = plugin.getLeaderboardHandler().getTopTen(mobName);

        if (topTen == null) {
            player.sendMessage("§4No data found for this mob.");
            return;
        }

        int i = 0;
        // Place player's head in the leaderboard with pyramid shape
        for (HashMap<UUID, Integer> playerData : topTen) {
            UUID playerUUID = playerData.keySet().stream().findFirst().orElse(null);
            if (i == 0) {
                displayPane.addItem(getPlayerHead(Bukkit.getPlayer(playerUUID), playerData, i + 1), Slot.fromXY(4, 0));
            } else if (i == 1 || i == 2) {
                displayPane.addItem(getPlayerHead(Bukkit.getPlayer(playerUUID), playerData, i + 1), Slot.fromXY(3 + i, 1));
            } else if (i == 3 || i == 4 || i == 5) {
                displayPane.addItem(getPlayerHead(Bukkit.getPlayer(playerUUID), playerData, i + 1), Slot.fromXY(2 + i, 2));
            } else if (i == 6 || i == 7 || i == 8 || i == 9) {
                displayPane.addItem(getPlayerHead(Bukkit.getPlayer(playerUUID), playerData, i + 1), Slot.fromXY(1 + i, 3));
            }
            i++;
        }

        int userKill = plugin.getPlayerKillTrack().getPlayerKill(player.getUniqueId()).get(mobName);
        int userRank = plugin.getLeaderboardHandler().getPlayerRank(mobName, player.getUniqueId());
        // Add the player head that open the menu
        displayPane.addItem(getPlayerHead(player, userKill, userRank), Slot.fromXY(4, 5));
    }

    private GuiItem getPlayerHead(Player player, HashMap<UUID, Integer> playerData, int rank) {
        int kills = playerData.values().iterator().next();

        ItemStack playerHead = getPlayerHeadItem(player, kills, rank);

        return new GuiItem(playerHead, event -> event.setCancelled(true));
    }

    private GuiItem getPlayerHead(Player player, int kill, int rank) {
        ItemStack playerHead = getPlayerHeadItem(player, kill, rank);

        return new GuiItem(playerHead, event -> event.setCancelled(true));
    }

    private StaticPane setupBackgroundPane() {
        StaticPane backgroundPane = new StaticPane(0, 0, 9, 6);
        backgroundPane.setPriority(Pane.Priority.LOWEST);

        ItemStack backgroundItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta backgroundItemMeta = backgroundItem.getItemMeta();
        backgroundItemMeta.setDisplayName(" ");
        backgroundItem.setItemMeta(backgroundItemMeta);

        GuiItem backgroundGuiItem = new GuiItem(backgroundItem, event -> event.setCancelled(true));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                backgroundPane.addItem(backgroundGuiItem, Slot.fromXY(i, j));
            }
        }

        return backgroundPane;
    }

    private ItemStack getPlayerHeadItem(Player player, int kills, int rank) {
        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).anyMatch(name -> name.equals("PLAYER_HEAD"));

        Material material = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
        ItemStack playerHead = new ItemStack(material, 1);

        if (isNewVersion) {
            playerHead.setDurability((short) 3);
            SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
            playerHeadMeta.setOwner(player.getName());
            playerHeadMeta.setDisplayName("§6" + player.getName());
            playerHeadMeta.setLore(List.of("§7Kills: §e" + kills, "§7Rank: §e" + rank));
            playerHeadMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            playerHead.setItemMeta(playerHeadMeta);
            return playerHead;
        } else {
            SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
            playerHeadMeta.setOwningPlayer(player);
            playerHeadMeta.setDisplayName("§6" + player.getName());
            playerHeadMeta.setLore(List.of("§7Kills: §e" + kills, "§7Rank: §e" + rank));
            playerHeadMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            playerHead.setItemMeta(playerHeadMeta);
            return playerHead;
        }
    }
}
