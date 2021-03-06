package com.github.ttran17.dependencies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.ttran17.dependencies.ClientDependencies;
import com.github.ttran17.util.ClassSignature;
import com.github.ttran17.util.ModUtils;
import com.github.ttran17.util.ClassSignature.Signature;

public class VirtualBoxDependencies {
	
	private static final File minecraftJar = ClientDependencies.minecraftJar;
	
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
		List<Signature> signatures = new ArrayList<>();
		// signatures.add(new Signature("visitField", new String[] {"(ACC_PUBLIC, \"l\", \"I\", null, null)"}));
		// signatures.add(new Signature("visitField", new String[] {"(ACC_PUBLIC, \"m\", \"I\", null, null)"}));
		// signatures.add(new Signature("visitField", new String[] {"(ACC_PROTECTED, \"n\", \"Ljava/util/List;\", null, null)"}));
		// signatures.add(new Signature("visitField", new String[] {"(ACC_PROTECTED, \"o\", \"Ljava/util/List;\", null, null)"}));
		// signatures.add(new Signature("visitMethod", new String[] {"(ACC_PUBLIC, \"b\", \"(I)V\", null, null)"}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"new Integer(-1072689136)"}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"new Integer(-804253680)"}));
		
		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
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
		
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"Already building!"}));

		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
	
	@Test
	public void findGL_BLEND() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitIntInsn", new String[] {"SIPUSH","3042"}));

		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
}
