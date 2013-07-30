package com.github.ttran17.blockmods.tv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;

import com.github.ttran17.blockmods.BlockRenderer;
import com.github.ttran17.blockmods.CreativeTabBlockMods;
import com.github.ttran17.blockmods.RenderingProxy;
import com.github.ttran17.blockmods.crazy.CrazyBlock;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@Mod(modid = TVBlocksMod.modid, name = "TV Blocks Mod", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class TVBlocksMod {
	public static final String modid = "TVBlocksMod";
	
	@SidedProxy(clientSide="com.github.ttran17.blockmods.ClientRenderingProxy",serverSide="com.github.ttran17.blockmods.RenderingProxy")
	public static RenderingProxy renderingProxy;
	
	public static final CreativeTabBlockMods creativeTab = new CreativeTabBlockMods("TVs");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Map<String,Integer> blockIDs = new TreeMap<String,Integer>();
		
		// Looks for file TVBlocksMod.cfg in .minecraft/config/
		// Creates file it it doesn't exist.
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
				
		// Once you choose a particular ordering, there's no going back ...
		// unless you edit the ids by hand using the config file.
		List<String> keys = new ArrayList<String>();
		keys.add("Dummy Block");
		keys.add("The Garfield Show");
		keys.add("Scooby Doo");
				
		int defaultBlockIDs = 1800;
		for (String key : keys) {
			int blockID = config.getBlock(key.replace(" ", ""), defaultBlockIDs++).getInt();
			blockIDs.put(key, blockID);
		}

		config.save();

		int dummyBlockID = 0;
		for (String key : keys) {
			int blockID = blockIDs.get(key);
			if (key.equalsIgnoreCase("Dummy Block")) {
				dummyBlockID = blockID;
				TVBlockContainer block = new TVBlockContainer(dummyBlockID, Material.rock);
				block.func_111022_d(modid.toLowerCase() + ":TVFrame");
				TVBlockContainer.setDummyID(block);
		        GameRegistry.registerBlock(block, modid + key);    
		        GameRegistry.registerTileEntity(TVTileEntity.class, modid + "." + key);
				continue;
			}
			Block block = (new TVBlock(blockID, Material.rock, modid)).setUnlocalizedName(key).setCreativeTab(creativeTab);
	        GameRegistry.registerBlock(block, TVItemBlock.class, modid + "." + key);       
	        LanguageRegistry.addName(block, key);
		}

		creativeTab.setTabIconItemIndex(blockIDs.get("The Garfield Show"));
		LanguageRegistry.instance().addStringLocalization("itemGroup.TVs", "en_US", "TVs");
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{    				
		renderingProxy.registerRenderers();
	}

}
