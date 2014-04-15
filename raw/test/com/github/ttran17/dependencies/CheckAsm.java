package com.github.ttran17.dependencies;

import java.io.IOException;

import org.junit.Test;

import com.github.ttran17.util.AsmUtils;

public class CheckAsm {
	@Test
	public void checkClassInJar() throws IOException {
		AsmUtils.toTraceClassVisitor(AsmUtils.readClass(ServerDependencies.minecraftJar,"cc"));
	}
	
	@Test
	public void checkClass() throws IOException {
//		AsmUtils.toTraceClassVisitor(AsmUtils.readClass("/home/ttran/Projects/minecraft/raw/bin/com/github/ttran17/basemods/finerops","CommandTransformer.class"));
		AsmUtils.toTraceClassVisitor(AsmUtils.readClass("/home/ttran/Projects/staging/raw/1.7.8/","bj.class"));
		
	}
}
