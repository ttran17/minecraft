package com.github.ttran17.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class ModUtils {
	
	private static final Logger LOGGER = LogManager.getLogger();
		
	public static final String version = "1.8";
	
	public static List<String> findClass(File minecraftJar, ClassSignature signature) {
		List<String> possibleMatches = new ArrayList<>();
		try {
			ZipFile zip = new ZipFile(minecraftJar);
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					DataInputStream zin = new DataInputStream(zip.getInputStream(entry));
					byte[] bytes = new byte[(int) entry.getSize()];
					zin.readFully(bytes);
					zin.close();

					PrintWriter pw = null;
					BufferedReader reader = null;
					try {		
						pw = new PrintWriter(new FileWriter(new File("tmp")));			
						ClassReader cr = new ClassReader(bytes);
						cr.accept(new TraceClassVisitor(null, 
								new ASMifier(), 
								pw),
								ClassReader.SKIP_DEBUG);	        
						pw.close();
						pw = null;
						
						reader = new BufferedReader(new FileReader(new File("tmp")));
						int matches = signature.check(reader);
						if (matches >= signature.getMinMatches()) {
							LOGGER.info("Possible match: " + entry.getName());
							possibleMatches.add(entry.getName());
						}
						reader.close();	
						reader = null;										
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (pw != null) {
							pw.close();						
						}
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}				
				}
			}
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return possibleMatches;
	}

}
