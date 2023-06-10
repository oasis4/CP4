package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.SpawnController;
import com.github.oasis.craftprotect.utils.MoveUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Singleton
public class SpawnElytraFeature implements Feature {
    private static final int radius = (int) Math.pow(25, 2);

    private final CraftProtectPlugin protect;
    private final NamespacedKey key;

    @Inject
    private SpawnController controller;

    @Inject
    public SpawnElytraFeature(CraftProtectPlugin protect) {
        this.protect = protect;
        this.key = NamespacedKey.fromString("chestplate-vault", protect);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer container = player.getPersistentDataContainer();
        PlayerInventory inventory = player.getInventory();

        // Check has saved chestplate and then restore
        byte[] bytes = container.get(key, PersistentDataType.BYTE_ARRAY);
        if (bytes != null) {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
                BukkitObjectInputStream inputStream = new BukkitObjectInputStream(stream);
                ItemStack savedItem = (ItemStack) inputStream.readObject();
                container.remove(key);
                inventory.setChestplate(savedItem);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Location spawnLocation = controller.getLocation();
        if (spawnLocation == null) return;

        // Check spawn radius
        if (player.getLocation().distanceSquared(spawnLocation) > radius) return;


        // Save chestplate
        ItemStack chestplate = inventory.getChestplate();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(stream);
            outputStream.writeObject(chestplate);
            player.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, stream.toByteArray());
            ItemStack itemStack = new ItemStack(Material.ELYTRA);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(1);
            itemStack.setItemMeta(itemMeta);
            inventory.setChestplate(itemStack);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer container = player.getPersistentDataContainer();
        PlayerInventory inventory = player.getInventory();

        // Check has saved chestplate and then restore
        byte[] bytes = container.get(key, PersistentDataType.BYTE_ARRAY);
        if (bytes != null) {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
                BukkitObjectInputStream inputStream = new BukkitObjectInputStream(stream);
                ItemStack savedItem = (ItemStack) inputStream.readObject();
                container.remove(key);
                inventory.setChestplate(savedItem);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onElytraEnd(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (!container.has(key, PersistentDataType.BYTE_ARRAY)) return;

        Location spawnLocation = controller.getLocation();
        if (spawnLocation == null) // TODO: Restore if not set
            return;

        if (event.isGliding() || player.getLocation().distanceSquared(spawnLocation) <= radius) return;

        PlayerInventory inventory = player.getInventory();
        // Check has saved chestplate and then restore
        byte[] bytes = container.get(key, PersistentDataType.BYTE_ARRAY);
        if (bytes == null) return;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
            BukkitObjectInputStream inputStream = new BukkitObjectInputStream(stream);
            ItemStack savedItem = (ItemStack) inputStream.readObject();
            container.remove(key);
            inventory.setChestplate(savedItem);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!MoveUtils.movedBlock(event.getFrom(), event.getTo()))
            return;
        Player player = event.getPlayer();
        PersistentDataContainer container = player.getPersistentDataContainer();

        Location spawnLocation = controller.getLocation();
        if (spawnLocation == null) // TODO: Restore if not set
            return;

        if (!container.has(key, PersistentDataType.BYTE_ARRAY)) {
            PlayerInventory inventory = player.getInventory();
            if (player.getLocation().distanceSquared(spawnLocation) <= radius) {
                // Save chestplate
                ItemStack chestplate = inventory.getChestplate();
                try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                    BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(stream);
                    outputStream.writeObject(chestplate);
                    player.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, stream.toByteArray());
                    ItemStack itemStack = new ItemStack(Material.ELYTRA);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setCustomModelData(1);
                    itemStack.setItemMeta(itemMeta);
                    inventory.setChestplate(itemStack);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        if (player.isGliding() || player.getLocation().distanceSquared(spawnLocation) <= radius)
            return;

        PlayerInventory inventory = player.getInventory();
        // Check has saved chestplate and then restore
        byte[] bytes = container.get(key, PersistentDataType.BYTE_ARRAY);
        if (bytes == null) return;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
            BukkitObjectInputStream inputStream = new BukkitObjectInputStream(stream);
            ItemStack savedItem = (ItemStack) inputStream.readObject();
            container.remove(key);
            inventory.setChestplate(savedItem);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PersistentDataContainer container = player.getPersistentDataContainer();

        // Check has saved chestplate and then restore
        byte[] bytes = container.get(key, PersistentDataType.BYTE_ARRAY);
        if (bytes != null) {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
                BukkitObjectInputStream inputStream = new BukkitObjectInputStream(stream);
                ItemStack savedItem = (ItemStack) inputStream.readObject();
                container.remove(key);
                event.getDrops().removeIf(itemStack -> itemStack.getType() == Material.ELYTRA && itemStack.getItemMeta() != null && itemStack.getItemMeta().getCustomModelData() == 1 || itemStack.getType() == Material.AIR);
                event.getDrops().add(savedItem);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        PersistentDataContainer container = whoClicked.getPersistentDataContainer();
        if (!container.has(key, PersistentDataType.BYTE_ARRAY)) return;

        if (!whoClicked.getInventory().equals(event.getClickedInventory())) return;
        if (event.getSlotType() != InventoryType.SlotType.ARMOR) return;
        if (event.getRawSlot() != 6) // Chestplate slot (https://wiki.vg/File:Inventory-slots.png)
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryCreativeClick(InventoryCreativeEvent event) {
        onInventoryClick(event);
        if (event.isCancelled()) {
            event.setCursor(null);
        }
    }


    @Override
    public void close() throws IOException {

    }
}
