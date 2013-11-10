package com.github.ttran17.blockmods.common.directional;

import com.github.ttran17.blockmods.tv.TVBlocksMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class DirectionalBlock extends Block {
	
	protected final String modid;

	public DirectionalBlock(int blockid, Material material, String modid) {
		super(blockid, material);
		this.modid = modid;
	}

	@Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }
	
	protected abstract double[] getBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z);
	
	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		double[] bound = getBlockBoundsBasedOnState(world, x, y, z);	

		this.minX = bound[0];
		this.minY = bound[1];
		this.minZ = bound[2];
		this.maxX = bound[3];
		this.maxY = bound[4];
		this.maxZ = bound[5];
	}
	
    /**
     * Called when the block is placed in the world. 
     * <p>
     * cf. BlockPumpkin. Got it from there --ttran.
     */
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        int metadata = MathHelper.floor_double((double)(entityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
    }	
}
