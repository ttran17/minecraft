package com.github.ttran17.blockmods.crazy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class CrazyBlock extends Block {
	
	public final String modid;

	public CrazyBlock(int par1, Material par2Material, String modid) {
		super(par1, par2Material);
		this.setCreativeTab(CrazyBlocksMod.creativeTab);
		this.modid = modid;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(modid.toLowerCase() + ":" + this.getUnlocalizedName().substring(5));
	}

}
