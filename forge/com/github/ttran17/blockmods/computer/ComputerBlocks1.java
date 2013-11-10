package com.github.ttran17.blockmods.computer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ComputerBlocks1 extends ItemBlockWithMetadata {

	/** At most sixteen computer types! */
	public static String[] computerTypes = new String[] {
		"The Garfield Show"
	};

	public ComputerBlocks1(int blockItemId, Block block) {
		super(blockItemId, block);
	}
	
	@Override
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return computerTypes[itemStack.getItemDamage()];
    }
}
