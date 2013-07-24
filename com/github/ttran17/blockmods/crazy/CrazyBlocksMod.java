package com.github.ttran17.blockmods.crazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.ttran17.blockmods.CreativeTabBlockMods;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = CrazyBlocksMod.modid, name = "Crazy Blocks Mod", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class CrazyBlocksMod {
	public static final String modid = "CrazyBlocksMod";
	
	public static final Map<String,Integer> blockIds = new TreeMap<String,Integer>();
	
	public static final CreativeTabBlockMods creativeTab = new CreativeTabBlockMods("Crazy Blocks");
	
	/**
	 * Check out the javadocs for {@link EventHandler}
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Looks for file CrazyBlocksMod.cfg in .minecraft/config/
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
		keys.add("Eskimo Dog");
		keys.add("Fireworks");
		keys.add("Gerbil Block");
		keys.add("Blue Balloons");
		keys.add("Candy Corn");
				
		int defaultID = 1700;
		for (String key : keys) {
			int value = config.getBlock(key.replace(" ", ""), defaultID++).getInt();
			blockIds.put(key, value);
		}

		config.save();

		for (Map.Entry<String, Integer> entry : blockIds.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue().intValue();

			Block block = new CrazyBlock(value, Material.rock, modid);
			block.setUnlocalizedName(key);
	        GameRegistry.registerBlock(block, modid + key);       
	        LanguageRegistry.addName(block, key);
		}
   
		creativeTab.setTabIconItemIndex(blockIds.get("Leopard"));
		LanguageRegistry.instance().addStringLocalization("itemGroup.Crazy Blocks", "en_US", "Crazy Blocks");
	}
}
