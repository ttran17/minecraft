package com.github.ttran17.vboxmods.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;;

@TransformerExclusions(value={"com.github.ttran17.vboxmods.asm"})
@MCVersion(value="")
public class VirtualBoxOpenGLCursorPlugin implements IFMLLoadingPlugin {

	public static File location;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
				MinecraftTransformer.class.getName(),
				GuiScreenTransformer.class.getName(),
				GuiContainerTransformer.class.getName()
		};
	}

	@Override
	public String getModContainerClass() {
		return VirtualBoxOpenGLCursorModContainer.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File) data.get("coremodLocation");
	}
	
	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
