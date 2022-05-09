package com.tabithastrong.pipes.block;

import com.tabithastrong.pipes.PipesMod;
import com.tabithastrong.pipes.block.entity.WoodenPipeBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class WoodenPipeBlock extends AbstractPipeBlock {
    public WoodenPipeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenPipeBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntityType getBlockEntityType() {
        return PipesMod.WOODEN_PIPE_BLOCK_ENTITY;
    }
}
