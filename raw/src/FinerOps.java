
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FinerOps {

	private static final Logger LOGGER = LogManager.getLogger();

	public static final Set<String> gods = new HashSet<String>();
	public static final Set<String> godCommands = new HashSet<String>();
	public static final Set<String> superOps = new HashSet<String>();
	public static final Set<String> superOpCommands = new HashSet<String>();

	public static ll dedicatedPlayerList;

	/**
	 * Called from DedicatedPlayerList constructor.
	 * 
	 * @param dedicatedServer
	 */
	public static void load(lm dedicatedServer) {
		LOGGER.warn("Configuring Finer Ops ...");
		loadGodList(dedicatedServer);
		loadSuperOpsList(dedicatedServer);
		loadGodCommands(dedicatedServer);
		loadSuperOpCommands(dedicatedServer);
	}

	@SuppressWarnings("unchecked")
	private static void loadGodList(lm dedicatedServer)
	{
		try
		{
			File godList = dedicatedServer.d("gods.txt"); // getFile
			gods.clear();
			BufferedReader bufferedreader = new BufferedReader(new FileReader(godList));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				gods.add(s.trim().toLowerCase());
			}
			dedicatedPlayerList.i().addAll(gods); // add to ops set

			bufferedreader.close();
		}
		catch (Exception exception)
		{
			LOGGER.warn("Failed to load gods list: " + exception);
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadSuperOpsList(lm dedicatedServer)
	{
		try
		{
			File superOpsList = dedicatedServer.d("super-ops.txt");
			superOps.clear();
			superOps.addAll(gods);
			BufferedReader bufferedreader = new BufferedReader(new FileReader(superOpsList));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				superOps.add(s.trim().toLowerCase());
			}
			dedicatedPlayerList.i().addAll(superOps);

			bufferedreader.close();
		}
		catch (Exception exception)
		{
			LOGGER.warn("Failed to load super operators list: " + exception);
		}
	}

	private static void loadGodCommands(lm dedicatedServer)
	{
		if (gods.size() == 0) {
			LOGGER.warn("No gods specified. God-commands will not be available! You probably don't want this!");
		}
		try
		{
			File godList = dedicatedServer.d("god-commands.txt");
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

	private static void loadSuperOpCommands(lm dedicatedServer)
	{
		if (superOps.size() == 0) {
			LOGGER.warn("No super-ops specified. Super-op-commands will not be available! You probably don't want this!");
		}
		try
		{
			File godList = dedicatedServer.d("super-op-commands.txt");
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
	 * Also: ChatMessageComponent.func_111082_b("chat.type.text", new Object[] {this.playerEntity.getTranslatedEntityName(), var2})
	 */
	public static boolean canCommandSenderUseCommand(int par1, String par2Str, String username, mp entityPlayerMP)
	{
		if (godCommands.contains(par2Str.toLowerCase()) && !gods.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, gods);
			fi message = new fi("commands.generic.usage", new Object[] {new fi(mesg)});
			message.b().a(a.m);
			entityPlayerMP.b(message); // sendChatToPlayer
			return false;
		}
		if (superOpCommands.contains(par2Str.toLowerCase()) && !superOps.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, superOps);
			fi message = new fi("commands.generic.usage", new Object[] {new fi(mesg)});
			message.b().a(a.m);
			entityPlayerMP.b(message); // sendChatToPlayer
			return false;
		}            

		return true;
	}
	
	private static String toWarning(String command, Set<String> set) {
		StringBuilder builder = new StringBuilder();
		builder.append("" + a.m);
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
