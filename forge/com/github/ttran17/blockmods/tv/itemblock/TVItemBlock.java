package com.github.ttran17.blockmods.tv.itemblock;

import com.github.ttran17.blockmods.common.MathUtils.COMPASS;
import com.github.ttran17.blockmods.crazy.CrazyBlocksMod;
import com.github.ttran17.blockmods.tv.TVMultiBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TVItemBlock extends ItemBlockWithMetadata {

	public TVItemBlock(int blockItemId, Block block) {
		super(blockItemId, block);
	}
	
	@Override
	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitx, float hity, float hitz) {
		Result result = canPlaceTVItemBlockOnSide(world, x, y, z, side, entityPlayer, itemStack);

		x = result.x;
		y = result.y;
		z = result.z;
		side = result.side;
		
		if (!entityPlayer.canPlayerEdit(x, y, z, side, itemStack)) {
			return false;
		}
		
		if (result.canPlaceItemBlock) {
            Block block = Block.blocksList[getBlockID()];
			int j1 = this.getMetadata(itemStack.getItemDamage());
			int k1 = Block.blocksList[getBlockID()].onBlockPlaced(world, x, y, z, side, hitx, hity, hitz, j1);

			if (placeBlockAt(itemStack, entityPlayer, world, x, y, z, side, hitx, hity, hitz, k1)) {
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				--itemStack.stackSize;
			}

			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given ItemBlock can be placed on the given side of the given block position.
	 */
	public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer entityPlayer, ItemStack itemStack) {
		Result result = canPlaceTVItemBlockOnSide(world, x, y, z, side, entityPlayer, itemStack);
		
		return result.canPlaceItemBlock;
	}	
	
	
	/**
	 * Avoid redundant code ...
	 */
	private Result canPlaceTVItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer entityPlayer, ItemStack itemStack) {
		if (itemStack.stackSize == 0) {
			return new Result(false, x, y, z, side, -1);
		}
		
		int i1 = world.getBlockId(x, y, z);

		if (i1 == Block.snow.blockID)
		{
			side = 1;
		}
		else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
				&& (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(world, x, y, z)))
		{
			if (side == 0)
			{
				--y;
			}

			if (side == 1)
			{
				++y;
			}

			if (side == 2)
			{
				--z;
			}

			if (side == 3)
			{
				++z;
			}

			if (side == 4)
			{
				--x;
			}

			if (side == 5)
			{
				++x;
			}
		}

		if (y == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid()) {
			return new Result(false, x, y, z, side, -1);
		}

		TVMultiBlock block = (TVMultiBlock) Block.blocksList[getBlockID()];
		block.setPrePlacementState(true);
		int compass = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		block.setPrePlacementCompass(compass);
		boolean canPlaceEntityOnSide = world.canPlaceEntityOnSide(getBlockID(), x, y, z, false, side, null, itemStack);
		block.setPrePlacementState(false);

		boolean canPlaceTV = canPlaceTVBlockAt(world, block, x, y, z, compass);

		// return world.canPlaceEntityOnSide(this.getBlockID(), x, y, z, false, side, (Entity)null, itemStack);
		return new Result(canPlaceEntityOnSide && canPlaceTV, x, y, z, side, compass);
	}	

	private boolean canPlaceTVBlockAt(World world, Block block, int x, int y, int z, int compass) {

		if (block.canPlaceBlockAt(world, x, y, z) && block.canPlaceBlockAt(world, x, y+1, z)) {
			int h = 1; // horizontal extent

			if (compass == COMPASS.NORTH.index || compass == COMPASS.SOUTH.index) {
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
				return true;
			} 

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
			return true;
		}
		return false;
	}

	private static class Result {
		public final boolean canPlaceItemBlock;
		public final int x, y, z;
		public final int side;
		public final int compass;

		public Result(boolean canPlace, int x, int y, int z, int side, int compass) {
			this.canPlaceItemBlock = canPlace;
			this.x = x;
			this.y = y;
			this.z = z;
			this.side = side;
			this.compass = compass;
		}		
	}
}
