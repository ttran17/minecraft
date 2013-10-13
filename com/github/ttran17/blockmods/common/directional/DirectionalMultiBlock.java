package com.github.ttran17.blockmods.common.directional;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.github.ttran17.blockmods.common.render.IBlockRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class DirectionalMultiBlock extends DirectionalBlock implements ITileEntityProvider, IBlockRenderer {

	private final String[] multiBlockNames;
	
	@SideOnly(Side.CLIENT)
	public int renderType;

    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;
	
	protected DirectionalMultiBlock(int blockid, Material material, String modid, String[] multiBlockNames) {
		super(blockid, material, modid);
		
        this.multiBlockNames = multiBlockNames;
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
        for (int j = 0; j < multiBlockNames.length; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }
    
    /**
     * Like getSubBlocks() but returns the ItemStacks paired with a name
     */
    public List<Pair<ItemStack, String>> getNamedItemStacks()
    {
    	List<Pair<ItemStack, String>> list = new ArrayList<Pair<ItemStack, String>>();
        for (int j = 0; j < multiBlockNames.length; ++j)
        {
            list.add(Pair.of(new ItemStack(this, 1, j),multiBlockNames[j]));
        }
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
    	this.iconArray = new Icon[multiBlockNames.length];
    	
        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = iconRegister.registerIcon(modid.toLowerCase() + ":" + multiBlockNames[i]);
        }
    }
    
	@Override
    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     * cf. BlockContainer
     */
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
		super.breakBlock(world, x, y, z, par5, par6);
        world.removeBlockTileEntity(x, y, z);
    }

    @Override
    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
     * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
     * cf. BlockContainer
     */
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventID, int eventParam)
    {
        super.onBlockEventReceived(world, x, y, z, eventID, eventParam);
        TileEntity tileentity = world.getBlockTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(eventID, eventParam) : false;
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
