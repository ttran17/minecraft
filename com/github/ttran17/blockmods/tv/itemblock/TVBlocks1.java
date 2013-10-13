package com.github.ttran17.blockmods.tv.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TVBlocks1 extends TVItemBlock {

	
	/** At most sixteen TV shows! */
	public static String[] tvShows = new String[] {
		"The Garfield Show",
		"Scooby Doo"
	};

	public TVBlocks1(int blockItemId, Block block) {
		super(blockItemId, block);
	}


	@Override
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return tvShows[itemStack.getItemDamage()];
    }
}
