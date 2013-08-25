package com.github.ttran17.opmods.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value={"com.github.ttran17.opmods.asm"})
@MCVersion(value="")
public class FinerOpsPlugin implements IFMLLoadingPlugin {
	
	public static File location;

	@Override
	public String[] getLibraryRequestClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
				DedicatedPlayerListTransformer.class.getName(),
				EntityPlayerMPTransformer.class.getName()			
		};
	}

	@Override
	public String getModContainerClass() {
		return FinerOpsModContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File) data.get("coremodLocation");
	}

}

