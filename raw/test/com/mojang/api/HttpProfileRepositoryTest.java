package com.mojang.api;

import org.junit.Test;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;

public class HttpProfileRepositoryTest {

	@Test
	public void check() {
		HttpProfileRepository rep = new HttpProfileRepository("minecraft");
		Profile[] profiles = rep.findProfilesByNames("runningdolphin");
		
		for (Profile profile : profiles) {
			System.out.println("Name: " + profile.getName() + ", id: " + profile.getId());
		}
	}
}
