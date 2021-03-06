package com.github.ttran17.dependencies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.ttran17.dependencies.ServerDependencies;
import com.github.ttran17.util.ClassSignature;
import com.github.ttran17.util.ModUtils;
import com.github.ttran17.util.ClassSignature.Signature;

@Deprecated
public class FinerOpsDependencies {
	
	private static final File minecraftJar = ServerDependencies.minecraftJar;
	
	@Test
	public void findDedicatedPlayerListClass() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"ops.txt"}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"white-list.txt"}));

		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
	
	@Test
	public void findChatMessageComponent() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"TranslatableComponent"}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"siblings"}));

		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
	
	@Test
	public void findSendChatToPlayerExample() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"commands.generic.usage"}));

		ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
	
}
