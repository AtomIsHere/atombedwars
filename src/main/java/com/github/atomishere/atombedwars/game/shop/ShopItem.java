package com.github.atomishere.atombedwars.game.shop;

import com.github.atomishere.atombedwars.arena.BedwarsArena;
import com.github.atomishere.atombedwars.game.Buyable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ShopItem {
    //Blocks
    WOOL("Wool", ShopCategory.BLOCKS, Material.WOOL, CurrencyMaterial.IRON, 5, false, new BasicBuyable(Material.WOOL, 16, null, null)),
    SANDSTONE("Sandstone", ShopCategory.BLOCKS, Material.SANDSTONE, CurrencyMaterial.IRON, 16, false, new BasicBuyable(Material.WOOL, 16, null, null)),
    END_STONE("End Stone", ShopCategory.BLOCKS, Material.ENDER_STONE, CurrencyMaterial.GOLD, 20, false, new BasicBuyable(Material.ENDER_STONE, 8, null, null)),
    OBSIDIAN("Obsidian", ShopCategory.BLOCKS, Material.OBSIDIAN, CurrencyMaterial.EMERALD, 20, false, new BasicBuyable(Material.OBSIDIAN, 8, null, null)),

    //Armour
    CHAIN_MAIL_ARMOUR("Chain Mail", ShopCategory.ARMOUR, Material.CHAINMAIL_CHESTPLATE, CurrencyMaterial.IRON, 24, true, new ArmourBuyable(ArmourBuyable.ArmourType.CHAINMAIL)),
    IRON_ARMOUR("Iron Armour", ShopCategory.ARMOUR, Material.IRON_CHESTPLATE, CurrencyMaterial.GOLD, 18, true, new ArmourBuyable(ArmourBuyable.ArmourType.IRON)),
    DIAMOND_ARMOUR("Diamond Armour", ShopCategory.ARMOUR, Material.DIAMOND_CHESTPLATE, CurrencyMaterial.EMERALD, 16, true, new ArmourBuyable(ArmourBuyable.ArmourType.DIAMOND)),

    //Weapons
    STONE_SWORD("Stone Sword", ShopCategory.WEAPONS, Material.STONE_SWORD, CurrencyMaterial.IRON, 10, false, new SwordBuyable(SwordBuyable.SwordType.STONE)),
    IRON_SWORD("Iron Sword", ShopCategory.WEAPONS, Material.IRON_SWORD, CurrencyMaterial.GOLD, 10, false, new SwordBuyable(SwordBuyable.SwordType.IRON)),
    DIAMOND_SWORD("Diamond Sword", ShopCategory.WEAPONS, Material.DIAMOND_SWORD, CurrencyMaterial.EMERALD, 6, false, new SwordBuyable(SwordBuyable.SwordType.DIAMOND)),
    BOW("Bow", ShopCategory.WEAPONS, Material.BOW, CurrencyMaterial.IRON, 10, false, new BowBuyable(0, false, 0)),
    POWER_BOW("Power Bow", ShopCategory.WEAPONS, Material.BOW, CurrencyMaterial.IRON, 20, false, new BowBuyable(1, false, 0)),
    POWER_TWO_BOW("Power Two Bow", ShopCategory.WEAPONS, Material.BOW, CurrencyMaterial.GOLD, 10, false, new BowBuyable(2, false, 0)),
    POWER_PUNCH_TWO_BOW("Power Punch Two Bow", ShopCategory.WEAPONS, Material.BOW, CurrencyMaterial.GOLD, 20, false, new BowBuyable(2, true, 2)),

    //Tools
    SHEARS("Shears", ShopCategory.TOOLS, Material.SHEARS, CurrencyMaterial.IRON, 5, false, new BasicBuyable(Material.SHEARS, 1, null, null)),
    STONE_PICKAXE("Stone Pickaxe", ShopCategory.TOOLS, Material.STONE_PICKAXE, CurrencyMaterial.IRON, 10, false, new BasicBuyable(Material.STONE_PICKAXE, 1, null, EnchantmentMapBuilder.newBuilder().add(Enchantment.DIG_SPEED, 2).build())),
    IRON_PICKAXE("Iron Pickaxe", ShopCategory.TOOLS, Material.IRON_PICKAXE, CurrencyMaterial.GOLD, 10, false, new BasicBuyable(Material.IRON_PICKAXE, 1, null, EnchantmentMapBuilder.newBuilder().add(Enchantment.DIG_SPEED, 2).build())),
    DIAMOND_PICKAXE("Diamond Pickaxe", ShopCategory.TOOLS, Material.DIAMOND_PICKAXE, CurrencyMaterial.GOLD, 15, false, new BasicBuyable(Material.DIAMOND_PICKAXE, 1, null, EnchantmentMapBuilder.newBuilder().add(Enchantment.DIG_SPEED, 2).build())),

    //Specials
    TNT("TNT", ShopCategory.SPECIALS, Material.TNT, CurrencyMaterial.GOLD, 5, false, new BasicBuyable(Material.TNT, 1, null, null)),
    FIREBALL("Fireball", ShopCategory.SPECIALS, Material.FIREBALL, CurrencyMaterial.IRON, 10, false, new BasicBuyable(Material.FIREBALL, 1, "Fireball", null)),
    BRIDGE_EGG("Bridge Egg", ShopCategory.TOOLS, Material.EGG, CurrencyMaterial.EMERALD, 5, false, new BasicBuyable(Material.EGG, 1, "Bridge Egg", null));

    @Getter
    private final String name;
    @Getter
    private final ShopCategory category;
    @Getter
    private final Material display;
    @Getter
    private final CurrencyMaterial currency;
    @Getter
    private final int cost;
    @Getter
    private final boolean persistent;
    @Getter
    private final Buyable buyable;

    public static class EnchantmentMapBuilder {
        private Map<Enchantment, Integer> enchantmentMap = new HashMap<>();

        public static EnchantmentMapBuilder newBuilder() {
            return new EnchantmentMapBuilder();
        }
        private EnchantmentMapBuilder() {
        }

        public EnchantmentMapBuilder add(Enchantment enchantment, Integer integer) {
            enchantmentMap.put(enchantment, integer);
            return this;
        }

        public Map<Enchantment, Integer> build() {
            return enchantmentMap;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static enum CurrencyMaterial {
        EMERALD(Material.EMERALD),
        IRON(Material.IRON_INGOT),
        GOLD(Material.GOLD_INGOT);

        private final Material material;
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static enum ShopCategory {
        BLOCKS("Blocks", Material.SANDSTONE),
        WEAPONS("Weapons", Material.DIAMOND_SWORD),
        ARMOUR("Armour", Material.IRON_CHESTPLATE),
        TOOLS("Tools", Material.IRON_PICKAXE),
        SPECIALS("Special Items", Material.EGG);

        @Getter
        private final String name;
        @Getter
        private final Material display;
    }

    @RequiredArgsConstructor
    public static class BasicBuyable implements Buyable {
        private final Material item;
        private final int amount;
        private final String name;
        private final Map<Enchantment, Integer> enchantments;

        @Override
        public void giveItem(Player player, BedwarsArena arena, ShopItem shopItem) {
            ItemStack itemStack = new ItemStack(item, amount);

            ItemMeta meta = itemStack.getItemMeta();
            if(name != null) {
                meta.setDisplayName(name);
            }
            if(enchantments != null) {
                for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    meta.addEnchant(entry.getKey(), entry.getValue(), false);
                }
            }

            meta.spigot().setUnbreakable(true);
            itemStack.setItemMeta(meta);
            player.getInventory().addItem(itemStack);
        }
    }

    @RequiredArgsConstructor
    public static class ArmourBuyable implements Buyable {
        private final ArmourType type;

        @Override
        public void giveItem(Player player, BedwarsArena arena, ShopItem shopItem) {
            ItemStack leggings = null;
            ItemStack boots = null;

            switch(type) {
                case CHAINMAIL:
                    leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                    boots = new ItemStack(Material.CHAINMAIL_BOOTS);
                case IRON:
                    leggings = new ItemStack(Material.IRON_LEGGINGS);
                    boots = new ItemStack(Material.IRON_BOOTS);
                case DIAMOND:
                    leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                    boots = new ItemStack(Material.DIAMOND_BOOTS);
            }

            ItemMeta leggingsMeta = leggings.getItemMeta();
            ItemMeta bootsMeta = boots.getItemMeta();

            leggingsMeta.spigot().setUnbreakable(true);
            bootsMeta.spigot().setUnbreakable(true);
            leggings.setItemMeta(leggingsMeta);
            boots.setItemMeta(bootsMeta);

            player.getInventory().setLeggings(leggings);
            player.getInventory().setLeggings(boots);
        }

        public static enum ArmourType {
            CHAINMAIL,
            IRON,
            DIAMOND;
        }
    }

    @RequiredArgsConstructor
    public static class SwordBuyable implements Buyable {
        private final SwordType type;

        @Override
        public void giveItem(Player player, BedwarsArena arena, ShopItem shopItem) {
            Material currentSword = getSword(player.getInventory());
            if(currentSword != null) {
                player.getInventory().remove(currentSword);
            }

            ItemStack sword = null;
            switch(type) {
                case WOOD:
                    sword = new ItemStack(Material.WOOD_SWORD);
                case STONE:
                    sword = new ItemStack(Material.STONE_SWORD);
                case IRON:
                    sword = new ItemStack(Material.IRON_SWORD);
                case DIAMOND:
                    sword = new ItemStack(Material.DIAMOND);
            }

            ItemMeta swordMeta = sword.getItemMeta();
            swordMeta.spigot().setUnbreakable(true);
            sword.setItemMeta(swordMeta);

            player.getInventory().addItem(sword);
        }

        public Material getSword(PlayerInventory inventory) {
            if(inventory.contains(Material.WOOD_SWORD)) {
                return Material.WOOD_SWORD;
            } else if(inventory.contains(Material.STONE_SWORD)) {
                return Material.STONE_SWORD;
            } else if(inventory.contains(Material.IRON_SWORD)) {
                return Material.IRON_SWORD;
            } else if(inventory.contains(Material.DIAMOND_SWORD)) {
                return Material.DIAMOND_SWORD;
            } else {
                return null;
            }
        }

        public static enum SwordType {
            WOOD,
            STONE,
            IRON,
            DIAMOND;
        }
    }

    @RequiredArgsConstructor
    public static class BowBuyable implements Buyable {
        private final int enchantLevel;
        private final boolean punchEnchant;
        private final int punchEnchantLevel;

        @Override
        public void giveItem(Player player, BedwarsArena arena, ShopItem shopItem) {
            ItemStack bow = new ItemStack(Material.BOW);

            ItemMeta meta = bow.getItemMeta();
            if(enchantLevel != 0) {
                meta.addEnchant(Enchantment.ARROW_DAMAGE, enchantLevel, false);
            }
            if(punchEnchant && punchEnchantLevel != 0) {
                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, enchantLevel, false);
            }
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);

            meta.spigot().setUnbreakable(true);
            bow.setItemMeta(meta);
            player.getInventory().addItem(bow, new ItemStack(Material.ARROW));
        }
    }
}
