package com.github.ttran17.vboxmods;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;;

@TransformerExclusions(value={"com.github.ttran17.vboxmods"})
@MCVersion(value="")
public class VirtualBoxOpenGLCursorPlugin implements IFMLLoadingPlugin {

	public static File location;

	@Override
	public String[] getLibraryRequestClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
				"com.github.ttran17.vboxmods.MinecraftTransformer",
				"com.github.ttran17.vboxmods.GuiScreenTransformer"
		};
	}

	@Override
	public String getModContainerClass() {
		return "com.github.ttran17.vboxmods.VirtualBoxOpenGLCursorModContainer";
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File) data.get("coremodLocation");
	}

}
