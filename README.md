# Gathering Chunks (Ryvione's Fork)

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen.svg)](https://www.minecraft.net/)
[![Mod Loader](https://img.shields.io/badge/Mod%20Loader-Fabric-blue.svg)](https://fabricmc.net/)
[![Mod Loader](https://img.shields.io/badge/NeoForge-1.21+-orange?style=for-the-badge)](https://neoforged.net)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Discord Server Link : https://discord.gg/3S9aKukmmJ

A Minecraft mod that generates the world chunk by chunk as you explore. This is a maintained fork of the original [Chunk By Chunk](https://github.com/immortius/chunkbychunk) by immortius.

## About This Fork

The original Chunk By Chunk is an excellent mod that deserves to be kept alive and updated. Since the original repository appears to be discontinued, I've decided to continue its development, fixing bugs, adding features, and ensuring compatibility with newer Minecraft versions.

## New Features & Improvements
- A lot of features were added in the mod, i will add all of them there soon.

## Features

- **World Expansion**: Start with a single chunk and expand it
- **Multiple Chunk Types**: Normal, unstable, and biome-themed spawners
- **Custom blocks**: World Forge, Scanner, and Mender blocks
- **Admin Commands**: Server management tools for multiplayer

## Building from Source
```bash
# Clone the repository
git clone https://github.com/ryvione/Gathering-Chunks.git
cd Gathering-Chunks

# Build the mod
./gradlew build

# The compiled JARS will be in Fabric/build/libs/ and NeoForge/build/libs
```

## Commands

| Command | Permission | Description |
|---------|-----------|-------------|
| `/chests` | Player | List all available chests in current dimension |
| `/chests tracker enable` | Player | Enable chest location notifications |
| `/chests tracker disable` | Player | Disable chest location notifications |
| `/spawnchunk` | Admin | Manually spawn a chunk |

## Roadmap

- [ ] Support for Minecraft 1.21.2+
- [X] NeoForge compatibility
- [X] Additional configuration options
- [X] Performance optimizations
- [ ] More biome-specific features
- [ ] Nether specific blocks
- [ ] Integration with popular mods
- [ ] Enhanced multiplayer features

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Setup

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Credits

**Original Author**: [immortius](https://github.com/immortius)  
**Fork Maintainer & Active Developer**: [Ryvione](https://github.com/Ryvione)

### Original Repository
The original mod can be found at: [github.com/immortius/chunkbychunk](https://github.com/immortius/chunkbychunk)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
```
Original work Copyright (c) immortius
Modified work Copyright (c) 2026 Ryvione
```

## Bug Reports

Found a bug? Please report it on our [Issues](../../issues) page with:
- Minecraft version
- Mod version
- Steps to reproduce
- Expected vs actual behavior
- Crash logs (if applicable)

## Community

- **Issues**: [GitHub Issues](../../issues)
- **Discussions**: [GitHub Discussions](../../discussions)
- **Download (Modrinth)**: [Modrinth](https://modrinth.com/mod/gathering-chunks)
- **Download (CurseForge)**: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/gathering-chunks)

---

*If you enjoy this mod, please consider starring the repository!* ⭐
