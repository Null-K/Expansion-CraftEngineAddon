package com.puddingkc;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks;
import net.momirealms.craftengine.bukkit.api.CraftEngineFurniture;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.entity.furniture.Furniture;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftEngineAddon extends PlaceholderExpansion {

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("CraftEngine") != null && Bukkit.getPluginManager().getPlugin("CraftEngine").isEnabled();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cea";
    }

    @Override
    public @NotNull String getAuthor() {
        return "PuddingKC";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.1";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null || !player.isOnline() || params.isEmpty()) { return null; }

        if (params.toLowerCase().startsWith("item_has_")) {
            return getReturn(handleHasItemPlaceholder(player, params));
        }

        if (params.toLowerCase().startsWith("item_count_")) {
            return handleCountItemPlaceholder(player, params);
        }

        return switch (params.toLowerCase()) {
            case "item_is_custom" -> getReturn(CraftEngineItems.isCustomItem(player.getInventory().getItemInMainHand()));
            case "item_key" -> {
                Key key = CraftEngineItems.getCustomItemId(player.getInventory().getItemInMainHand());
                yield key != null ? key.toString() : null;
            }
            case "block_is_custom" -> getReturn(handleIsCustomBlockPlaceholder(player));
            case "entity_is_furniture" -> getReturn(handleIsFurniturePlaceholder(player));
            case "entity_furniture_key" -> handleFurnitureKeyPlaceholder(player);
            default -> null;
        };
    }

    private String handleFurnitureKeyPlaceholder(Player player) {
        Entity entity = player.getTargetEntity(5);
        if (entity == null) { return null; }

        Furniture furniture = CraftEngineFurniture.getLoadedFurnitureByBaseEntity(entity);
        if (furniture == null) { return null; }

        return furniture.id().asString();
    }

    private boolean handleIsFurniturePlaceholder(Player player) {
        Entity entity = player.getTargetEntity(5);
        if (entity == null) { return false; }

        Furniture furniture = CraftEngineFurniture.getLoadedFurnitureByBaseEntity(entity);
        return furniture != null;
    }

    private boolean handleIsCustomBlockPlaceholder(Player player) {
        Block block = player.getTargetBlockExact(5);
        if (block == null) { return false; }

        return CraftEngineBlocks.isCustomBlock(block);
    }

    private boolean handleHasItemPlaceholder(Player player, String params) {
        int start = params.indexOf('<');
        int end = params.indexOf('>');

        if (start == -1 || end == -1 || end <= start + 1) { return false; }

        String itemId = params.substring(start + 1, end);
        String amountStr = params.substring(end + 1);

        int amount = 1;
        if (amountStr.startsWith("_")) {
            try {
                amount = Integer.parseInt(amountStr.substring(1));
                amount = Math.max(1, amount);
            } catch (NumberFormatException ignored) {}
        }

        return hasItem(player, itemId, amount);
    }

    private boolean hasItem(Player player, String itemId, int amount) {
        CustomItem<ItemStack> customItem = CraftEngineItems.byId(Key.from(itemId));
        if (customItem == null) { return false; }

        ItemStack target = customItem.buildItemStack();
        int total = 0;

        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null) { continue; }
            if (content.isSimilar(target)) {
                total += content.getAmount();
                if (total >= amount) { return true; }
            }
        }
        return false;
    }

    private String handleCountItemPlaceholder(Player player, String params) {
        int start = params.indexOf('<');
        int end = params.indexOf('>');

        if (start == -1 || end == -1 || end <= start + 1) { return "0"; }

        String itemId = params.substring(start + 1, end);
        int count = countItem(player, itemId);
        return String.valueOf(count);
    }

    private int countItem(Player player, String itemId) {
        CustomItem<ItemStack> customItem = CraftEngineItems.byId(Key.from(itemId));
        if (customItem == null) { return 0; }

        ItemStack target = customItem.buildItemStack();
        int total = 0;

        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null) { continue; }
            if (content.isSimilar(target)) {
                total += content.getAmount();
            }
        }
        return total;
    }

    private String getReturn(boolean bool) {
        return bool ? "true" : "false";
    }
}