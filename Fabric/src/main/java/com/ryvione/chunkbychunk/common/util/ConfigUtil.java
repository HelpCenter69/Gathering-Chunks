/*
 * Original work Copyright (c) immortius
 * Modified work Copyright (c) 2026 Ryvione
 *
 * This file is part of Chunk By Chunk (Ryvione's Fork).
 * Original: https://github.com/immortius/chunkbychunk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for details.
 */

package com.ryvione.chunkbychunk.common.util;
import com.ryvione.chunkbychunk.common.ChunkByChunkConstants;
import com.ryvione.chunkbychunk.config.ChunkByChunkConfig;
import com.ryvione.chunkbychunk.config.system.ConfigSystem;
import java.nio.file.Paths;
public final class ConfigUtil {
    private ConfigUtil() {
    }
    private static final ConfigSystem system = new ConfigSystem();
    public static void loadDefaultConfig() {
        synchronized (system) {
            system.synchConfig(Paths.get(ChunkByChunkConstants.DEFAULT_CONFIG_PATH).resolve(ChunkByChunkConstants.CONFIG_FILE), ChunkByChunkConfig.get());
        }
    }
    public static void saveDefaultConfig() {
        synchronized (system) {
            system.write(Paths.get(ChunkByChunkConstants.DEFAULT_CONFIG_PATH).resolve(ChunkByChunkConstants.CONFIG_FILE), ChunkByChunkConfig.get());
        }
    }
}
