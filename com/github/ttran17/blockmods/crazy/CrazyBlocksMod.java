package com.github.ttran17.blockmods.crazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import com.github.ttran17.blockmods.CreativeTabBlockMods;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = CrazyBlocksMod.modid, name = "Crazy Blocks Mod", version = "2.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class CrazyBlocksMod {
	public static final String modid = "CrazyBlocksMod";

	public static final CreativeTabBlockMods creativeTab = new CreativeTabBlockMods("Crazy Blocks");
	
	/**
	 * Check out the javadocs for {@link EventHandler}
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Looks for file CrazyBlocksMod.cfg -- creates file it it doesn't exist.
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		
		// Order matters. Or you can mess with the config file.
		int defaultID = 1700;
		int displayBlockID = register(config, defaultID++, CrazyBlocks1.class, CrazyBlocks1.crazyTypes);
		register(config, defaultID++, CrazyBlocks2.class, CrazyBlocks2.crazyTypes);
		
		config.save();
		
		creativeTab.setTabIconItemIndex(displayBlockID);
		LanguageRegistry.instance().addStringLocalization("itemGroup.Crazy Blocks", "en_US", "Crazy Blocks");
	}
	
	private int register(Configuration config, int defaultID, Class<? extends ItemBlockWithMetadata> clazz, String[] crazyTypes) {
		int value = config.getBlock(clazz.getSimpleName(),defaultID).getInt();
		CrazyMultiBlock cmb = new CrazyMultiBlock(value, Material.rock, modid, crazyTypes);
		cmb.setUnlocalizedName(clazz.getSimpleName());
		cmb.setCreativeTab(creativeTab);
		GameRegistry.registerBlock(cmb, clazz, modid + "." + clazz.getSimpleName());		
		for (Pair<ItemStack,String> pair : cmb.getNamedItemStacks()) {
			LanguageRegistry.addName(pair.getLeft(), pair.getRight());
		}
		
		return value;
	}
}
