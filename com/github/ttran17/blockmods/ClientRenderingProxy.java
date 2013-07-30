package com.github.ttran17.blockmods;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import com.github.ttran17.blockmods.tv.TVBlock;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientRenderingProxy extends RenderingProxy implements ISimpleBlockRenderingHandler {
	
	@Override
	public void registerRenderers() {
		TVBlock.renderType = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(TVBlock.renderType, this);
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		// TODO Auto-generated method stub	
	}

	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (modelId == TVBlock.renderType) {
			return ((TVBlock) block).render(world, x, y, z, renderer);
		}
		
		return false;
	}
	
}
