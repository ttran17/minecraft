package com.github.ttran17.basemods.virtualbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ttran17.basemods.AbstractBytecodeTransformer;
import com.github.ttran17.basemods.IClassTransformer;
import com.github.ttran17.dependencies.ClientDependencies;
import com.github.ttran17.util.ClassSignature;
import com.github.ttran17.util.ModUtils;
import com.github.ttran17.util.ClassSignature.Signature;

/**
 * Main class for modifying raw (obfuscated / non-MCP) Minecraft bytecode
 * 
 * @author ttran
 */
public class VboxOpenGLTransformer extends AbstractBytecodeTransformer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void main(String[] args) throws IOException {
		LOGGER.info("Running " + VboxOpenGLTransformer.class.getSimpleName());
		
		(new VboxOpenGLTransformer()).transform();
	}
	
	private final Map<String, List<Signature>> signatureMap = new TreeMap<>();
	private final String Minecraft = "MineCraft";
	private final String GuiScreen = "GuiScreen";
	private final String GuiContainer = "GuiContainer";
	private final String Tessellator = "Tessellator";
	
	private final boolean transformDrawCursor = false;
	
	protected File getMinecraftJar() {
		return ClientDependencies.minecraftJar;
	}
	
	@Override
	protected void init() {
		{
			List<Signature> signatures = new ArrayList<>();
			signatures.add(new Signature("visitLdcInsn", new String[] {"textures/gui/title/mojang.png"}));
			
			signatureMap.put(Minecraft, signatures);
		}
		
		{
			List<Signature> signatures = new ArrayList<>();
			signatures.add(new Signature("visitLdcInsn", new String[] {"new Integer(-1072689136)"}));
			signatures.add(new Signature("visitLdcInsn", new String[] {"new Integer(-804253680)"}));
			
			signatureMap.put(GuiScreen, signatures);
		}
		
		{
			List<Signature> signatures = new ArrayList<>();
			signatures.add(new Signature("visitLdcInsn", new String[] {"textures/gui/container/inventory.png"}));

			signatureMap.put(GuiContainer, signatures);
		}
		
		{
			List<Signature> signatures = new ArrayList<>();
			signatures.add(new Signature("visitLdcInsn", new String[] {"Already building!"}));

			signatureMap.put(Tessellator, signatures);
		}
	}
	
	@Override
	public void transform() throws ZipException, IOException {
		super.transform();
		
		if (!transformDrawCursor) {
			LOGGER.warn("Not transforming the drawCursor() code in VirtualBoxOpenGLCursor. You'll have to do it manually.");
			return;
		}
		
		String tessellatorClassName = findClass(Tessellator);
		
		File file = new File("src","VirtualBoxOpenGLCursor.java");
		File ofile = new File("src","tmp");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));
		try {
			String line = reader.readLine();
			while (line != null) {
				if (line.contains("// tessellator instance")) {
					line = "        " + tessellatorClassName + " tessellator = " + tessellatorClassName + ".a; // tessellator instance";
				}
				writer.write(line + "\n");
				line = reader.readLine();
			}
		} finally {
			reader.close();
			writer.close();
		}
		
		if (file.delete()) {
			if (!ofile.renameTo(new File("src","VirtualBoxOpenGLCursor.java"))) {
				throw new IllegalStateException("Could not rename tmp to VirtualBoxOpenGLCursor.java");
			}
		} else {
			throw new IllegalStateException("Could not delete old VirtualBoxOpenGLCursor.java");
		}
	}
	
	private String findClass(String deobfuscatedClassName) {
		List<Signature> signatures = signatureMap.get(deobfuscatedClassName);

		List<String> className = ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
		if (className.size() == 0) {
			LOGGER.fatal("Could not find class for: " + deobfuscatedClassName);
			throw new IllegalStateException();
		} else if (className.size() > 1) {
			LOGGER.fatal("Found more than one class for: " + deobfuscatedClassName);
			throw new IllegalStateException();
		}
		return className.get(0).replace(".class","");
	}

	@Override
	protected Map<IClassTransformer, String[]> getTransformers() throws IOException {
		Map<IClassTransformer, String[]> transformers = new HashMap<>();

		String minecraftClassName = findClass(Minecraft);
		transformers.put(new MinecraftTransformer(minecraftClassName), new String[] {minecraftClassName, Minecraft});

		String guiScreenClassName = findClass(GuiScreen);
		transformers.put(new GuiScreenTransformer(guiScreenClassName, minecraftClassName), new String[] {guiScreenClassName, GuiScreen});

		String guiContainerClassName = findClass(GuiContainer);
		transformers.put(new GuiContainerTransformer(guiContainerClassName, guiScreenClassName, minecraftClassName), new String[] {guiContainerClassName, GuiContainer});

		return transformers;
	}
}