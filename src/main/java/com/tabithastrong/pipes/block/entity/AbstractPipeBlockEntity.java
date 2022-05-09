package com.tabithastrong.pipes.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.Pipe;

public class AbstractPipeBlockEntity extends BlockEntity {
    public class PipeItemStack {
        public ItemStack stack;
        public Direction from;
        public Direction to;
        public float progress;

        public PipeItemStack(ItemStack stack, Direction from, Direction to, float progress) {
            this.stack = stack;
            this.from = from;
            this.to = to;
            this.progress = progress;
        }

        public PipeItemStack(NbtCompound nbt) {
            this(ItemStack.fromNbt(nbt.getCompound("ItemStack")), Direction.values()[nbt.getInt("From")], Direction.values()[nbt.getInt("To")], nbt.getFloat("Progress"));
        }

        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();
            NbtCompound itemStack = this.stack.writeNbt(new NbtCompound());
            nbt.put("ItemStack", itemStack);
            nbt.putInt("From", from.getId());
            nbt.putInt("To", to.getId());
            nbt.putFloat("Progress", progress);

            return nbt;
        }
    }

    public DefaultedList<PipeItemStack> itemsInTransit;

    public AbstractPipeBlockEntity(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        itemsInTransit = DefaultedList.of();
        itemsInTransit.add(new PipeItemStack(new ItemStack(Items.RAIL, 1), Direction.NORTH, Direction.EAST, 0f));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        NbtList list = new NbtList();
        for(int i = 0; i < itemsInTransit.size(); i++) {
            list.add(itemsInTransit.get(i).toNbt());
        }
        nbt.put("Items", list);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        itemsInTransit = DefaultedList.of();
        NbtList list = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < list.size(); i++) {
            itemsInTransit.add(new PipeItemStack(list.getCompound(i)));
        }

    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void tick() {
        for(PipeItemStack s : itemsInTransit) {
            s.progress = Math.min(1f, s.progress + 1f/20f);

            if(s.progress > 1f) {
                if(!world.isClient) {
                    BlockEntity entity = world.getBlockEntity(pos.offset(s.to));

                    if(entity != null && entity instanceof Inventory) {
                        Inventory inv = (Inventory) entity;
                        for(int i = 0; i < inv.size(); i++) {
                            ItemStack j = inv.getStack(i);

                            GenericContainerScreenHandler

                            if(j.isEmpty()) {
                                inv.setStack(i, s.stack.copy());
                                entity.markDirty();
                                markDirty();
                                break;
                            } else if(j.isItemEqual(s.stack) && j.getCount() < j.getMaxCount()) {
                                int amountCanFit = j.getMaxCount() - j.getCount();

                                if(s.stack.getCount() < amountCanFit) {
                                    j.increment(s.stack.getCount());
                                    inv.setStack(i, j);
                                    break;
                                } else {
                                    j.increment(amountCanFit);
                                    s.stack.decrement(amountCanFit);
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity be) {
        ((AbstractPipeBlockEntity) be).tick();
    }
}
