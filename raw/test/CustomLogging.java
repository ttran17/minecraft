import net.minecraft.server.MinecraftServer;


public class CustomLogging {

	public static void log(fa iChatComponent) {
		MinecraftServer.G().a(iChatComponent);
	}
	
	public void logg(fa iChatComponent) {
		MinecraftServer.G().a(iChatComponent);
	}
	
	public static void log() {
		fa iChatComponent = null;
		MinecraftServer.G().a(iChatComponent);			
	}
	
}
