package com.github.ttran17.basemods.finerops;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ttran17.basemods.AbstractBytecodeTransformer;
import com.github.ttran17.basemods.IClassTransformer;

/**
 * Main class for modifying raw (obfuscated / non-MCP) Minecraft bytecode
 * 
 * @author ttran
 */
public class FinerOpsTransformer extends AbstractBytecodeTransformer {

	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) throws IOException {
		LOGGER.info("Running " + FinerOpsTransformer.class.getSimpleName());
		
		new FinerOpsTransformer();
	}
	
	protected String getJarName() {
		return "/home/ttran/vanilla-minecraft-server/versions/" + version + "/" + "minecraft_server." + version + ".jar";
	}

	public Map<IClassTransformer, String[]> getTransformers() throws IOException {
		Map<IClassTransformer, String[]> transformers = new HashMap<>();
		transformers.put(new DedicatedPlayerListTransformer(), new String[] {DedicatedPlayerListTransformer.DedicatedPlayerList_classname,"DedicatedPlayerList"});
		transformers.put(new EntityPlayerMPTransformer(), new String[] {EntityPlayerMPTransformer.EntityPlayerMP_classname,"EntityPlayerMP"});

		return transformers;
	}
}