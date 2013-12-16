package com.github.ttran17.basemods.virtualbox;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ttran17.basemods.AbstractBytecodeTransformer;
import com.github.ttran17.basemods.IClassTransformer;
import com.github.ttran17.dependencies.ClientDependencies;

/**
 * Main class for modifying raw (obfuscated / non-MCP) Minecraft bytecode
 * 
 * @author ttran
 */
public class VboxOpenGLTransformer extends AbstractBytecodeTransformer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void main(String[] args) throws IOException {
		LOGGER.info("Running " + VboxOpenGLTransformer.class.getSimpleName());
		
		new VboxOpenGLTransformer();
	}
	
	protected File getMinecraftJar() {
		return ClientDependencies.minecraftJar;
	}

	@Override
	protected Map<IClassTransformer, String[]> getTransformers() throws IOException {
		Map<IClassTransformer, String[]> transformers = new HashMap<>();
		transformers.put(new MinecraftTransformer(), new String[] {MinecraftTransformer.Minecraft_classname,"Minecraft"});
		transformers.put(new GuiScreenTransformer(), new String[] {GuiScreenTransformer.GuiScreen_classname,"GuiScreen"});
		transformers.put(new GuiContainerTransformer(), new String[] {GuiContainerTransformer.GuiContainer_classname,"GuiContainer"});

		return transformers;
	}
}