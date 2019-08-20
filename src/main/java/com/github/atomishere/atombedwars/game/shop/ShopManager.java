package com.github.atomishere.atombedwars.game.shop;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopManager implements Listener {
    private final List<Entity> shopEntities = new ArrayList<>();
    private final Map<Player, List<ShopItem>> unlockedItems = new HashMap<>();


}
