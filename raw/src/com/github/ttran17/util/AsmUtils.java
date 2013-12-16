package com.github.ttran17.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class AsmUtils {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static TraceClassVisitor traceClassVisitor = new TraceClassVisitor(
			null, 
			new ASMifier(), 
			new PrintWriter(System.out));
	
	public static byte[] readClass(String dir, String jarfilename, String name) throws IOException {
		File jarfile = new File(dir,jarfilename);
		return readClass(jarfile, name);
	}
	
	public static byte[] readClass(File jarfile, String name) throws IOException {
		byte[] bytes = null;

		ZipFile zip = new ZipFile(jarfile);
		ZipEntry entry = zip.getEntry(name + ".class");
		if (entry == null) {
			LOGGER.error(name + " not found at " + jarfile.getName());
		} else {
			DataInputStream zin = new DataInputStream(zip.getInputStream(entry));
			bytes = new byte[(int) entry.getSize()];
			zin.readFully(bytes);
			zin.close();
		}
		zip.close();

		return bytes;
	}
	
	public static FileInputStream readClass(String dir, String name) throws FileNotFoundException {
		return new FileInputStream(new File(dir,name));
	}
	
	public static void toTraceClassVisitor(byte[] bytes) {
		ClassReader cr = new ClassReader(bytes);
		cr.accept(traceClassVisitor, ClassReader.SKIP_DEBUG);
	}
	
	public static void toTraceClassVisitor(FileInputStream stream) throws IOException {
		ClassReader cr = new ClassReader(stream);
		cr.accept(traceClassVisitor, ClassReader.SKIP_DEBUG);
	}
}
