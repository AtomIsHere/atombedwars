package com.github.atomishere.atombedwars.game;

import com.github.atomishere.atombedwars.arena.BedwarsArena;
import com.github.atomishere.atombedwars.game.shop.ShopItem;
import org.bukkit.entity.Player;

public interface Buyable {
    void giveItem(Player player, BedwarsArena arena, ShopItem shopItem);
}
