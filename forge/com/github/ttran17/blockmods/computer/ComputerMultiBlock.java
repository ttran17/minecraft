package com.github.ttran17.blockmods.computer;

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

import com.github.ttran17.blockmods.common.MathUtils;
import com.github.ttran17.blockmods.common.MathUtils.COMPASS;
import com.github.ttran17.blockmods.common.directional.DirectionalBlock;
import com.github.ttran17.blockmods.common.directional.DirectionalMultiBlock;
import com.github.ttran17.blockmods.common.render.IBlockRenderer;
import com.github.ttran17.blockmods.computer.tileentity.ComputerTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ComputerMultiBlock extends DirectionalMultiBlock implements IBlockRenderer {

	public static final double[][] canonical = new double[][] {
		// Base of computer 
		{0.2, 0.0, 0.1, 0.8, 0.025, 0.55},

		// Frame bottom
		{0.0, 0.1, 0.45, 1.0, 0.15, 0.5},

		// Frame left side
		{0.0, 0.15, 0.45, 0.05, 0.95, 0.5},

		// Frame right side
		{0.95, 0.15, 0.45, 1.0, 0.95, 0.5},

		// Frame top
		{0.0, 0.95, 0.45, 1.0, 1.0, 0.5},

		// Frame back
		{0.05, 0.15, 0.45, 0.95, 0.95, 0.475},
		
		// Support back
		{0.45, 0.5, 0.425, 0.55, 0.7, 0.45}, // BLACK
		{0.4, 0.015, 0.4, 0.6, 0.8, 0.425}, // GRAY // index = 7

		// Speakers
		{0.25, 0.05, 0.485, 0.75, 0.1, 0.515}, // GRAY
		
		// What's on the screen
		{0.05, 0.15, 0.475, 0.95, 0.95, 0.5}
	};
	
	public static final double[][][] model = MathUtils.getModelByCompassDirection(canonical);
	
	public static final double[][] bounds = MathUtils.getBoundsByCompassDirection(model);
	
	@SideOnly(Side.CLIENT)
	protected Icon blackFrame;
	
	@SideOnly(Side.CLIENT)
	protected Icon grayFrame;
	
	public ComputerMultiBlock(int blockid, Material material, String modid, String[] computers) {
		super(blockid, material, modid, computers);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {	
		super.registerIcons(iconRegister);
		blackFrame = iconRegister.registerIcon(modid.toLowerCase() + ":TVFrame");
		grayFrame = iconRegister.registerIcon(modid.toLowerCase() + ":GrayFrame");
	}

	@Override
	protected double[] getBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		ComputerTileEntity tileEntity = (ComputerTileEntity) world.getBlockTileEntity(x, y, z);
        if (tileEntity != null) {
        	return bounds[tileEntity.computer_compass];
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
        
        ComputerTileEntity tileEntity = (ComputerTileEntity) world.getBlockTileEntity(x, y, z);
        if (tileEntity != null) {
        	tileEntity.computer_compass = compass;
        }		
    }	
	
	@Override
	public boolean render(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
		ComputerTileEntity tileEntity = (ComputerTileEntity) world.getBlockTileEntity(x, y, z);
		
		if (tileEntity == null) {
			return false;
		}
		
		// Which direction to render the computer
		int compass = tileEntity.computer_compass;
		
		// Which computer to render
		int metadata = world.getBlockMetadata(x, y, z) & 3;
		
		double[][] model = this.model[compass];
				
		// Draw the computer frame
		boolean renderAllFacesState = renderer.renderAllFaces;
		renderer.renderAllFaces = true;
		renderer.overrideBlockTexture = blackFrame;
		for (int i = 0; i < 7; i++) {
			double[] xyz = model[i];
			renderer.setRenderBounds(xyz[0], xyz[1], xyz[2], xyz[3], xyz[4], xyz[5]);
			renderer.renderStandardBlock(this, x, y, z);
		}
		
		renderer.overrideBlockTexture = grayFrame;
		for (int i = 7; i < model.length-1; i++) {
			double[] xyz = model[i];
			renderer.setRenderBounds(xyz[0], xyz[1], xyz[2], xyz[3], xyz[4], xyz[5]);
			renderer.renderStandardBlock(this, x, y, z);
		}

		// Draw the computer screen
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
		return new ComputerTileEntity();
	}
	
}
