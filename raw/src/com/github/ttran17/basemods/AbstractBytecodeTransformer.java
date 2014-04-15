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

import com.github.ttran17.util.ModUtils;

public abstract class AbstractBytecodeTransformer {
	
	private static final Logger LOGGER = LogManager.getLogger();

	protected static final String pathOut = "/home/ttran/Projects/staging/raw/" + ModUtils.version;
	
	protected final File minecraftJar;
	
	protected abstract File getMinecraftJar();
	protected abstract Map<IClassTransformer, String[]> getTransformers() throws IOException;
	
	public AbstractBytecodeTransformer() {
		this.minecraftJar = getMinecraftJar();
	}
	
	protected void init() {
		
	};

	public void transform() throws ZipException, IOException {
		init();
		
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
			zip.close();
			LOGGER.fatal(name + " not found at " + minecraftJar.getName());
			throw new IllegalStateException();
		} else {
			DataInputStream zin = new DataInputStream(zip.getInputStream(entry));
			bytes = new byte[(int) entry.getSize()];
			zin.readFully(bytes);
			zin.close();
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
