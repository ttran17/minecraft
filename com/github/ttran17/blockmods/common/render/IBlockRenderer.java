package com.github.ttran17.blockmods.common.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public interface IBlockRenderer {
	
	@SideOnly(Side.CLIENT)
	void setRenderType(int renderType);

	@SideOnly(Side.CLIENT)
	boolean render(IBlockAccess world, int x, int y, int z, RenderBlocks renderer);
	
}
