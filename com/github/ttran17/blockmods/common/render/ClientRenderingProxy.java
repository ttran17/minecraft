package com.github.ttran17.blockmods.common.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientRenderingProxy extends RenderingProxy implements ISimpleBlockRenderingHandler {

	private final List<IBlockRenderer> rendererList = new ArrayList<IBlockRenderer>();
	private final Map<Integer, IBlockRenderer> rendererMap = new HashMap<Integer, IBlockRenderer>();
	
	@Override 
	public void addRenderers(IBlockRenderer renderer) {
		rendererList.add(renderer);
	}
	
	@Override 
	public void registerRenderers() {
		for (IBlockRenderer renderer : rendererList) {
			int renderType = RenderingRegistry.getNextAvailableRenderId();
			renderer.setRenderType(renderType);
			rendererMap.put(renderType, renderer);
			RenderingRegistry.registerBlockHandler(renderType, this);
		}
	}
	
	/**
	 * Shamelessly taken from RenderBlocks.getIconSafe.
	 * 
	 * @param par1Icon
	 * @return
	 */
    public static Icon getIconSafe(Icon par1Icon)
    {
        if (par1Icon == null)
        {
            par1Icon = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
        }

        return (Icon)par1Icon;
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
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (modelId == block.getRenderType()) {
			return rendererMap.get(block.getRenderType()).render(world, x, y, z, renderer);
		}
		
		return false;
	}
}
