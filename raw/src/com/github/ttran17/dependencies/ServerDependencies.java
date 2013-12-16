package com.github.ttran17.dependencies;

import java.io.File;

import com.github.ttran17.util.ModUtils;

public class ServerDependencies {
	
	public static final String server = "/home/ttran/vanilla-minecraft-server/versions/";
	
	public static final String dir = server + ModUtils.version + "/";
	
	public static final String filename = "minecraft_server." + ModUtils.version + ".jar";
	
	public static final File minecraftJar = new File(dir,filename);
	
}
