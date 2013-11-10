package com.github.ttran17.blockmods.crazy.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class CrazyBlocks1 extends ItemBlockWithMetadata {

	/** At most sixteen crazy types! */
	public static String[] crazyTypes = new String[] {
		"Leopard",
		"Disco Zebra",
		"Blue Tiger",
		"Blue Paw Print",
		"Siamese Cat",
		"Horse",
		"Kylie",
		"Creeper",
		"Magma Cube",
		"Skeleton in Tuxedo",
		"Blaze",
		"No Boys Allowed",
		"CM Block",
		"Stop Sitting on Me",
		"Ice Cream Block",
		"Tons of Ice Cream"
	};
	
	public CrazyBlocks1(int blockItemId, Block block) {
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
