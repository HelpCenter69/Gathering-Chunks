/*
 * Original work Copyright (c) immortius
 * Modified work Copyright (c) 2026 Ryvione
 *
 * This file is part of Chunk By Chunk (Ryvione's Fork).
 * Original: https://github.com/immortius/chunkbychunk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for details.
 */

package com.ryvione.chunkbychunk.server.world;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
public class ChestTracker extends SavedData {
    private final Set<BlockPos> chestPositions = new HashSet<>();
    private final Map<UUID, Boolean> playerTrackerEnabled = new HashMap<>();
    private final MinecraftServer server;
    public static ChestTracker get(MinecraftServer server) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) {
            return new ChestTracker(server);
        }
        return overworld.getChunkSource().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        () -> new ChestTracker(server),
                        (tag, provider) -> ChestTracker.load(server, tag, provider),
                        DataFixTypes.LEVEL
                ),
                "chunkbychunk_chest_tracker"
        );
    }
    private static ChestTracker load(MinecraftServer server, CompoundTag tag, HolderLookup.Provider provider) {
        ChestTracker tracker = new ChestTracker(server);
        ListTag positionsTag = tag.getList("chests", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < positionsTag.size(); i++) {
            CompoundTag posTag = positionsTag.getCompound(i);
            int x = posTag.getInt("x");
            int y = posTag.getInt("y");
            int z = posTag.getInt("z");
            tracker.chestPositions.add(new BlockPos(x, y, z));
        }
        ListTag playerSettingsTag = tag.getList("playerSettings", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < playerSettingsTag.size(); i++) {
            CompoundTag playerTag = playerSettingsTag.getCompound(i);
            UUID playerUUID = playerTag.getUUID("uuid");
            boolean enabled = playerTag.getBoolean("enabled");
            tracker.playerTrackerEnabled.put(playerUUID, enabled);
        }
        return tracker;
    }
    private ChestTracker(MinecraftServer server) {
        this.server = server;
    }
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag positionsTag = new ListTag();
        for (BlockPos pos : chestPositions) {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            positionsTag.add(posTag);
        }
        tag.put("chests", positionsTag);
        ListTag playerSettingsTag = new ListTag();
        for (Map.Entry<UUID, Boolean> entry : playerTrackerEnabled.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("uuid", entry.getKey());
            playerTag.putBoolean("enabled", entry.getValue());
            playerSettingsTag.add(playerTag);
        }
        tag.put("playerSettings", playerSettingsTag);
        return tag;
    }
    public void addChest(BlockPos pos) {
        chestPositions.add(pos.immutable());
        setDirty();
    }
    public void removeChest(BlockPos pos) {
        if (chestPositions.remove(pos)) {
            setDirty();
        }
    }
    public Set<BlockPos> getChestPositions() {
        return new HashSet<>(chestPositions);
    }
    public void checkAndRemoveIfEmpty(BlockPos pos, ServerLevel level) {
        if (!chestPositions.contains(pos)) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RandomizableContainerBlockEntity chest) {
            boolean isEmpty = true;
            for (int i = 0; i < chest.getContainerSize(); i++) {
                if (!chest.getItem(i).isEmpty()) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                removeChest(pos);
            }
        } else {
            removeChest(pos);
        }
    }
    public boolean isTracked(BlockPos pos) {
        return chestPositions.contains(pos);
    }
    public void setTrackerEnabled(UUID playerUUID, boolean enabled) {
        playerTrackerEnabled.put(playerUUID, enabled);
        setDirty();
    }
    public boolean isTrackerEnabled(UUID playerUUID) {
        return playerTrackerEnabled.getOrDefault(playerUUID, true);
    }
}
