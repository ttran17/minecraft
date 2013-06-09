package com.github.ttran17.blockmods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class FunBlock extends Block {
	
	private final String modid;

	public FunBlock(int par1, Material par2Material, String modid) {
		super(par1, par2Material);
		this.setCreativeTab(FunBlockMods.creativeTabFunBlock);
		this.modid = modid;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(modid + ":" + this.getUnlocalizedName2());
	}

}
