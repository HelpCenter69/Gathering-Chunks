/*
 * Original work Copyright (c) immortius
 * Modified work Copyright (c) 2026 Ryvione
 *
 * This file is part of Chunk By Chunk (Ryvione's Fork).
 * Original: https://github.com/immortius/chunkbychunk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for details.
 */

package com.ryvione.chunkbychunk.config;
import com.google.common.collect.ImmutableList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import com.ryvione.chunkbychunk.common.util.ChunkUtil;
import com.ryvione.chunkbychunk.interop.Services;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public enum ChunkRewardChestContent {
    ChunkSpawner(Services.PLATFORM::spawnChunkBlockItem),
    UnstableChunkSpawner(Services.PLATFORM::unstableChunkSpawnBlockItem),
    WorldCore(Services.PLATFORM::worldCoreBlockItem),
    WorldCrystal(Services.PLATFORM::worldCrystalItem),
    WorldForge(Services.PLATFORM::worldForgeBlockItem),
    WorldFragment(Services.PLATFORM::worldFragmentItem),
    Random(null) {
        // Main slot: Chunk spawners and world core - these are the primary progression items
        private static final List<RNGSlot> mainSlot = ImmutableList.of(
                new RNGSlot(Services.PLATFORM::spawnChunkBlockItem, 1, 1, 40),
                new RNGSlot(Services.PLATFORM::unstableChunkSpawnBlockItem, 1, 1, 50),
                new RNGSlot(Services.PLATFORM::worldCoreBlockItem, 1, 1, 10)
        );
        private static final int mainSlotRange = mainSlot.stream().mapToInt(x -> x.chance).sum();

        // Additional slots: Resources and very rare machines
        // Reduced quantities and chances for overpowered items
        private static final List<RNGSlot> additionalSlot = ImmutableList.of(
                // Common resources - higher chance, reasonable quantities
                new RNGSlot(Services.PLATFORM::worldFragmentItem, 1, 12, 70),
                new RNGSlot(Services.PLATFORM::worldShardItem, 1, 6, 40),

                // Uncommon resources
                new RNGSlot(Services.PLATFORM::worldCrystalItem, 1, 2, 20),

                // Rare progression items
                new RNGSlot(Services.PLATFORM::worldCoreBlockItem, 1, 1, 8),

                // Very rare machines - much lower chance, only 1 per drop
                // Players should work for these, not get them easily from chests
                new RNGSlot(Services.PLATFORM::worldForgeBlockItem, 1, 1, 1),
                new RNGSlot(Services.PLATFORM::worldScannerBlockItem, 1, 1, 1),
                new RNGSlot(Services.PLATFORM::worldMenderBlockItem, 1, 1, 1)
        );
        private static final int additionalSlotRange = additionalSlot.stream().mapToInt(x -> x.chance).sum();

        // Reduced additional chance to prevent chest spam
        private static final float additionalChance = 0.3f;
        // Maximum 6 additional items instead of 8
        private static final int maxAdditionalItems = 6;

        @Override
        public List<ItemStack> getItems(Random random, int quantity) {
            List<ItemStack> result = new ArrayList<>();

            // Main slot - always get one progression item
            int roll = random.nextInt(mainSlotRange);
            int mainSlotChance = 0;
            for (RNGSlot rngSlot : mainSlot) {
                mainSlotChance += rngSlot.chance;
                if (roll < mainSlotChance) {
                    ItemStack stack = rngSlot.item.get().getDefaultInstance();
                    stack.setCount(random.nextInt(rngSlot.minQuanity, rngSlot.maxQuantity + 1));
                    result.add(stack);
                    break;
                }
            }

            // Additional slots - random resources with diminishing returns
            do {
                roll = random.nextInt(additionalSlotRange);
                int addSlotChance = 0;
                for (RNGSlot rngSlot : additionalSlot) {
                    addSlotChance += rngSlot.chance;
                    if (roll < addSlotChance) {
                        ItemStack stack = rngSlot.item.get().getDefaultInstance();
                        stack.setCount(random.nextInt(rngSlot.minQuanity, rngSlot.maxQuantity + 1));
                        result.add(stack);
                        break;
                    }
                }
            } while (result.size() < maxAdditionalItems && random.nextFloat() < additionalChance);

            return result;
        }
    };

    private final Supplier<Item> item;

    ChunkRewardChestContent(Supplier<Item> item) {
        this.item = item;
    }

    public List<ItemStack> getItems(Random random, int quantity) {
        ItemStack result = item.get().getDefaultInstance();
        result.setCount(quantity);
        return Collections.singletonList(result);
    }

    private record RNGSlot(Supplier<Item> item, int minQuanity, int maxQuantity, int chance) {}
}