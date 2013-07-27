package com.github.ttran17.blockmods.crazy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class CrazyMultiBlock extends Block {
	
	public final String modid;
	
	private final String[] crazyTypes;

    @SideOnly(Side.CLIENT)
    private final Icon[] iconArray;

    public CrazyMultiBlock(int par1, Material material, String modid, String[] crazyTypes) 
    {
        super(par1, material);
        this.modid = modid;
        this.crazyTypes = crazyTypes;
        this.iconArray = new Icon[crazyTypes.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int metadata)
    {
        if (metadata < 0 || metadata >= this.iconArray.length)
        {
            metadata = 0;
        }

        return this.iconArray[metadata];
    }

    @Override
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int j = 0; j < crazyTypes.length; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }
    
    @SideOnly(Side.CLIENT)
    /**
     * Like getSubBlocks() but returns the ItemStacks paired with a name
     */
    public List<Pair<ItemStack, String>> getNamedItemStacks()
    {
    	List<Pair<ItemStack, String>> list = new ArrayList<Pair<ItemStack, String>>();
        for (int j = 0; j < crazyTypes.length; ++j)
        {
            list.add(Pair.of(new ItemStack(this, 1, j),crazyTypes[j]));
        }
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = par1IconRegister.registerIcon(modid.toLowerCase() + ":" + crazyTypes[i]);
        }
    }

}
