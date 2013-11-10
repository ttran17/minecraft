package com.github.ttran17.blockmods.xray;

import com.github.ttran17.blockmods.common.render.RenderingProxy;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = XrayBlockMod.modid, name = "Xray Block Mod", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class XrayBlockMod {
	public static final String modid = "XrayBlockMod";
	
	@SidedProxy(clientSide="com.github.ttran17.blockmods.common.render.ClientRenderingProxy",serverSide="com.github.ttran17.blockmods.common.render.RenderingProxy")
	public static RenderingProxy renderingProxy;
	
	/**
	 * Check out the javadocs for {@link EventHandler}
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		int defaultID = 1700;
		XrayBlock xray = new XrayBlock(defaultID, Material.glass, modid);
		xray.setUnlocalizedName(XrayBlock.class.getSimpleName());
		xray.setCreativeTab(CreativeTabs.tabMaterials);
		
		GameRegistry.registerBlock(xray, modid + "." + XrayBlock.class.getSimpleName());
		LanguageRegistry.addName(xray, "Xray Block");
		
		renderingProxy.addRenderers(xray);
		
		config.save();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{    				
		renderingProxy.registerRenderers();
	}
}
