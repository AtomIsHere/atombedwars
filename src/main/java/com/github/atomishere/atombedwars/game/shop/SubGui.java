package com.github.atomishere.atombedwars.game.shop;

import com.github.atomishere.atombedwars.arena.BedwarsArena;
import com.github.atomishere.atombedwars.utils.AtomUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import java.util.List;


public class SubGui implements InventoryHolder, Listener {
    private final Inventory inventory;

    private final ShopItem.ShopCategory category;
    @Getter
    private final ShopGui mainGui;

    private final BedwarsArena arena;

    public SubGui(ShopItem.ShopCategory category, ShopGui mainGui, BedwarsArena arena) {
        this.inventory = Bukkit.getServer().createInventory(this, 9, category.getName());

        this.category = category;
        this.mainGui = mainGui;

        this.arena = arena;
    }

    public void initializeItems() {
        for(ShopItem shopItem : ShopItem.values()) {
            if(!shopItem.getCategory().equals(category)) {
                continue;
            }

            ItemStack item = new ItemStack(shopItem.getDisplay());
            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(AtomUtils.hideText(shopItem.name()));

            switch(shopItem.getCurrency()) {
                case IRON:
                    lore.add(ChatColor.WHITE + "Cost: " + shopItem.getCost() + " Iron");
                case GOLD:
                    lore.add(ChatColor.GOLD + "Cost: " + shopItem.getCost() + " Gold");
                case EMERALD:
                    lore.add(ChatColor.GREEN + "Cost: " + shopItem.getCost() + " Emerald");
            }
            meta.setLore(lore);

            meta.setDisplayName(shopItem.getName());

            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();

        backMeta.setDisplayName(ChatColor.BOLD + "Back");

        List<String> lore = new ArrayList<>();
        lore.add(AtomUtils.hideText("BACK"));
        backMeta.setLore(lore);

        backItem.setItemMeta(backMeta);

        inventory.addItem(backItem);
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
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

        if(hiddenData.equals("BACK")) {
            player.closeInventory();
            mainGui.openInventory(player);
            return;
        }

        ShopItem item;
        try {
            item = ShopItem.valueOf(hiddenData);
        } catch(IllegalArgumentException ignored) {
            return;
        }

        ItemStack money = null;
        switch(item.getCurrency()) {
            case IRON: {
                for(ItemStack moneyItem : player.getInventory()) {
                    if(moneyItem.getType().equals(Material.IRON_INGOT)) {
                        money = moneyItem;
                    }
                }
            }
            case GOLD: {
                for(ItemStack moneyItem : player.getInventory()) {
                    if(moneyItem.getType().equals(Material.GOLD_INGOT)) {
                        money = moneyItem;
                    }
                }
            }
            case EMERALD: {
                for(ItemStack moneyItem : player.getInventory()) {
                    if(moneyItem.getType().equals(Material.EMERALD)) {
                        money = moneyItem;
                    }
                }
            }
        }

        if(money == null) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "You don't have enough " + item.getCurrency().name().toLowerCase() + "!");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);
            return;
        }else if(item.getCost() >= money.getAmount()) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "You don't have enough " + item.getCurrency().name().toLowerCase() + "!");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);
            return;
        }

        if(item.getCost() == money.getAmount()) {
            player.getInventory().remove(money);
        } else {
            ItemStack newMoneyItem = new ItemStack(money.getType(), money.getAmount() - item.getCost());
            player.getInventory().remove(money);
            player.getInventory().addItem(newMoneyItem);
        }

        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 1);

        item.getBuyable().giveItem(player, arena, item);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
