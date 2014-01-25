package com.github.ttran17.opmods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class FinerOps {
	
	private static final Logger LOGGER = LogManager.getLogger();

	public static final Set<String> gods = new HashSet<String>();
	public static final Set<String> godCommands = new HashSet<String>();
	public static final Set<String> superOps = new HashSet<String>();
	public static final Set<String> superOpCommands = new HashSet<String>();

	public static DedicatedPlayerList dedicatedPlayerList;
	
	/**
	 * Called from DedicatedPlayerList constructor.
	 * 
	 * @param par1DedicatedServer
	 */
	public static void load(DedicatedServer par1DedicatedServer) {
        LOGGER.warn("Configuring Finer Ops ...");
		loadGodList(par1DedicatedServer);
		loadSuperOpsList(par1DedicatedServer);
		loadGodCommands(par1DedicatedServer);
		loadSuperOpCommands(par1DedicatedServer);
	}

	private static void loadGodList(DedicatedServer par1DedicatedServer)
	{
		try
		{
			File godList = par1DedicatedServer.getFile("gods.txt");
			gods.clear();
			BufferedReader bufferedreader = new BufferedReader(new FileReader(godList));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				gods.add(s.trim().toLowerCase());
			}
			dedicatedPlayerList.getOps().addAll(gods);

			bufferedreader.close();
		}
		catch (Exception exception)
		{
			LOGGER.warn("Failed to load gods list: " + exception);
		}
	}

	private static void loadSuperOpsList(DedicatedServer par1DedicatedServer)
	{
		try
		{
			File superOpsList = par1DedicatedServer.getFile("super-ops.txt");
			superOps.clear();
			superOps.addAll(gods);
			BufferedReader bufferedreader = new BufferedReader(new FileReader(superOpsList));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				superOps.add(s.trim().toLowerCase());
			}
			dedicatedPlayerList.getOps().addAll(superOps);

			bufferedreader.close();
		}
		catch (Exception exception)
		{
			LOGGER.warn("Failed to load super operators list: " + exception);
		}
	}

	private static void loadGodCommands(DedicatedServer par1DedicatedServer)
	{
		if (gods.size() == 0) {
			LOGGER.warn("No gods specified. Therefore, no commands reserved for gods.");
			return;
		}
		try
		{
			File godList = par1DedicatedServer.getFile("god-commands.txt");
			godCommands.clear();
			BufferedReader bufferedreader = new BufferedReader(new FileReader(godList));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				godCommands.add(s.trim().toLowerCase());
			}

			bufferedreader.close();
		}
		catch (Exception exception)
		{
			LOGGER.warn("Failed to load god commands list: " + exception);
		}
	}

	private static void loadSuperOpCommands(DedicatedServer par1DedicatedServer)
	{
		if (superOps.size() == 0) {
			LOGGER.warn("No super-ops specified. Therefore, no commands reserved for super-ops.");
			return;
		}
		try
		{
			File godList = par1DedicatedServer.getFile("super-op-commands.txt");
			superOpCommands.clear();
			BufferedReader bufferedreader = new BufferedReader(new FileReader(godList));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				superOpCommands.add(s.trim().toLowerCase());
			}

			bufferedreader.close();
		}
		catch (Exception exception)
		{
			LOGGER.warn("Failed to load super-op commands list: " + exception);
		}
	}

	/**
	 * Returns true if the command sender is allowed to use the given command. 
	 * <p>
	 * Called from EntityPlayerMP.canCommandSenderUseCommand():
     *
     *    public boolean canCommandSenderUseCommand(int par1, String par2Str)
     *    {
     *       return FinerOpsConfigurationManager.canCommandSenderUseCommand(par1, par2Str, this.username, this));
     *		 // return "seed".equals(par2Str) && !this.mcServer.isDedicatedServer() ? true : (!"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str) ? (this.mcServer.getConfigurationManager().areCommandsAllowed(this.username) ? this.mcServer.func_110455_j() >= par1 : false) : true);
     *    }
	 * 
	 */
	public static boolean canCommandSenderUseCommand(int par1, String par2Str, String username, EntityPlayerMP entityPlayerMP)
	{
		if (godCommands.contains(par2Str.toLowerCase()) && !gods.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, gods);
			ChatComponentTranslation component = new ChatComponentTranslation(
					"commands.generic.usage", 
					new Object[] {new ChatComponentTranslation(mesg)});
            component.getChatStyle().setColor(EnumChatFormatting.RED);
			entityPlayerMP.addChatMessage(component);
			return false;
		}
		if (superOpCommands.contains(par2Str.toLowerCase()) && !superOps.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, superOps);
			ChatComponentTranslation component = new ChatComponentTranslation(
					"commands.generic.usage", 
					new Object[] {new ChatComponentTranslation(mesg)});
            component.getChatStyle().setColor(EnumChatFormatting.RED);
			entityPlayerMP.addChatMessage(component);
			return false;
		}    	

		return canCommandSenderUseCommand(par1, par2Str, entityPlayerMP);
	}
	
	private static boolean canCommandSenderUseCommand(int par1, String par2Str, EntityPlayerMP entityPlayerMP) {
		 return "seed".equals(par2Str) && !entityPlayerMP.mcServer.isDedicatedServer() ? true : (!"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str) ? (entityPlayerMP.mcServer.getConfigurationManager().isPlayerOpped(entityPlayerMP.getCommandSenderName()) ? entityPlayerMP.mcServer.getOpPermissionLevel() >= par1 : false) : true);
	}

	private static String toWarning(String command, Set<String> set) {
		StringBuilder builder = new StringBuilder();
		builder.append("" + EnumChatFormatting.RED);
		builder.append("Sorry. On this server the command \"");
		builder.append(command);
		builder.append("\" is restricted to ");
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {   
			builder.append((String) iter.next());
			if (iter.hasNext()) {
				builder.append(", ");
			} else {
				builder.append(".");
			}
		}
		return builder.toString();
	}
}
