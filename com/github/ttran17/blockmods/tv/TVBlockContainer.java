package com.github.ttran17.blockmods.tv;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TVBlockContainer extends BlockContainer {

	private static int tvBlockContainerID = -1;
	
	public static void setDummyID(TVBlockContainer block) {
		tvBlockContainerID = block.blockID;
	}
	
	/** 
	 * Must call setDummyID() before calling getTvBlockContainerID()
	 * @return
	 */
	public static int getTvBlockContainerID() {
		if (tvBlockContainerID == -1) {
			throw new IllegalStateException("Must call TVBlockContainer.setDummyID() first!");
		}
		return tvBlockContainerID;
	}
	
	public TVBlockContainer(int blockID, Material material) {
		super(blockID, material);		
	}

	@Override
    /**
     * ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{      
		TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
		if (tileEntity != null){
			//Destroy the tv block
			world.destroyBlock(tileEntity.tv_x, tileEntity.tv_y, tileEntity.tv_z, false);
			//In our case this is most likely unnecessary since our primary block doesn'thave a tile entity ...
			world.removeBlockTileEntity(tileEntity.tv_x, tileEntity.tv_y, tileEntity.tv_z);
		}
		//Same as above, but for the TVTileEntity at the location of the TVBlockContainer
		world.removeBlockTileEntity(x, y, z);
    }
	
	@Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
		TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
		if (tileEntity != null){
			// If the tv block is gone then so are we ...
			if(world.getBlockId(tileEntity.tv_x, tileEntity.tv_y, tileEntity.tv_z) == 0){
				world.destroyBlock(x, y, z, false);
				world.removeBlockTileEntity(x, y, z);
			}
		} 
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     * <p>
     * In this case it returns the prism for the TV -- hence the primary coords are used.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
		TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
		if (tileEntity != null) {
			int blockID = world.getBlockId(tileEntity.tv_x, tileEntity.tv_y, tileEntity.tv_z);
			Block block = Block.blocksList[blockID];
			if (block != null && block instanceof TVBlock) {
				double[] bound = ((TVBlock) block).getBlockBoundsBasedOnState(world, tileEntity.tv_x, tileEntity.tv_y, tileEntity.tv_z);

				this.minX = bound[0];
				this.minY = bound[1];
				this.minZ = bound[2];
				this.maxX = bound[3];
				this.maxY = bound[4];
				this.maxZ = bound[5];

				return super.getSelectedBoundingBoxFromPool(world, tileEntity.tv_x, tileEntity.tv_y, tileEntity.tv_z);
			}
		}
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
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
	@SideOnly(Side.CLIENT)
	/**
	 * There is nothing that matches this renderType so this block is never actually rendered.
	 */
	public int getRenderType() {
		return -1;
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
		return 1;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TVTileEntity();
	}

}
