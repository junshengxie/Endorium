package com.endreborn.init;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
    TUNGSTEN(2, 260, 6.0F, 2.5F, 0, () -> {
        return Ingredient.of(ModItems.TUNGSTEN_INGOT.get());
    }),
    ENDORIUM(4, 1348, 8.0F, 4.0F, 12, () -> {
        return Ingredient.of(ModItems.ENDORIUM_INGOT.get());
    }),
    ENDORIUM_FLEXIBILITY(4, 1348, 9.5F, 4.0F, 12, () -> {
        return Ingredient.of(ModItems.ENDORIUM_INGOT.get());
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModTiers(int p_43332_, int p_43333_, float p_43334_, float p_43335_, int p_43336_, Supplier<Ingredient> p_43337_) {
        this.level = p_43332_;
        this.uses = p_43333_;
        this.speed = p_43334_;
        this.damage = p_43335_;
        this.enchantmentValue = p_43336_;
        this.repairIngredient = new LazyLoadedValue<>(p_43337_);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
