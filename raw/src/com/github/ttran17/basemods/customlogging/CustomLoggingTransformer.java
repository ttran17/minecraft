package com.github.ttran17.basemods.customlogging;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ttran17.basemods.AbstractBytecodeTransformer;
import com.github.ttran17.basemods.IClassTransformer;
import com.github.ttran17.dependencies.ServerDependencies;

public class CustomLoggingTransformer extends AbstractBytecodeTransformer {

	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) throws IOException {
		LOGGER.info("Running " + CustomLoggingTransformer.class.getSimpleName());
		
		new CustomLoggingTransformer();
	}
	
	@Override
	protected File getMinecraftJar() {
		return ServerDependencies.minecraftJar;
	}

	@Override
	protected Map<IClassTransformer, String[]> getTransformers() throws IOException {
		Map<IClassTransformer, String[]> transformers = new HashMap<>();
		transformers.put(new CommandMessageTransformer(), new String[] {CommandMessageTransformer.CommandMessage_classname,"CommandMessage"});

		return transformers;
	}

}
