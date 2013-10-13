package com.github.ttran17.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class ASMUtils {
	public static void main(String[] args) {
        ClassReader cr;
		try {
			String forge = "forge-9.11.0883";
			String base = "/home/ttran/Projects/" + forge + "/mcp/reobf/minecraft/";
			String base2 = "/home/ttran/Projects/" + forge + "/mcp/bin/minecraft/";
//			cr = new ClassReader(new FileInputStream(base + "com/github/ttran17/servermods/FinerOps.class"));
//			cr = new ClassReader(new FileInputStream(base2 + "com/github/ttran17/servermods/FinerOps.class"));
			cr = new ClassReader(new FileInputStream(base + "awy.class"));
//			cr = new ClassReader(new FileInputStream("axr.class"));
	        cr.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(
	                System.out)),  ClassReader.SKIP_DEBUG);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
