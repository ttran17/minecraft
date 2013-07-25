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
			String base = "/home/ttran/Projects/forge-9.10.0.789/mcp/reobf/minecraft/";
			cr = new ClassReader(new FileInputStream(base + "awb.class"));
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
