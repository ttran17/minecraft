package com.github.ttran17.blockmods.xray;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.ttran17.blockmods.common.MathUtils;
import com.github.ttran17.blockmods.common.MathUtils.COMPASS;
import com.github.ttran17.blockmods.common.MathUtils.FACE;
import com.github.ttran17.blockmods.common.directional.DirectionalBlock;
import com.github.ttran17.blockmods.common.render.ClientRenderingProxy;
import com.github.ttran17.blockmods.common.render.IBlockRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class XrayBlock extends Block implements IBlockRenderer {
	
	private static final double[][] bounds = new double[6][];
	
	private static final int[][] iconSide = new int[][] {
	 //  Side of Xray Block, Side of Block that Xray is attached to, how to get to latter block
		{FACE.BOTTOM.index, FACE.TOP.index, 	0, -1,  0},
		{FACE.TOP.index, 	FACE.BOTTOM.index, 	0,  1,  0},
		{FACE.NORTH.index, 	FACE.SOUTH.index, 	0,  0, -1},
		{FACE.SOUTH.index, 	FACE.NORTH.index, 	0,  0,  1},
		{FACE.WEST.index, 	FACE.EAST.index,   -1,  0,  0},
		{FACE.EAST.index, 	FACE.WEST.index, 	1,  0,  0}
	};
	
	static {
		double[][] canonical = new double[][] {{0.0, 0.0, 0.0, 1.0, 1.0, 0.0}};
		double[][][] model = MathUtils.getModelByCompassDirection(canonical);
		
		// Looking at it from below
		bounds[FACE.BOTTOM.index] = new double[]{0.0, 1.0, 0.0, 1.0, 1.0, 1.0};
		
		// Looking at it from top
		bounds[FACE.TOP.index] = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0};
		
		// Looking at it from north
		bounds[FACE.NORTH.index] = model[COMPASS.SOUTH.index][0];
				
	    // Looking at it from south
		bounds[FACE.SOUTH.index] = model[COMPASS.NORTH.index][0];
				
		// Looking at it from west
		bounds[FACE.WEST.index] = model[COMPASS.EAST.index][0];
		
	    // Looking at it from east
		bounds[FACE.EAST.index] = model[COMPASS.WEST.index][0];
	}

	protected final String modid;
	
	@SideOnly(Side.CLIENT)
	public int renderType;

	@SideOnly(Side.CLIENT)
	protected Icon inventoryIcon;
	
	@SideOnly(Side.CLIENT)
	protected boolean isOpaqueCube;
	
	public XrayBlock(int blockid, Material material, String modid) {
		super(blockid, material);
		this.modid = modid;
		this.lightOpacity[blockid] = 0;
		this.useNeighborBrightness[blockid] = true;
		this.isOpaqueCube = true;
	}
	
	@Override
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
		this.isOpaqueCube = false;
        this.setBlockBoundsBasedOnState(world, x, y, z);
        this.isOpaqueCube = true;
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
		this.isOpaqueCube = false;
        this.setBlockBoundsBasedOnState(world, x, y, z);
        this.isOpaqueCube = true;
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }
	
	protected double[] getBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if (isOpaqueCube) {
			return new double[] {0.0,0.0,0.0,1.0,1.0,1.0};
		}
		int metadata = world.getBlockMetadata(x, y, z) & 7;
		return bounds[metadata];
	}
	
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {		
		inventoryIcon = iconRegister.registerIcon(modid.toLowerCase() + ":" + this.getUnlocalizedName().substring(5));
		this.blockIcon = inventoryIcon;
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
    public boolean isOpaqueCube()
    {
        return isOpaqueCube;
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
	
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
    	// overrides the default returned value which is usually 'metadata'
    	// 'side' is the side of the other block the xray is attached to; it is not the side of the xray block!
        return side; 
    }
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) { }
    
	@Override
    /**
     * Called after a block is placed
     */
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata) {
		world.setBlockMetadataWithNotify(x, y, z, metadata, 2); // metadata is 'side' in this case
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
		// Get the orientation info for the Xray Block
		// 'side' is the side of the other block the xray is attached to; it is not the side of the xray block!
		int side = world.getBlockMetadata(x, y, z) & 7;
		
		// Render other sides as needed -- for aesthetics only
		isOpaqueCube = false;	
		for (int s = 0; s < 6; s++) {
			if (side == iconSide[s][1]) {
				continue;
			}					
			int dx = iconSide[s][2];
			int dy = iconSide[s][3];
			int dz = iconSide[s][4];
			Material material = world.getBlockMaterial(x+dx, y+dy, z+dz);
			if (!material.isReplaceable() || material.isLiquid()) {
				int blockId = world.getBlockId(x+dx, y+dy, z+dz);
				if (blockId == this.blockID) {
					continue;
				}
				Block block = Block.blocksList[blockId];
				renderer.renderStandardBlock(block, x+dx, y+dy, z+dz);
			}	
		}
		isOpaqueCube = true;

		return true;
	}


	@SideOnly(Side.CLIENT)
	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return renderType;
	}
}
