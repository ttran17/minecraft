package com.github.ttran17.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.ttran17.util.ClassSignature.Signature;

public class ClientDependencies {

	public static final String client = "/home/ttran/vanilla-minecraft/versions/";
	
	public static final String dir = client + ModUtils.version + "/";
	
	public static final String filename = ModUtils.version + ".jar";
	
	public static final File minecraftJar = new File(dir,filename);
	
	@Test
	/**
	 * The main minecraft class (i.e. "Minecraft.class")
	 */
	public void findMinecraftClass() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"textures/gui/title/mojang.png"}));

		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
	
	@Test
	public void findGuiScreenClass() {
		// Easier to find this by first locating Minecraft class and 
		// comparing instance variables in current version of Minecraft.class
		// against previous version ...
		
		// Even easier is to find GuiContainer class
		// and then use fact that Guiscreen is superclass ...
	}
	
	@Test
	public void findGuiContainerClass() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"textures/gui/container/inventory.png"}));

		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}

	@Test
	public void findTessellatorClass() {
		// Easier to find this by comparing instance variables in current version 
		// of found classes ... either Minecraft.class or GuiScreen.class contains
		// references to Tessellator.class
	}
}
