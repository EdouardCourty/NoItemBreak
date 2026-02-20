# NoItemBreak

[![build](https://github.com/EdouardCourty/NoItemBreak/actions/workflows/build.yml/badge.svg)](https://github.com/EdouardCourty/NoItemBreak/actions/workflows/build.yml)

A client-side Fabric mod for Minecraft 1.21.11 that prevents you from accidentally breaking your tools and weapons.

When an item in your main hand reaches **1 durability**, NoItemBreak blocks any action that would destroy it (attacking, mining, using). A warning message is shown in your action bar.

## Features

- 🛡️ Blocks attacks, mining and item use when durability is critically low
- ⚙️ Configurable list of protected items
- 💾 Config persists between sessions
- 🖥️ In-game config screen (via [ModMenu](https://modrinth.com/mod/modmenu), optional)
- 💬 Commands to manage the list without leaving the game

## Requirements

| Dependency           | Version  |
|----------------------|----------|
| Minecraft            | 1.21.11  |
| Fabric Loader        | ≥ 0.18.4 |
| Fabric API           | any      |
| ModMenu *(optional)* | ≥ 17.0.0 |

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) and [Fabric API](https://modrinth.com/mod/fabric-api)
2. Drop `noitembreak-<version>.jar` into your `.minecraft/mods/` folder
3. Launch the game

## Configuration

### Via ModMenu

Open **Options → Mods → NoItemBreak → Config** to manage the list of protected items.

### Via commands

```
/noitembreak add <item_id>       — Add an item (with autocomplete)
/noitembreak remove <item_id>    — Remove an item
/noitembreak list                — Show all protected items
```

Example:
```
/noitembreak add minecraft:netherite_sword
```

### Config file

The config is saved at `.minecraft/config/noitembreak.json`:

```json
{
  "enabled": true,
  "protectedItems": [
    "minecraft:diamond_sword",
    "minecraft:netherite_pickaxe"
  ]
}
```

## Default protected items

All diamond and netherite tools and weapons are protected by default:
`diamond_sword`, `diamond_pickaxe`, `diamond_axe`, `diamond_shovel`, `diamond_hoe`,
`netherite_sword`, `netherite_pickaxe`, `netherite_axe`, `netherite_shovel`, `netherite_hoe`

## License

[CC0-1.0](LICENSE) — public domain
