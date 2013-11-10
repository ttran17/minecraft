package com.github.ttran17.opmods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.DedicatedPlayerList;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumChatFormatting;

public class FinerOps {

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
		dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Configuring Finer Ops ...");
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
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load gods list: " + exception);
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
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load super operators list: " + exception);
		}
	}

	private static void loadGodCommands(DedicatedServer par1DedicatedServer)
	{
		if (gods.size() == 0) {
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("No gods specified. Therefore, no commands reserved for gods.");
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
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load god commands list: " + exception);
		}
	}

	private static void loadSuperOpCommands(DedicatedServer par1DedicatedServer)
	{
		if (superOps.size() == 0) {
			dedicatedPlayerList.getDedicatedServerInstance().getLogAgent().logWarning("No super-ops specified. Therefore, no commands reserved for super-ops.");
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
     *       if (FinerOpsConfigurationManager.canCommandSenderUseCommand(par1, par2Str, this.username, this)) {
     *		 return "seed".equals(par2Str) && !this.mcServer.isDedicatedServer() ? true : (!"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str) ? (this.mcServer.getConfigurationManager().areCommandsAllowed(this.username) ? this.mcServer.func_110455_j() >= par1 : false) : true);
     *	  } else {
     *		return false;
     *	  }
     *
	 * 
	 * Also: ChatMessageComponent.func_111082_b("chat.type.text", new Object[] {this.playerEntity.getTranslatedEntityName(), var2})
	 */
	public static boolean canCommandSenderUseCommand(int par1, String par2Str, String username, EntityPlayerMP entityPlayerMP)
	{
		if (godCommands.contains(par2Str.toLowerCase()) && !gods.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, gods);
			ChatMessageComponent par1ChatMessageComponent = ChatMessageComponent.createFromTranslationWithSubstitutions("commands.generic.usage", 
					new Object[] {ChatMessageComponent.createFromTranslationWithSubstitutions(mesg)}).setColor(EnumChatFormatting.RED);
			entityPlayerMP.sendChatToPlayer(par1ChatMessageComponent);
			return false;
		}
		if (superOpCommands.contains(par2Str.toLowerCase()) && !superOps.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, superOps);
			ChatMessageComponent par1ChatMessageComponent = ChatMessageComponent.createFromTranslationWithSubstitutions("commands.generic.usage", 
					new Object[] {ChatMessageComponent.createFromTranslationWithSubstitutions(mesg)}).setColor(EnumChatFormatting.RED);
			entityPlayerMP.sendChatToPlayer(par1ChatMessageComponent);
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
