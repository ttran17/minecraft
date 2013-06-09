package com.github.ttran17.blockmods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class FunBlock extends Block {
	
	private final String modid;

	public FunBlock(int par1, Material par2Material, String modid, CreativeTabs tab) {
		super(par1, par2Material);
		//this.setCreativeTab(CreativeTabs.tabBlock);
		this.setCreativeTab(tab);
		this.modid = modid;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(modid + ":" + this.getUnlocalizedName2());
	}

}
