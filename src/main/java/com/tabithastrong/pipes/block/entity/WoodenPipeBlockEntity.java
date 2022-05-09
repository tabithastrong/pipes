package com.tabithastrong.pipes.block.entity;

import com.tabithastrong.pipes.PipesMod;
import com.tabithastrong.pipes.block.entity.AbstractPipeBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class WoodenPipeBlockEntity extends AbstractPipeBlockEntity {
    public WoodenPipeBlockEntity(BlockPos pos, BlockState state) {
        super(PipesMod.WOODEN_PIPE_BLOCK_ENTITY, pos, state);
    }
}
