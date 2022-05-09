package com.tabithastrong.pipes;

import com.tabithastrong.pipes.block.WoodenPipeBlock;
import com.tabithastrong.pipes.block.entity.WoodenPipeBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipesMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("pipes_mod");

	public static final Block WOODEN_PIPE = new WoodenPipeBlock(FabricBlockSettings.of(Material.GLASS, MapColor.BLACK).breakInstantly().hardness(1f).resistance(10f).nonOpaque());
	public static final Identifier WOODEN_PIPE_IDENTIFIER = new Identifier("pipes_mod", "wooden_pipe");
	public static BlockEntityType<WoodenPipeBlockEntity> WOODEN_PIPE_BLOCK_ENTITY;

	@Override
	public void onInitialize() {
		ServerPlayerEntity
		LOGGER.info("What do we want? Pipes! When do we want 'em? Now!");
		Registry.register(Registry.BLOCK, WOODEN_PIPE_IDENTIFIER, WOODEN_PIPE);
		Registry.register(Registry.ITEM, WOODEN_PIPE_IDENTIFIER, new BlockItem(WOODEN_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		WOODEN_PIPE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, WOODEN_PIPE_IDENTIFIER, FabricBlockEntityTypeBuilder.create(WoodenPipeBlockEntity::new, WOODEN_PIPE).build(null));
	}
}
