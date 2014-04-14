import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.OpList;
import net.minecraft.server.OpListEntry;
import net.minecraft.server.WhiteList;
import net.minecraft.server.WhiteListEntry;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.authlib.GameProfile;

public class ConvertTxtToJson {

	public static void main(String[] args) {

		if (args != null && args.length > 0) {
			System.err.println("Too many arguments!");
			System.err.println("Usage: java -jar foo.jar");
			System.exit(-1);
		}
		
		File opTxtFile = new File("/home/ttran/Minecraft/vanilla-server/versions/1.7.8","ops.txt");
		if (!opTxtFile.exists()) {
			System.err.println("Cannot find ops.txt -- no conversion possible.");
		} else {
			System.err.println("Converting to ops.json: " + opTxtFile.getAbsolutePath());
			try {
				OpList opList = new OpList(new File("ops.json"));				
				List<GameProfile> gameProfiles = toJsonObjects(opTxtFile);
				for (GameProfile gameProfile : gameProfiles) {					
					OpListEntry entry = new OpListEntry(gameProfile, 4);
					opList.add(entry);
				}				
				opList.save();
				
			} catch (IOException e) {
				System.err.println("Unexpected IOException ...");
				e.printStackTrace();
			}
		}
		
		File whitelistTxtFile = new File("/home/ttran/Minecraft/vanilla-server/versions/1.7.8","white-list.txt");
		if (!whitelistTxtFile.exists()) {
			System.err.println("Cannot find white-list.txt -- no conversion possible.");
		} else {
			System.err.println("Converting to whitelist.json: " + whitelistTxtFile.getAbsolutePath());
			try {
				WhiteList whiteList = new WhiteList(new File("whitelist.json"));				
				List<GameProfile> gameProfiles = toJsonObjects(whitelistTxtFile);
				for (GameProfile gameProfile : gameProfiles) {					
					WhiteListEntry entry = new WhiteListEntry(gameProfile);
					whiteList.add(entry);
				}				
				whiteList.save();
				
			} catch (IOException e) {
				System.err.println("Unexpected IOException ...");
				e.printStackTrace();
			}
		}		
	}
	
	private static List<GameProfile> toJsonObjects(File file) throws IOException {
		List<String> names = Files.readLines(file, Charsets.UTF_8);
		HttpProfileRepository rep = new HttpProfileRepository("minecraft");
		Profile[] profiles = rep.findProfilesByNames(names.toArray(new String[0]));
		List<GameProfile> gameProfiles = new ArrayList<>();
		for (Profile profile : profiles) {
			String id = profile.getId();
			UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));            
			GameProfile gameProfile = new GameProfile(uuid, profile.getName());
			gameProfiles.add(gameProfile);
		}
		return gameProfiles;
	}
}
