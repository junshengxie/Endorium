package com.endreborn.init;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModTiers implements ToolMaterial {
    TUNGSTEN(2, 260, 6.0F, 2.5F, 0, () -> {
        return Ingredient.ofItems(ModItems.TUNGSTEN_INGOT);
    }),
    ENDORIUM(4, 1348, 8.0F, 4.0F, 12, () -> {
        return Ingredient.ofItems(ModItems.ENDORIUM_INGOT);
    }),
    ENDORIUM_FLEXIBILITY(4, 1348, 9.5F, 4.0F, 12, () -> {
        return Ingredient.ofItems(ModItems.ENDORIUM_INGOT);
    });
    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Lazy<Ingredient> repairIngredient;

    private ModTiers(int p_43332_, int p_43333_, float p_43334_, float p_43335_, int p_43336_, Supplier repairIngredient) {
        this.level = p_43332_;
        this.uses = p_43333_;
        this.speed = p_43334_;
        this.damage = p_43335_;
        this.enchantmentValue = p_43336_;
        this.repairIngredient = new Lazy(repairIngredient);
    }

    public int getDurability() {
        return this.uses;
    }

    public float getMiningSpeedMultiplier() {
        return this.speed;
    }

    public float getAttackDamage() {
        return this.damage;
    }

    public int getMiningLevel() {
        return this.level;
    }

    public int getEnchantability() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredient.get();
    }
}
