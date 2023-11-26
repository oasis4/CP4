package com.github.oasis.craftprotect.feature;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Reciploader {

    public void registerRecipes() {
        ItemStack teleportstick = new ItemStack(Material.BARRIER);
        ItemMeta teleportstickmeta = teleportstick.getItemMeta();
        teleportstickmeta.setDisplayName("§cTeleport Stick");
        teleportstick.setItemMeta(teleportstickmeta);
        ShapedRecipe teleportstickrecipe = new ShapedRecipe(teleportstick);
        teleportstickrecipe.shape("GDG", "DED", "GDG");
        teleportstickrecipe.setIngredient('G', Material.GLASS);
        teleportstickrecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        teleportstickrecipe.setIngredient('E', Material.ENDER_PEARL);


        Bukkit.addRecipe(teleportstickrecipe);



    }

}
