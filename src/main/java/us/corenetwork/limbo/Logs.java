package us.corenetwork.limbo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logs
{
	public static void debug(String text)
	{
		if (Settings.DEBUG.bool())
			sendLog("&f[&aLimbo&f]&5|&f "+text);
	}
	
	public static void info(String text)
	{
		sendLog("&f[&aLimbo&f]|&f "+text);
	}
	
	public static void warning(String text)
	{
		sendLog("&f[&aLimbo&f]&c|&f "+text);
	}
	
	public static void severe(String text)
	{
		sendLog("&f[&aLimbo&f]&4|&f "+text);
	}
	
	public static void sendLog(String text)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	}
}
