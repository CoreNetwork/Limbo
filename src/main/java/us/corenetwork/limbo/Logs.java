package us.corenetwork.limbo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logs
{
	public static void debug(String text)
	{
		if (Settings.DEBUG.bool())
			sendLog("&f[&3Limbo&f]&f "+text);
	}

	public static void info(String text)
	{
		sendLog("&f[&fLimbo&f]&f "+text);
	}

	public static void warning(String text)
	{
		sendLog("&f[&eLimbo&f]&f " + text);
	}

	public static void severe(String text)
	{
		sendLog("&f[&cLimbo&f]&f " + text);
	}

	public static void sendLog(String text)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	}

}
