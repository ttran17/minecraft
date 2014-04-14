package com.github.ttran17.dependencies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.github.ttran17.dependencies.ServerDependencies;
import com.github.ttran17.util.AsmUtils;
import com.github.ttran17.util.ClassSignature;
import com.github.ttran17.util.ModUtils;
import com.github.ttran17.util.ClassSignature.Signature;

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
	public void findEntityPlayerMPClass() {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"\"tell\""}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"\"help\""}));
		signatures.add(new Signature("visitLdcInsn", new String[] {"\"me\""}));

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
	
	@Test
	public void checkClassInJar() throws IOException {
		AsmUtils.toTraceClassVisitor(AsmUtils.readClass(ServerDependencies.minecraftJar,"cc"));
	}
	
	@Test
	public void checkClass() throws IOException {
//		AsmUtils.toTraceClassVisitor(AsmUtils.readClass("/home/ttran/Projects/minecraft/raw/bin/com/github/ttran17/basemods/finerops","CommandTransformer.class"));
		AsmUtils.toTraceClassVisitor(AsmUtils.readClass("/home/ttran/Projects/staging/raw/1.7.8/","bj.class"));
		
	}
	
	
	private String findCommands(String command) {
		List<Signature> signatures = new ArrayList<>();
		signatures.add(new Signature("visitLdcInsn", new String[] {"commands." + command + ".usage"}));

		return ModUtils.findClass(minecraftJar, new ClassSignature(signatures));
	}
	
	@Test
	public void findCommands() {
		Map<String,Integer> commandLevels = new TreeMap<>();
		
		commandLevels.put("whitelist", 6);
		commandLevels.put("ban", 6);
		commandLevels.put("banip", 6);
		commandLevels.put("banlist", 6);
		commandLevels.put("setidletimeout", 6);
		commandLevels.put("unban", 6);
		commandLevels.put("unbanip", 6);
		
		commandLevels.put("save", 6);
		commandLevels.put("save-on", 6);
		commandLevels.put("save-off", 6);
		commandLevels.put("stop", 6);
		
		commandLevels.put("op", 5);
		commandLevels.put("deop", 5);
		commandLevels.put("kick", 5);
		
		for (String key : commandLevels.keySet()) {
			Integer value = commandLevels.get(key);
			String className = findCommands(key).replace(".class", "");
			System.out.println("Classname is: " + className);
		}
	}
	
}
