package com.github.ttran17.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class AsmUtils {
	
	public static void main(String[] args) {
        ClassReader cr;
		try {
			String mcp = "mcp-1.6.4-811";
			String base = "/home/ttran/Projects/" + mcp + "/reobf/minecraft/";
			String base2 = "/home/ttran/Projects/" + mcp + "/bin/minecraft/";
//			cr = new ClassReader(new FileInputStream(base + "com/github/ttran17/opmods/FinerOps.class"));
//			cr = new ClassReader(new FileInputStream(base2 + "com/github/ttran17/opmods/FinerOps.class"));
			cr = new ClassReader(new FileInputStream(base + "bfq.class"));
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
