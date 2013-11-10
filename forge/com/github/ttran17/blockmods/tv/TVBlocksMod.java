package com.github.ttran17.blockmods.tv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import com.github.ttran17.blockmods.common.CreativeTabBlockMods;
import com.github.ttran17.blockmods.common.render.IBlockRenderer;
import com.github.ttran17.blockmods.common.render.RenderingProxy;
import com.github.ttran17.blockmods.tv.itemblock.TVBlocks1;
import com.github.ttran17.blockmods.tv.tileentity.TVFrameTileEntity;
import com.github.ttran17.blockmods.tv.tileentity.TVTileEntity;

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


@Mod(modid = TVBlocksMod.modid, name = "TV Blocks Mod", version = "2.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class TVBlocksMod {
	public static final String modid = "TVBlocksMod";
	
	@SidedProxy(clientSide="com.github.ttran17.blockmods.common.render.ClientRenderingProxy",serverSide="com.github.ttran17.blockmods.common.render.RenderingProxy")
	public static RenderingProxy renderingProxy;
	
	public static final CreativeTabBlockMods creativeTab = new CreativeTabBlockMods("TVs");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		
		int defaultID = 1800;
		int dummyBlockID = config.getBlock("TVFrame", defaultID++).getInt();
		TVFrame block = new TVFrame(dummyBlockID, Material.rock);
		block.setTextureName(modid.toLowerCase() + ":TVFrame");
		TVFrame.setDummyID(block);
        GameRegistry.registerBlock(block, modid + "TVFrame");    
        GameRegistry.registerTileEntity(TVFrameTileEntity.class, modid + "." + "TVFrame");
        GameRegistry.registerTileEntity(TVTileEntity.class, modid + "." + "TVBlock");
        
		int displayBlockID = register(config, defaultID++, TVBlocks1.class, TVBlocks1.tvShows);
		
		config.save();
		
		creativeTab.setTabIconItemIndex(displayBlockID);
		LanguageRegistry.instance().addStringLocalization("itemGroup.TV Shows", "en_US", "TV Shows");
	}
	
	private int register(Configuration config, int defaultID, Class<? extends ItemBlockWithMetadata> clazz, String[] tvShows) {
		int value = config.getBlock(clazz.getSimpleName(),defaultID).getInt();
		TVMultiBlock tvmb = new TVMultiBlock(value, Material.rock, modid, tvShows);
		tvmb.setUnlocalizedName(clazz.getSimpleName());
		tvmb.setCreativeTab(creativeTab);
		GameRegistry.registerBlock(tvmb, clazz, modid + "." + clazz.getSimpleName());		
		for (Pair<ItemStack,String> pair : tvmb.getNamedItemStacks()) {
			LanguageRegistry.addName(pair.getLeft(), pair.getRight());
		}
		
		renderingProxy.addRenderers(tvmb);
		
		return value;
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{    				
		renderingProxy.registerRenderers();
	}

}
