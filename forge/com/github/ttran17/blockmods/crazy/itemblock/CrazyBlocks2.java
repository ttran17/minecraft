package com.github.ttran17.blockmods.crazy.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class CrazyBlocks2 extends ItemBlockWithMetadata {

	/** At most sixteen crazy types! */
	public static String[] crazyTypes = new String[] {
		"Peace Symbol",
		"Eskimo Dog",
		"Fireworks",
		"Gerbil Block",
		"Blue Balloons",
		"Candy Corn"
	};
	
	public CrazyBlocks2(int blockItemId, Block block) {
		super(blockItemId, block);
	}

	@Override
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return crazyTypes[itemStack.getItemDamage()];
    }

}
