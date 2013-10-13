package com.github.ttran17.opmods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class FinerOps {

	public static final Set<String> gods = new HashSet<String>();
	public static final Set<String> godCommands = new HashSet<String>();
	public static final Set<String> superOps = new HashSet<String>();
	public static final Set<String> superOpCommands = new HashSet<String>();

	public static DedicatedPlayerList dedicatedPlayerList;
	
	/**
	 * Called from DedicatedPlayerList constructor.
	 * 
	 * @param dedicatedServer
	 */
	public static void load(DedicatedServer dedicatedServer) {
		dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Configuring Finer Ops ...");
		loadGodList(dedicatedServer);
		loadSuperOpsList(dedicatedServer);
		loadGodCommands(dedicatedServer);
		loadSuperOpCommands(dedicatedServer);
	}

	private static void loadGodList(DedicatedServer dedicatedServer)
	{
		try
		{
			File godList = dedicatedServer.getFile("gods.txt");
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
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load gods list: " + exception);
		}
	}

	private static void loadSuperOpsList(DedicatedServer dedicatedServer)
	{
		try
		{
			File superOpsList = dedicatedServer.getFile("super-ops.txt");
			superOps.clear();
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
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load super operators list: " + exception);
		}
	}

	private static void loadGodCommands(DedicatedServer dedicatedServer)
	{
		if (gods.size() == 0) {
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("No gods specified. Therefore, no commands reserved for gods.");
			return;
		}
		try
		{
			File godList = dedicatedServer.getFile("god-commands.txt");
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
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load god commands list: " + exception);
		}
	}

	private static void loadSuperOpCommands(DedicatedServer dedicatedServer)
	{
		if (superOps.size() == 0) {
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("No super-ops specified. Therefore, no commands reserved for super-ops.");
			return;
		}
		try
		{
			File godList = dedicatedServer.getFile("super-op-commands.txt");
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
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load super-op commands list: " + exception);
		}
	}

	/**
	 * Returns true if the command sender is allowed to use the given command. 
	 * <p>
	 * Called from EntityPlayerMP.canCommandSenderUseCommand():
     *
     *    public boolean canCommandSenderUseCommand(int par1, String par2Str)
     *    {
     *       if (FinerOpsConfigurationManager.canCommandSenderUseCommand(par1, par2Str, this.username, playerNetServerHandler)) {
     *		 return "seed".equals(par2Str) && !this.mcServer.isDedicatedServer() ? true : (!"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str) ? (this.mcServer.getConfigurationManager().areCommandsAllowed(this.username) ? this.mcServer.func_110455_j() >= par1 : false) : true);
     *	  } else {
     *		return false;
     *	  }
     *
	 * 
	 * Also: ChatMessageComponent.func_111082_b("chat.type.text", new Object[] {this.playerEntity.getTranslatedEntityName(), var2})
	 */
	public static boolean canCommandSenderUseCommand(int par1, String par2Str, String username, NetServerHandler playerNetServerHandler)
	{
		if (godCommands.contains(par2Str.toLowerCase()) && !gods.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, gods);
			ChatMessageComponent par1ChatMessageComponent = ChatMessageComponent.createFromTranslationWithSubstitutions("commands.generic.usage", 
					new Object[] {ChatMessageComponent.createFromTranslationWithSubstitutions(mesg)}).setColor(EnumChatFormatting.RED);
			playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(par1ChatMessageComponent));
			return false;
		}
		if (superOpCommands.contains(par2Str.toLowerCase()) && !superOps.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, superOps);
			ChatMessageComponent par1ChatMessageComponent = ChatMessageComponent.createFromTranslationWithSubstitutions("commands.generic.usage", 
					new Object[] {ChatMessageComponent.createFromTranslationWithSubstitutions(mesg)}).setColor(EnumChatFormatting.RED);
			playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(par1ChatMessageComponent));
			return false;
		}    	

		return true;
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
