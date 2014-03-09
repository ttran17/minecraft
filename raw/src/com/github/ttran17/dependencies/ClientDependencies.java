package com.github.ttran17.dependencies;

import java.io.File;

import com.github.ttran17.util.ModUtils;

public class ClientDependencies {

	public static final String client = "/home/ttran/Minecraft/vanilla/versions/";
	
	public static final String dir = client + ModUtils.version + "/";
	
	public static final String filename = ModUtils.version + ".jar";
	
	public static final File minecraftJar = new File(dir,filename);
	
}
