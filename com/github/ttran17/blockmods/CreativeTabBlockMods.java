package com.github.ttran17.blockmods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabBlockMods extends CreativeTabs
{
	private int displayBlockID;
	
    public CreativeTabBlockMods(String par2Str)
    {
        super(par2Str);
    }
    
    public void setTabIconItemIndex(int displayBlockID) {
    	this.displayBlockID = displayBlockID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * the itemID for the item to be displayed on the tab
     */
    public int getTabIconItemIndex()
    {
        return displayBlockID;
    }
}
