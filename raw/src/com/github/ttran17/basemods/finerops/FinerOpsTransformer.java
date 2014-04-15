package com.github.ttran17.basemods.finerops;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ttran17.basemods.AbstractBytecodeTransformer;
import com.github.ttran17.basemods.IClassTransformer;
import com.github.ttran17.dependencies.ServerDependencies;
import com.github.ttran17.util.ClassSignature;
import com.github.ttran17.util.ModUtils;
import com.github.ttran17.util.ClassSignature.Signature;

/**
 * Main class for modifying raw (obfuscated / non-MCP) Minecraft bytecode
 * 
 * @author ttran
 */
public class FinerOpsTransformer extends AbstractBytecodeTransformer {

	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) throws IOException {
		LOGGER.info("Running " + FinerOpsTransformer.class.getSimpleName() + " for version " + ModUtils.version);
		
		(new FinerOpsTransformer()).transform();
	}
	
	private final Map<String,Integer> commandLevels = new TreeMap<>();
	private final Map<String,Integer> commandLevelInsertions = new TreeMap<>();
	
	protected File getMinecraftJar() {
		return ServerDependencies.minecraftJar;
	}

	private List<String> findCommands(String command) {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"commands." + command + ".usage"}));

		return ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
	
	@Override
	protected void init() {
		commandLevels.put("whitelist", 6);
		commandLevels.put("ban", 6);
		commandLevels.put("banip", 6);
		commandLevels.put("banlist", 6);
		commandLevels.put("setidletimeout", 6);
		commandLevels.put("unban", 6);
		commandLevels.put("unbanip", 6);
		commandLevels.put("op", 5);
		commandLevels.put("deop", 5);
		commandLevels.put("kick", 5);
		
		commandLevelInsertions.put("save", 6);
		commandLevelInsertions.put("save-on", 6);
		commandLevelInsertions.put("save-off", 6);
		commandLevelInsertions.put("stop", 6);
	}
	
	public Map<IClassTransformer, String[]> getTransformers() throws IOException {
		Map<IClassTransformer, String[]> transformers = new HashMap<>();
		
		for (String key : commandLevels.keySet()) {
			Integer value = commandLevels.get(key);
			List<String> className = findCommands(key);
			if (className.size() == 0) {
				LOGGER.fatal("Could not find class for: " + key);
				throw new IllegalStateException();
			} else if (className.size() > 1) {
				LOGGER.fatal("Found more than one class for: " + key);
				throw new IllegalStateException();
			}
			String uniqueClassName = className.get(0).replace(".class","");
			transformers.put(new CommandLevelTransformer(value.intValue()), new String[] {uniqueClassName, key});
		}		
		
		for (String key : commandLevelInsertions.keySet()) {
			Integer value = commandLevelInsertions.get(key);
			List<String> className = findCommands(key);
			if (className.size() == 0) {
				LOGGER.fatal("Could not find class for: " + key);
				throw new IllegalStateException();
			} else if (className.size() > 1) {
				LOGGER.fatal("Found more than one class for: " + key);
				throw new IllegalStateException();
			}
			String uniqueClassName = className.get(0).replace(".class","");
			transformers.put(new CommandLevelInsertionTransformer(value.intValue()), new String[] {uniqueClassName, key});
		}	
		
		return transformers;
	}
}