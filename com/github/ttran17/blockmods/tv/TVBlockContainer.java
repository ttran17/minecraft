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

	@SideOnly(Side.CLIENT)
	public static final int renderType = RenderingRegistry.getNextAvailableRenderId();
	
	protected TVBlockContainer(int blockID, Material material) {
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
			//Actually destroys primary block.
			world.destroyBlock(tileEntity.primary_x, tileEntity.primary_y, tileEntity.primary_z, false);
			//Forces removing tile entity from primary block coordinates,
			//cause sometimes minecraft forgets to do that.
			world.removeBlockTileEntity(tileEntity.primary_x, tileEntity.primary_y, tileEntity.primary_z);
		}
		//Same as above, but for the gag block tile entity.
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
			//No need to check if block's Id matches the Id of our primary block, 
			//because if a player want to change a block, he needs to break it first, 
			//and in this case block will be set to Air (Id = 0)
			if(world.getBlockId(tileEntity.primary_x, tileEntity.primary_y, tileEntity.primary_z) == 0){
				world.destroyBlock(x, y, z, false);
				world.removeBlockTileEntity(x, y, z);
			}
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
		TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
		if (tileEntity != null) {
			int px = tileEntity.primary_x;
			int py = tileEntity.primary_y;
			int pz = tileEntity.primary_z;
			
			int blockID = world.getBlockId(px, py, pz);
			Block block = Block.blocksList[blockID];
			if (block != null && block instanceof TVBlock) {
				double[] bound = ((TVBlock) block).getBlockBoundsBasedOnState(world, px, py, pz);

				this.minX = bound[0];
				this.minY = bound[1];
				this.minZ = bound[2];
				this.maxX = bound[3];
				this.maxY = bound[4];
				this.maxZ = bound[5];

				return super.getSelectedBoundingBoxFromPool(world, px, py, pz);
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
		return 1;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TVTileEntity();
	}

}
