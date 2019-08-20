package com.github.atomishere.atombedwars.arena;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.Bed;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BedwarsArena {
    @Getter
    public final World arenaWorld;

    @Getter
    public final PlayerContainer playerOne;
    @Getter
    public final PlayerContainer playerTwo;

    @Getter
    private final List<Location> diamondGenerators;
    @Getter
    private final List<Location> emeraldGenerators;

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PlayerContainer {
        @Getter
        private final Player player;
        @Getter
        private final Island island;

        public static class Builder {
            private Player player = null;

            private ChatColor islandColor = null;
            private Location spawnLocation = null;
            private Location bedLocation = null;
            private Bed bed = null;

            private Location generator = null;

            private Builder() {
            }
            public static Builder newBuilder() {
                return new Builder();
            }

            public Builder setPlayer(Player player) {
                this.player = player;
                return this;
            }

            public Builder setIslandColor(ChatColor islandColor) {
                this.islandColor = islandColor;
                return this;
            }

            public Builder setSpawnLocation(Location spawnLocation) {
                this.spawnLocation = spawnLocation;
                return this;
            }

            public Builder setBedLocation(Location bedLocation) {
                this.bedLocation = bedLocation;
                return this;
            }

            public Builder setBed(Bed bed) {
                this.bed = bed;
                return this;
            }

            public Builder setGenerator(Location generator) {
                this.generator = generator;
                return this;
            }

            public PlayerContainer build() {
                if(player == null || spawnLocation == null || bedLocation == null || bed == null || generator == null) {
                    return null;
                }

                return new PlayerContainer(player, new Island(islandColor, spawnLocation, bedLocation, bed, generator));
            }
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Island {
        @Getter
        private final ChatColor islandColor;
        @Getter
        private final Location spawnLocation;
        @Getter
        private final Location bedLocation;
        @Getter
        private final Bed bed;

        @Getter
        private final Location generator;
    }

    public static class Builder {
        private World arenaWorld = null;

        private PlayerContainer playerOne = null;
        private PlayerContainer playerTwo = null;

        private List<Location> diamondGenerators = null;
        private List<Location> emeraldGenerators = null;

        private Builder() {
        }
        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setArenaWorld(World arenaWorld) {
            this.arenaWorld = arenaWorld;
            return this;
        }

        public Builder setPlayerOne(PlayerContainer playerOne) {
            this.playerOne = playerOne;
            return this;
        }
        public Builder setPlayerTwo(PlayerContainer playerTwo) {
            this.playerTwo = playerTwo;
            return this;
        }

        public Builder setDiamondGenerators(List<Location> diamondGenerators) {
            this.diamondGenerators = diamondGenerators;
            return this;
        }
        public Builder setEmeraldGenerators(List<Location> emeraldGenerators) {
            this.emeraldGenerators = emeraldGenerators;
            return this;
        }

        public BedwarsArena build() {
            if(arenaWorld == null || playerOne == null || playerTwo == null || diamondGenerators == null || emeraldGenerators == null) {
                return null;
            }

            return new BedwarsArena(arenaWorld, playerOne, playerTwo, diamondGenerators, emeraldGenerators);
        }
    }
}
