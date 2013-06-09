package com.github.ttran17.blockmods;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = FunBlockMods.modid, name = "Fun Block Mods", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class FunBlockMods {
	public static final String modid = "FunBlockMods";

	public static final Map<String,Integer> blockIds = new TreeMap<String,Integer>();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		// Looks for file FunBlockMods.cfg in .minecraft/config/
		// Creates file it it doesn't exist.
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();

		// Once you choose a particular ordering, there's no going back ...
		// unless you edit the ids by hand using the config file.
		List<String> keys = new ArrayList<String>();
		keys.add("Leopard");
		keys.add("Disco Zebra");
		keys.add("Blue Tiger");
		keys.add("Blue Paw Print");
		keys.add("Siamese Cat");
		keys.add("Horse");
		keys.add("Kylie");
		keys.add("Creeper");
		keys.add("Magma Cube");
		keys.add("Skeleton in Tuxedo");
		keys.add("Blaze");
		keys.add("No Boys Allowed");
		keys.add("CM Block");
		keys.add("Stop Sitting on Me");
		keys.add("Ice Cream Block");
		keys.add("Tons of Ice Cream");
		keys.add("Peace Symbol");
		
		
		int defaultID = 1700;
		for (String key : keys) {
			int value = config.getBlock(key.replace(" ", ""), defaultID++).getInt();
			blockIds.put(key, value);
		}

		config.save();
	}

	@Init
	public void load(FMLInitializationEvent event)
	{    
		CreativeTabs creativeTabFunBlock = new CreativeTabFunBlock("Fun Blocks", blockIds.get("Leopard"));
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup." + creativeTabFunBlock.getTabLabel(), 
				"en_US", 
				creativeTabFunBlock.getTabLabel() );
		
		for (Map.Entry<String, Integer> entry : blockIds.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue().intValue();

			Block block = new FunBlock(value, Material.rock, modid, creativeTabFunBlock);
			block.setUnlocalizedName(key);
	        GameRegistry.registerBlock(block, modid + key);       
	        LanguageRegistry.addName(block, key);
		}
	}
}
