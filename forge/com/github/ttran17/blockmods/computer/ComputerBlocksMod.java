package com.github.ttran17.blockmods.computer;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import org.apache.commons.lang3.tuple.Pair;

import com.github.ttran17.blockmods.common.CreativeTabBlockMods;
import com.github.ttran17.blockmods.common.render.RenderingProxy;
import com.github.ttran17.blockmods.computer.tileentity.ComputerTileEntity;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = ComputerBlocksMod.modid, name = "Computer Blocks Mod", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ComputerBlocksMod {
	public static final String modid = "ComputerBlocksMod";
	
	@SidedProxy(clientSide="com.github.ttran17.blockmods.common.render.ClientRenderingProxy",serverSide="com.github.ttran17.blockmods.common.render.RenderingProxy")
	public static RenderingProxy renderingProxy;
	
	public static final CreativeTabBlockMods creativeTab = new CreativeTabBlockMods("Computers");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		
		int defaultID = 1810;
//		int dummyBlockID = config.getBlock("TVFrame", defaultID++).getInt();
//		TVFrame block = new TVFrame(dummyBlockID, Material.rock);
//		block.func_111022_d(modid.toLowerCase() + ":TVFrame");
//		TVFrame.setDummyID(block);
//        GameRegistry.registerBlock(block, modid + "TVFrame");    
//        GameRegistry.registerTileEntity(TVFrameTileEntity.class, modid + "." + "TVFrame");
//        GameRegistry.registerTileEntity(TVTileEntity.class, modid + "." + "TVBlock");
        
		GameRegistry.registerTileEntity(ComputerTileEntity.class, modid + "." + "ComputerBlock");
		
		int displayBlockID = register(config, defaultID++, ComputerBlocks1.class, ComputerBlocks1.computerTypes);
		
		config.save();
		
		creativeTab.setTabIconItemIndex(displayBlockID);
		LanguageRegistry.instance().addStringLocalization("itemGroup.Computers", "en_US", "Computers");
	}
	
	private int register(Configuration config, int defaultID, Class<? extends ItemBlockWithMetadata> clazz, String[] computerTypes) {
		int value = config.getBlock(clazz.getSimpleName(),defaultID).getInt();
		ComputerMultiBlock cmb = new ComputerMultiBlock(value, Material.rock, modid, computerTypes);
		cmb.setUnlocalizedName(clazz.getSimpleName());
		cmb.setCreativeTab(creativeTab);
		GameRegistry.registerBlock(cmb, clazz, modid + "." + clazz.getSimpleName());		
		for (Pair<ItemStack,String> pair : cmb.getNamedItemStacks()) {
			LanguageRegistry.addName(pair.getLeft(), pair.getRight());
		}
		
		renderingProxy.addRenderers(cmb);
		
		return value;
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{    				
		renderingProxy.registerRenderers();
	}
}
