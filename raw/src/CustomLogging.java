

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomLogging {
	
	private static final Logger LOGGER = LogManager.getLogger();

	/** 
	 * Called from net.minecraft.command.server.CommandMessage.processCommand()
	 * 
	 * @param from IChatComponent_classname
	 * @param to IChatComponent_classname
	 * @param message IChatComponent_classname
	 */
	public static void logWhisper(fg from, fg to, fg message) {
		StringBuilder builder = new StringBuilder();
		builder.append("[CHAT] ");
		builder.append(from.c());
		builder.append(" whispers to ");
		builder.append(to.c());
		builder.append(": ");
		builder.append(message.c());
		
		LOGGER.info(builder.toString());
	}
	
	/**
	 * Called from net.minecraft.network.NetHandlerPlayServer.func_147354_a()
	 * 
	 * @param from IChatComponent_classname
	 * @param message
	 */
	public static void logChat(fg from, String message) {
		StringBuilder builder = new StringBuilder();
		builder.append("[CHAT] ");
		builder.append(from.c());
		builder.append(" ");
		builder.append(message);
		
		LOGGER.info(builder.toString());
	}
}
