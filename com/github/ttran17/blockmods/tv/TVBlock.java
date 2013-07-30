package com.github.ttran17.blockmods.tv;

import com.github.ttran17.blockmods.DirectionalBlock;
import com.github.ttran17.blockmods.MathUtils;
import com.github.ttran17.blockmods.MathUtils.YAW;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TVBlock extends DirectionalBlock {
	
	public static final double[][] canonical = new double[][]{
		// Base of TV 
		{0.2, 0.0, 0.4, 0.8, 0.05, 0.65},
		{0.475, 0.05, 0.475, 0.525, 0.075, 0.525},

		// Frame bottom
		{-1.0, 0.075, 0.45, 2.0, 0.125, 0.55},

		// Frame left side
		{-1.0, 0.125, 0.50, -0.95, 1.95, 0.55},

		// Frame right side
		{1.95, 0.125, 0.50, 2.0, 1.95, 0.55},

		// Frame top
		{-1.0, 1.95, 0.45, 2.0, 2.0, 0.55},

		// Frame back
		{-1.0, 0.125, 0.45, 2.0, 1.95, 0.50},

		// What's on TV
		{-0.95, 0.125, 0.50, 1.95, 1.95, 0.55}
	};

	public static final double[][][] yaw = MathUtils.getModelYawBasedOnCanonical(canonical);
	
	public static final double[][] bounds = MathUtils.getBoundsBasedOnYaw(yaw);
	
	@SideOnly(Side.CLIENT)
	public static int renderType;

	@SideOnly(Side.CLIENT)
	protected Icon tvShowIcon;
	
	@SideOnly(Side.CLIENT)
	protected Icon tvFrame;

	public TVBlock(int par1, Material par2Material, String modid) {
		super(par1, par2Material, modid);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {		
		tvShowIcon = iconRegister.registerIcon(modid.toLowerCase() + ":" + this.getUnlocalizedName().substring(5));
		tvFrame = iconRegister.registerIcon(modid.toLowerCase() + ":TVFrame");
		this.blockIcon = tvShowIcon;
	}
	
	@Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int blockYaw = world.getBlockMetadata(x, y, z) & 3;
    	double[] bound = bounds[blockYaw];		
		
        this.minX = bound[0];
        this.minY = bound[1];
        this.minZ = bound[2];
        this.maxX = bound[3];
        this.maxY = bound[4];
        this.maxZ = bound[5];
    }
	
	public double[] getBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int blockYaw = world.getBlockMetadata(x, y, z) & 3;
    	double[] bound = bounds[blockYaw];	
    	return bound;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return renderType;
	}
	
	@Override
	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	public int getRenderBlockPass()
	{
		return 0;
	}
	
	@Override
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.getBlockId(par2, par3, par4);
        Block block = Block.blocksList[l];
        return block == null || block.blockID == 0;
    }
	
	@SideOnly(Side.CLIENT)
	/** 
	 * Renders the TV
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param renderer
	 * @return
	 */
	public boolean render(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
		renderer.renderAllFaces = true;

		int blockYaw = world.getBlockMetadata(x, y, z) & 3;
		double[][] model = yaw[blockYaw];
				
		// Draw the TV Frame
		renderer.overrideBlockTexture = tvFrame;
		for (int i = 0; i < model.length-1; i++) {
			double[] xyz = model[i];
			renderer.setRenderBounds(xyz[0], xyz[1], xyz[2], xyz[3], xyz[4], xyz[5]);
			renderer.renderStandardBlock(this, x, y, z);
		}

		// Draw the TV show
		renderer.overrideBlockTexture = tvShowIcon;
		double[] xyz = model[model.length-1];		
		renderer.setRenderBounds(xyz[0], xyz[1], xyz[2], xyz[3], xyz[4], xyz[5]);
		renderer.renderStandardBlock(this, x, y, z);
		
		renderer.overrideBlockTexture = null;
		return true;
	}
}
