package com.endreborn.content;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradableShovelItem extends ShovelItem {

    private final int sharpness;
    private final int flexibility;

    public UpgradableShovelItem(Tier p_43114_, float p_43115_, float p_43116_, Properties p_43117_, int sharpness, int flexibility) {
        super(p_43114_, p_43115_, p_43116_, p_43117_);
        this.sharpness = sharpness;
        this.flexibility = flexibility;
    }
    public Component getName(ItemStack p_41458_) {
        return Component.translatable("item.endreborn.endorium_shovel");
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.relic").withStyle(ChatFormatting.GRAY));
        if (this.sharpness > 0) {
            tooltip.add(Component.translatable("tooltip.shovel_sharpness").withStyle(ChatFormatting.DARK_GRAY));
        } else if (this.flexibility > 0){
            tooltip.add(Component.translatable("tooltip.uni_flexibility").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.translatable("tooltip.uni_flexibility_n").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public boolean hurtEnemy(ItemStack p_40994_, LivingEntity p_40995_, LivingEntity p_40996_) {
        p_40994_.hurtAndBreak(2 + this.flexibility, p_40996_, (p_41007_) -> {
            p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext p_43119_) {
        Level level = p_43119_.getLevel();
        BlockPos blockpos = p_43119_.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (p_43119_.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = p_43119_.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(p_43119_, net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN, false);
            BlockState blockstate2 = null;
            if (blockstate1 != null && level.isEmptyBlock(blockpos.above())) {
                level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!level.isClientSide()) {
                    level.levelEvent((Player) null, 1009, blockpos, 0);
                }

                CampfireBlock.dowse(p_43119_.getPlayer(), level, blockpos, blockstate);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
            }

            if (blockstate2 != null) {
                if (!level.isClientSide) {
                    level.setBlock(blockpos, blockstate2, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate2));
                    if (player != null) {
                        p_43119_.getItemInHand().hurtAndBreak(1 - this.sharpness, player, (p_43122_) -> {
                            p_43122_.broadcastBreakEvent(p_43119_.getHand());
                        });
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public float getSharpness() {
        return this.sharpness;
    }
    public float getFlexibility() {
        return this.flexibility;
    }
}
