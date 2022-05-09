package com.tabithastrong.pipes.client.render;

import com.tabithastrong.pipes.block.entity.AbstractPipeBlockEntity;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class PipeBlockEntityRenderer<T extends AbstractPipeBlockEntity> implements BlockEntityRenderer<T> {

    public PipeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

    }
    @Override
    public void render(AbstractPipeBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        DefaultedList<AbstractPipeBlockEntity.PipeItemStack> items = blockEntity.itemsInTransit;

        for(AbstractPipeBlockEntity.PipeItemStack item : items) {
            matrices.push();

            Vec3f unit = item.from.getUnitVector();

            if(item.progress < 0.5f) {
                Vec3f from = item.from.getUnitVector();
                Vec3f to = Vec3f.ZERO;

                unit = new Vec3f(MathHelper.lerp(item.progress * 2f, from.getX(), to.getX()),
                        MathHelper.lerp(item.progress * 2f, from.getY(), to.getY()),
                        MathHelper.lerp(item.progress * 2f, from.getZ(), to.getZ()));
            } else {
                Vec3f from = Vec3f.ZERO;
                Vec3f to = item.to.getUnitVector();

                unit = new Vec3f(MathHelper.lerp((item.progress * 2f) - 1f, from.getX(), to.getX()),
                        MathHelper.lerp((item.progress * 2f) - 1f, from.getY(), to.getY()),
                        MathHelper.lerp((item.progress * 2f) - 1f, from.getZ(), to.getZ()));
            }

            matrices.translate(0.5f, 0.425f, 0.5f);
            matrices.translate(unit.getX() / 2f, 0f, unit.getZ() / 2f);
            matrices.scale(0.35f, 0.35f, 0.35f);


            MinecraftClient.getInstance().getItemRenderer().renderItem(item.stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);

            matrices.pop();}
    }
}
