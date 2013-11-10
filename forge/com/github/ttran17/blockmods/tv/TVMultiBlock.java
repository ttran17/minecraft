package com.github.ttran17.blockmods.tv;

import com.github.ttran17.blockmods.common.MathUtils;
import com.github.ttran17.blockmods.common.MathUtils.COMPASS;
import com.github.ttran17.blockmods.common.directional.DirectionalBlock;
import com.github.ttran17.blockmods.common.directional.DirectionalMultiBlock;
import com.github.ttran17.blockmods.common.render.IBlockRenderer;
import com.github.ttran17.blockmods.tv.tileentity.TVFrameTileEntity;
import com.github.ttran17.blockmods.tv.tileentity.TVTileEntity;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TVMultiBlock extends DirectionalMultiBlock {
	
	public static final double[][] canonical = new double[][] {
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

	public static final double[][][] model = MathUtils.getModelByCompassDirection(canonical);
	
	public static final double[][] bounds = MathUtils.getBoundsByCompassDirection(model);
	
	@SideOnly(Side.CLIENT)
	protected Icon tvFrame;
	
	private boolean prePlacementState;
	private int prePlacementCompass;

	public TVMultiBlock(int blockid, Material material, String modid, String[] tvShows) {
		super(blockid, material, modid, tvShows);
		this.prePlacementState = false;
	}
		
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {	
		super.registerIcons(iconRegister);
		tvFrame = iconRegister.registerIcon(modid.toLowerCase() + ":TVFrame");
	}

	public void setPrePlacementState(boolean state) {
		prePlacementState = state;
	}
	
	public void setPrePlacementCompass(int compass) {
		prePlacementCompass = compass;
	}
	
	protected double[] getBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if (prePlacementState) {
			return bounds[prePlacementCompass];
		}
        TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
        if (tileEntity != null) {
        	return bounds[tileEntity.tv_compass];
        }
		return bounds[0]; // Ack!
	}
	
	@Override
	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}
	
    /**
     * Called when the block is placed in the world. 
     * <p>
     * cf. BlockPumpkin. Got it from there --ttran.
     */
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        int compass = MathHelper.floor_double((double)(entityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        
        TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
        if (tileEntity != null) {
        	tileEntity.tv_compass = compass;
        }
                
		// Now place dummy blocks
		if (compass == COMPASS.NORTH.index || compass == COMPASS.SOUTH.index) {
			placeDummyBlocks(world, x-1, y, z, x, y, z);
			placeDummyBlocks(world, x+1, y, z, x, y, z);

			placeDummyBlocks(world, x-1, y+1, z, x, y, z);
			placeDummyBlocks(world, x,   y+1, z, x, y, z);
			placeDummyBlocks(world, x+1, y+1, z, x, y, z);
		} else {
			placeDummyBlocks(world, x, y+1, z-1, x, y, z);
			placeDummyBlocks(world, x, y+1, z+1, x, y, z);

			placeDummyBlocks(world, x, y+1, z-1, x, y, z);
			placeDummyBlocks(world, x, y+1, z,   x, y, z);
			placeDummyBlocks(world, x, y+1, z+1, x, y, z); 
		}		
    }		
	
	private void placeDummyBlocks(World world, int x, int y, int z, int tv_x, int tv_y, int tv_z) {
		world.setBlock(x, y, z, TVFrame.getTVFrameID(), 0, 2);
		TVFrameTileEntity tileEntity = (TVFrameTileEntity) world.getBlockTileEntity(x, y, z);
		if (tileEntity != null) {
			tileEntity.tv_x = tv_x;
			tileEntity.tv_y = tv_y;
			tileEntity.tv_z = tv_z;			
		}
	}
	
	@SideOnly(Side.CLIENT)
	/** 
	 * Renders the TV
	 */
	public boolean render(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
		TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
		
		if (tileEntity == null) {
			return false;
		}
		
		// Which direction to render the TV
		int compass = tileEntity.tv_compass;
		
		// Which TV show to render
		int metadata = world.getBlockMetadata(x, y, z) & 3;
		
		double[][] model = this.model[compass];
				
		// Draw the TV Frame
		boolean renderAllFacesState = renderer.renderAllFaces;
		renderer.renderAllFaces = true;
		renderer.overrideBlockTexture = tvFrame;
		for (int i = 0; i < model.length-1; i++) {
			double[] xyz = model[i];
			renderer.setRenderBounds(xyz[0], xyz[1], xyz[2], xyz[3], xyz[4], xyz[5]);
			renderer.renderStandardBlock(this, x, y, z);
		}

		// Draw the TV show
		renderer.overrideBlockTexture = getIcon(compass, metadata);
		double[] xyz = model[model.length-1];		
		renderer.setRenderBounds(xyz[0], xyz[1], xyz[2], xyz[3], xyz[4], xyz[5]);
		renderer.renderStandardBlock(this, x, y, z);
		
		renderer.overrideBlockTexture = null;
		renderer.renderAllFaces = renderAllFacesState;
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TVTileEntity();
	}
	
}
