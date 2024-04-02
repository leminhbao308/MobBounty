package me.leminhbao.mobbounty.inventoryframework;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.leminhbao.mobbounty.MobBounty;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MobViewMenu {
    private final MobBounty plugin;

    private final ChestGui gui;
    private final PaginatedPane displayPane;

    private int currentPage = 0;

    private GuiItem nextButton;
    private GuiItem previousButton;

    public MobViewMenu(MobBounty plugin) {
        this.plugin = plugin;

        this.gui = new ChestGui(6, "Mob View");
        this.displayPane = new PaginatedPane(0, 0, 9, 4);

        setupActionButton();

        StaticPane backgroundPane = setupBackgroundPane();

        StaticPane actionPane = new StaticPane(0, 5, 9, 1);
        actionPane.addItem(previousButton, Slot.fromIndex(0));
        actionPane.addItem(nextButton, Slot.fromIndex(8));

        gui.addPane(backgroundPane);
        gui.addPane(displayPane);
        gui.addPane(actionPane);

        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnGlobalDrag(event -> event.setCancelled(true));
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

    public void open(Player player) {
        setupGui(player.getUniqueId());
        gui.show(player);
    }

    public void openOther(Player player, UUID targetUUID) {
        setupGui(targetUUID);
        gui.show(player);
    }

    private void setupGui(UUID uuid) {
        displayPane.clear();
        currentPage = 0;

        Collection<MythicMob> mmobs = MythicBukkit.inst().getMobManager().getMobTypes();

        List<GuiItem> displayMobItems = new ArrayList<>();

        for (Map.Entry<String, Integer> mobData : plugin.getPlayerKillTrack().getPlayerKill(uuid).entrySet()) {
            String mobName = mobData.getKey();
            int killCount = mobData.getValue();

            for (MythicMob mmob : mmobs) {
                if (mmob.getInternalName().equals(mobName)) {
                    ItemStack mobItem = new ItemStack(mmob.getMythicEntity().getHead());
                    ItemMeta mobItemMeta = mobItem.getItemMeta();
                    mobItemMeta.setDisplayName(mmob.getDisplayName().get());
                    mobItemMeta.setLore(List.of("Killed: " + killCount));
                    mobItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    mobItem.setItemMeta(mobItemMeta);

                    displayMobItems.add(new GuiItem(mobItem, event -> event.setCancelled(true)));
                }
            }
        }
        
        displayPane.populateWithGuiItems(displayMobItems);
        displayPane.setPage(currentPage);
    }

    void setupActionButton() {
        ItemStack nextBtnItem = new ItemStack(Material.ARROW);
        ItemMeta nextBtnMeta = nextBtnItem.getItemMeta();
        nextBtnMeta.setDisplayName("Next Page");
        nextBtnItem.setItemMeta(nextBtnMeta);

        this.nextButton = new GuiItem(nextBtnItem, event -> {
            if (currentPage < displayPane.getPages() - 1) {
                displayPane.setPage(currentPage + 1);
                currentPage++;
            }
            event.setCancelled(true);
        });

        ItemStack previousBtnItem = new ItemStack(Material.ARROW);
        ItemMeta previousBtnMeta = previousBtnItem.getItemMeta();
        previousBtnMeta.setDisplayName("Previous Page");
        previousBtnItem.setItemMeta(previousBtnMeta);

        this.previousButton = new GuiItem(previousBtnItem, event -> {
            if (currentPage > 0) {
                displayPane.setPage(currentPage - 1);
                currentPage--;
            }
            event.setCancelled(true);
        });
    }
}
