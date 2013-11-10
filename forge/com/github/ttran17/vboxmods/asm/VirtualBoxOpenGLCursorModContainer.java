package com.github.ttran17.vboxmods.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class VirtualBoxOpenGLCursorModContainer extends DummyModContainer {

	private static final String mcVersionRange = "[1.6.4]";

	public VirtualBoxOpenGLCursorModContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId       = "VirtualBoxOpenGLCursor";
		meta.name        = "VirtualBoxOpenGLCursor";
		meta.version     = "1.6.4";
		meta.authorList  = Arrays.asList("KidGoldenArms");
		meta.description = "Auto-detect virtual box env & draw OpenGL cursor";
		meta.url         = "";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Override
	public VersionRange acceptableMinecraftVersionRange() {
		return VersionParser.parseRange(mcVersionRange);
	}
}
