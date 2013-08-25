package com.github.ttran17.opmods.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class FinerOpsModContainer extends DummyModContainer {

	private static final String mcVersionRange = "[1.6.2]";

	public FinerOpsModContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId       = "FinerOps";
		meta.name        = "FinerOps";
		meta.version     = "1.6.2";
		meta.authorList  = Arrays.asList("KidGoldenArms");
		meta.description = "Allows finer control over ops roles / privileges";
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
