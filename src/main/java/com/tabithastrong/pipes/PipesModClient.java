package com.tabithastrong.pipes;

import com.tabithastrong.pipes.client.render.PipeBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipesModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(PipesMod.WOODEN_PIPE, RenderLayer.getCutout());

		BlockEntityRendererRegistry.register(PipesMod.WOODEN_PIPE_BLOCK_ENTITY, PipeBlockEntityRenderer::new);
	}
}
