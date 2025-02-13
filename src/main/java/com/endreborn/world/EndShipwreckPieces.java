package com.endreborn.world;

import com.endreborn.EndReborn;
import com.endreborn.init.ModPieces;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Map;
import java.util.Random;

public class EndShipwreckPieces {
    public static final ResourceLocation END_SHIPWRECK_LOOT = new ResourceLocation(EndReborn.MODID, "chests/end_shipwreck");
    private static final ResourceLocation[] STRUCTURES = new ResourceLocation[]{new ResourceLocation(EndReborn.MODID + ":end_shipwreck"), new ResourceLocation(EndReborn.MODID + ":end_beacon"), new ResourceLocation(EndReborn.MODID + ":end_shipruin")};

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation rotation, StructurePieceAccessor pieceList, Structure.GenerationContext generationContext, RandomSource random) {
        pieceList.addPiece(new EndShipwreckPieces.Piece(manager, (ResourceLocation) Util.getRandom(STRUCTURES, random), pos, rotation));
    }
    public static class Piece extends TemplateStructurePiece {
        public Piece(StructureTemplateManager manager, ResourceLocation resourceLocation, BlockPos position, Rotation rotation) {
            super(ModPieces.END_SHIPWRECK, 0, manager, resourceLocation, resourceLocation.toString(), loadTemplate(manager, resourceLocation, rotation), position.offset(0, -4, 0));
        }

        public Piece(StructurePieceSerializationContext serializationContext, CompoundTag compoundNBT) {
            super(ModPieces.END_SHIPWRECK, compoundNBT, serializationContext.structureTemplateManager(), (placementSettings) -> {
                ResourceLocation templateLocation = new ResourceLocation(compoundNBT.getString("Template"));
                Rotation rotation = Rotation.valueOf(compoundNBT.getString("Rot"));
                return loadTemplate(serializationContext.structureTemplateManager(), templateLocation, rotation);
            });
        }

        private static StructurePlaceSettings loadTemplate(StructureTemplateManager manager, ResourceLocation resourceLocation, Rotation rotation) {
            StructureTemplate template = manager.getOrCreate(resourceLocation);

            BlockPos pivot = new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot(pivot).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }
        protected void addAdditionalSaveData(StructurePieceSerializationContext serializationContext, CompoundTag compoundNBT) {
            super.addAdditionalSaveData(serializationContext, compoundNBT);
            compoundNBT.putString("Rot", this.placeSettings.getRotation().name());
        }

        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, RandomSource rand, BoundingBox sbb) {
            if (function.startsWith("Chest")) {
                BlockPos blockpos = pos.below();
                if (sbb.isInside(blockpos)) {
                    RandomizableContainerBlockEntity.setLootTable(worldIn, rand, blockpos, EndShipwreckPieces.END_SHIPWRECK_LOOT);
                }
            }
        }
    }
}