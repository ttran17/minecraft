
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

//	public static final Set<String> gods = new HashSet<String>();
	public static final Set<String> godCommands = new HashSet<String>();
//	public static final Set<String> superOps = new HashSet<String>();
	public static final Set<String> superOpCommands = new HashSet<String>();

	public static lr dedicatedPlayerList;
	
	public static final oi gods = new oi(null);

	/**
	 * Called from DedicatedPlayerList constructor.
	 * 
	 * @param dedicatedServer
	 */
	public static void load(ls dedicatedServer) {
		LOGGER.warn("Configuring Finer Ops ...");
		loadGodList(dedicatedServer);
		loadSuperOpsList(dedicatedServer);
		loadGodCommands(dedicatedServer);
		loadSuperOpCommands(dedicatedServer);
	}

	@SuppressWarnings("unchecked")
	private static void loadGodList(ls dedicatedServer)
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
	private static void loadSuperOpsList(ls dedicatedServer)
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

	private static void loadGodCommands(ls dedicatedServer)
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

	private static void loadSuperOpCommands(ls dedicatedServer)
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
	 * This is some sort of ChatMessageComponent ...
	 */
	public static boolean canCommandSenderUseCommand(int par1, String par2Str, String username, mv entityPlayerMP)
	{
		if (godCommands.contains(par2Str.toLowerCase()) && !gods.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, gods);
			fr message = new fr("commands.generic.usage", new Object[] {new fr(mesg)}); // ChatMessageComponent ?
			message.b().a(a.m);
			entityPlayerMP.b(message); // sendChatToPlayer ; around line 665 in entityPlayerMP
			return false;
		}
		if (superOpCommands.contains(par2Str.toLowerCase()) && !superOps.contains(username.toLowerCase())) {
			String mesg = toWarning(par2Str, superOps);
			fr message = new fr("commands.generic.usage", new Object[] {new fr(mesg)}); // ChatMessageComponent ?
			message.b().a(a.m);
			entityPlayerMP.b(message); // sendChatToPlayer ; around line 665 in entityPlayerMP
			return false;
		}  

		return canCommandSenderUseCommand(par1, par2Str, entityPlayerMP);
	}
	
	/**
	 * Mojang's original method.
	 * 
	 * @param paramInt
	 * @param paramString
	 * @param entityPlayerMP
	 * @return
	 */
	public static boolean canCommandSenderUseCommand(int paramInt, String paramString, mv entityPlayerMP) {
		if (("seed".equals(paramString)) && (!entityPlayerMP.b.X())) {
			return true;
		}
		if (("tell".equals(paramString)) || ("help".equals(paramString)) || ("me".equals(paramString))) return true;
	    if (entityPlayerMP.b.ah().g(entityPlayerMP.bI())) {
	        oj localoj = (oj) entityPlayerMP.b.ah().m().b((Object) entityPlayerMP.bI());
	        if (localoj != null) {
	          return localoj.a() >= paramInt;
	        }
	        return entityPlayerMP.b.l() >= paramInt;
	      }
		return false;
	}
	
	private static String toWarning(String command, Set<String> set) {
		StringBuilder builder = new StringBuilder();
		builder.append("" + a.m);
		builder.append("Sorry. On this server you do not have the necessary privileges to use '");
		builder.append(command);
		builder.append("'.");
		return builder.toString();
	}

}
