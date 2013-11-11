package com.github.ttran17.basemods;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractBytecodeTransformer {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	protected static final String version = "1.7.2";

	protected static final String pathOut = "/home/ttran/Projects/staging/raw/" + version;
	
	protected final File minecraftJar;
	
	protected abstract String getJarName();
	protected abstract Map<IClassTransformer, String[]> getTransformers() throws IOException;
	
	public AbstractBytecodeTransformer() {
		this.minecraftJar = new File(getJarName());
		
		try {
			transform();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void transform() throws ZipException, IOException {
		Map<IClassTransformer, String[]> transformers = getTransformers();

		for (IClassTransformer transformer : transformers.keySet()) {
			String[] names = transformers.get(transformer);
			transform(transformer, names[0], names[1]);
		}
	}
	
	public void transform(IClassTransformer transformer, String name, String transformedName) throws IOException {
		byte[] bytes = readClass(name);
		bytes = transformer.transform(name, transformedName, bytes);
		writeClass(name, bytes);
	}

	public byte[] readClass(String name) throws IOException {
		byte[] bytes = null;

		ZipFile zip = new ZipFile(minecraftJar);
		ZipEntry entry = zip.getEntry(name + ".class");
		if (entry == null) {
			LOGGER.error(name + " not found at " + minecraftJar.getName());
		} else {
			DataInputStream zin = new DataInputStream(zip.getInputStream(entry));
			bytes = new byte[(int) entry.getSize()];
			zin.readFully(bytes);
		}
		zip.close();

		return bytes;
	}

	public void writeClass(String classname, byte[] byteArray) throws IOException {
		File file = new File(pathOut,classname + ".class");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(byteArray);
		fos.close();
	}
}
