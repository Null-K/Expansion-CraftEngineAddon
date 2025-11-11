# CraftEngine Addon PlaceholderAPI (CEA) Placeholders

> **Prerequisite Plugin:** [CraftEngine](https://modrinth.com/plugin/craftengine)

This document describes the placeholders provided by the CraftEngineAddon (CEA) extension.

| Placeholder | Description |
|------------|-------------|
| `%cea_item_is_custom%` | Returns whether the item in the player's main hand is a custom item. |
| `%cea_item_key%` | Returns the custom ID of the item in the player's main hand. |
| `%cea_item_has_<key>_1%` | Checks whether the player has a specified amount of the custom item. Replace `{key}` with the custom item ID. |
| `%cea_item_count_<key>%` | Counts the total number of a specific custom item in the player's inventory. Replace `{key}` with the custom item ID. |
| `%cea_block_is_custom%` | Returns whether the targeted block is a custom block. |
| `%cea_entity_is_furniture%` | Returns whether the targeted entity is a furniture entity. |
| `%cea_entity_furniture_key%` | Returns the custom ID of the targeted furniture entity. |
