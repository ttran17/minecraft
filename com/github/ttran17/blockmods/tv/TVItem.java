package com.github.ttran17.blockmods.tv;

import com.github.ttran17.blockmods.MathUtils.YAW;
import com.github.ttran17.blockmods.crazy.CrazyBlocksMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TVItem extends Item {
	
	public final int blockID;
	public final int dummyBlockID;

	public TVItem(int itemID, int blockID, int dummyBlockID) {
		super(itemID);
		this.blockID = blockID;
		this.dummyBlockID = dummyBlockID;
	}

	@Override
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
	{
		Block block = Block.blocksList[this.blockID];
		int yaw = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;

		if (!canPlaceBlockAt(world, block, x, y+1, z, yaw)) {
			return false;
		}
				
		world.setBlock(x, y+1, z, this.blockID, 0, 2);
		block.onBlockPlacedBy(world, x, y+1, z, player, stack);

		// Now place dummy blocks
		if (yaw == YAW.South.index || yaw == YAW.North.index) {
			placeDummyBlocks(world, x-1, y+1, z, x, y+1, z);
			placeDummyBlocks(world, x+1, y+1, z, x, y+1, z);

			placeDummyBlocks(world, x-1, y+2, z, x, y+1, z);
			placeDummyBlocks(world, x,   y+2, z, x, y+1, z);
			placeDummyBlocks(world, x+1, y+2, z, x, y+1, z);
		} else {
			placeDummyBlocks(world, x, y+1, z-1, x, y+1, z);
			placeDummyBlocks(world, x, y+1, z+1, x, y+1, z);

			placeDummyBlocks(world, x, y+2, z-1, x, y+1, z);
			placeDummyBlocks(world, x, y+2, z,   x, y+1, z);
			placeDummyBlocks(world, x, y+2, z+1, x, y+1, z); 
		}

		return true;
	}
	
	private boolean canPlaceBlockAt(World world, Block block, int x, int y, int z, int yaw) {
        if (block.canPlaceBlockAt(world, x, y, z) && block.canPlaceBlockAt(world, x, y+1, z)) {
        	// For that day when we have a huge TV ...
        	int h = 1; // horizontal extent

        	if (yaw == YAW.South.index || yaw == YAW.North.index) {
        		if (!block.canPlaceBlockAt(world, x-h, y, z)) {
        			return false;
        		}
        		if (!block.canPlaceBlockAt(world, x+h, y, z)) {
        			return false;
        		}
        		if (!block.canPlaceBlockAt(world, x-h, y+1, z)) {
        			return false;
        		}
        		if (!block.canPlaceBlockAt(world, x+h, y+1, z)) {
        			return false;
        		}
        	} else {
        		if (!block.canPlaceBlockAt(world, x, y, z-h)) {
        			return false;
        		}
        		if (!block.canPlaceBlockAt(world, x, y, z+h)) {
        			return false;
        		}   
        		if (!block.canPlaceBlockAt(world, x, y+1, z-h)) {
        			return false;
        		}
        		if (!block.canPlaceBlockAt(world, x, y+1, z+h)) {
        			return false;
        		}  
        	}
        }
        return true;
	}
	
	private void placeDummyBlocks(World world, int x, int y, int z, int px, int py, int pz) {
		world.setBlock(x, y, z, this.dummyBlockID, 0, 2);
		TVTileEntity tileEntity = (TVTileEntity) world.getBlockTileEntity(x, y, z);
		if (tileEntity != null) {
			tileEntity.primary_x = px;
			tileEntity.primary_y = py;
			tileEntity.primary_z = pz;			
		}
	}
}
