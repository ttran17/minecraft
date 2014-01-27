package com.github.ttran17.logmods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.IChatComponent;

public class CustomLogging {
	
	private static final Logger LOGGER = LogManager.getLogger();

	/** 
	 * Called from net.minecraft.command.server.CommandMessage.processCommand()
	 * 
	 * @param from
	 * @param to
	 * @param message
	 */
	public static void logWhisper(IChatComponent from, IChatComponent to, IChatComponent message) {
		StringBuilder builder = new StringBuilder();
		builder.append("[CHAT] ");
		builder.append(from.getUnformattedText());
		builder.append(" whispers to ");
		builder.append(to.getUnformattedText());
		builder.append(" ");
		builder.append(message.getUnformattedText());
		builder.append(": ");
		
		LOGGER.info(builder.toString());
	}
	
	/**
	 * Called from net.minecraft.network.NetHandlerPlayServer.func_147354_a()
	 * 
	 * @param from
	 * @param message
	 */
	public static void logChat(IChatComponent from, String message) {
		StringBuilder builder = new StringBuilder();
		builder.append("[CHAT] ");
		builder.append(from.getUnformattedText());
		builder.append(" ");
		builder.append(message);
		
		LOGGER.info(builder.toString());
	}
}
