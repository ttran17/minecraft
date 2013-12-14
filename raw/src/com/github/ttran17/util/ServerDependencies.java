package com.github.ttran17.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.ttran17.util.ClassSignature.Signature;

public class ServerDependencies {
	
	public static final String server = "/home/ttran/vanilla-minecraft-server/versions/";
	
	public static final String dir = server + ModUtils.version + "/";
	
	public static final String filename = "minecraft_server." + ModUtils.version + ".jar";
	
	public static final File minecraftJar = new File(dir,filename);
	
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
	
}
