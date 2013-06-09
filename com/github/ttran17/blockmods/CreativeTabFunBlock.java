package com.github.ttran17.blockmods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

public final class CreativeTabFunBlock extends CreativeTabs
{
	private final int displayBlockID;
	
    CreativeTabFunBlock(String par2Str, int displayBlockID)
    {
        super(par2Str);
        this.displayBlockID = displayBlockID;
    }

    @SideOnly(Side.CLIENT)
    /**
     * the itemID for the item to be displayed on the tab
     */
    public int getTabIconItemIndex()
    {
        return displayBlockID;
    }
}
