package com.github.ttran17.dependencies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.ttran17.util.AsmUtils;
import com.github.ttran17.util.ClassSignature;
import com.github.ttran17.util.ModUtils;
import com.github.ttran17.util.ClassSignature.Signature;

public class CustomLoggingDependencies {
	private static final File minecraftJar = ServerDependencies.minecraftJar;
	
	@Test
	public void findNetHandlerPlayServer() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"chat.cannotSend"}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"Illegal characters in chat"}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"chat.type.text"}));
		
		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}

	@Test
	public void checkClassInJar() throws IOException {
		AsmUtils.toTraceClassVisitor(AsmUtils.readClass(ServerDependencies.minecraftJar,"ng"));
	}
	
	@Test
	public void findCommandMessage() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"commands.message.display.incoming"}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"commands.message.display.outgoing"}));
		
		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
}
