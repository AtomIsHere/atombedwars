package com.github.atomishere.atombedwars.game.shop;

import com.github.atomishere.atombedwars.arena.BedwarsArena;
import com.github.atomishere.atombedwars.utils.AtomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopGui implements InventoryHolder, Listener {
    private final Inventory inventory;

    private final BedwarsArena arena;

    private final Map<ShopItem.ShopCategory, SubGui> guis = new HashMap<>();

    public ShopGui(BedwarsArena arena) {
        this.inventory = Bukkit.getServer().createInventory(this, 9, "Shop");

        this.arena = arena;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void initializeItems() {
        for(ShopItem.ShopCategory category : ShopItem.ShopCategory.values()) {
            SubGui subGui = new SubGui(category, this, arena);
            subGui.initializeItems();
            guis.put(category, subGui);

            List<String> lore = new ArrayList<>();
            lore.add(AtomUtils.hideText(category.name()));

            ItemStack item = new ItemStack(category.getDisplay());
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(category.getName());
            meta.setLore(lore);
            item.setItemMeta(meta);

            inventory.addItem(item);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getInventory().getHolder().equals(this)) {
            return;
        }

        event.setCancelled(true);

        if(event.getClick().equals(ClickType.NUMBER_KEY)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType() == Material.AIR) return;

        List<String> lore = clickedItem.getItemMeta().getLore();

        if(lore == null) return;

        String hiddenData;
        try {
            hiddenData = AtomUtils.revealText(lore.get(0));
        } catch(IndexOutOfBoundsException | IllegalArgumentException ignored) {
            return;
        }

        ShopItem.ShopCategory category;
        try {
            category = ShopItem.ShopCategory.valueOf(hiddenData);
        } catch(IllegalArgumentException ignored) {
            return;
        }

        SubGui subGui = guis.get(category);

        player.closeInventory();
        subGui.openInventory(player);
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
