package com.tabithastrong.pipes.block;

import com.tabithastrong.pipes.block.entity.AbstractPipeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPipeBlock extends BlockWithEntity {
    public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.of("north");
    public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty CONNECTED_EAST = BooleanProperty.of("east");
    public static final BooleanProperty CONNECTED_WEST = BooleanProperty.of("west");
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.of("up");
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.of("down");

    public static final VoxelShape MIDDLE_SHAPE = VoxelShapes.cuboid(0.375D, 0.375D, 0.375D, 0.625D, 0.625D, 0.625D);
    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.cuboid(0.375D, 0D, 0.375D, 0.625D, 0.375D, 0.625D);

    public AbstractPipeBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(CONNECTED_NORTH, false).with(CONNECTED_SOUTH, false).with(CONNECTED_EAST, false).with(CONNECTED_WEST, false).with(CONNECTED_UP, false).with(CONNECTED_DOWN, false));
    }

    public BooleanProperty propertyForDirection(Direction direction) {
        switch(direction) {
            case SOUTH: return CONNECTED_SOUTH;
            case EAST: return CONNECTED_EAST;
            case WEST: return CONNECTED_WEST;
            case UP: return CONNECTED_UP;
            case DOWN: return CONNECTED_DOWN;
            default: return CONNECTED_NORTH;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_EAST, CONNECTED_WEST, CONNECTED_UP, CONNECTED_DOWN);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);

        for(Direction d : Direction.values()) {
            state = state.with(propertyForDirection(d), canConnectToBlock(ctx.getWorld(), ctx.getBlockPos().offset(d)));
        }

        return state;
    }

    public boolean canConnectToBlock(WorldAccess world, BlockPos pos) {
        BlockState neighbour = world.getBlockState(pos);
        boolean hasInventory = neighbour.hasBlockEntity() && world.getBlockEntity(pos) instanceof Inventory;

        if(neighbour.getBlock() instanceof AbstractPipeBlock || hasInventory) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state.with(propertyForDirection(direction), canConnectToBlock(world, neighborPos));
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = MIDDLE_SHAPE;

        if(state.get(CONNECTED_DOWN).booleanValue()) {
            shape = VoxelShapes.union(shape, SOUTH_SHAPE);
        }
        return shape;
    }

    @Nullable
    @Override
    public abstract BlockEntity createBlockEntity(BlockPos pos, BlockState state);

    public abstract BlockEntityType getBlockEntityType();

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(getBlockEntityType(), type, (world1, pos, state1, be) -> AbstractPipeBlockEntity.tick(world1, pos, state1, be));
    }
}
