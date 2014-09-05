package us.corenetwork.limbo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logs
{
	public static void debug(String text)
	{
		if (Settings.DEBUG.bool())
			info(text);
	}

    public static void debugIngame(String text)
    {
        if (Settings.DEBUG.bool())
            Bukkit.broadcastMessage(text);
    }
	
	public static void info(String text)
	{
		Bukkit.getLogger().info(ChatColor.GOLD + "[Limbo] " + ChatColor.GRAY + text);
	}
	
	public static void warning(String text)
	{
		Bukkit.getLogger().warning(ChatColor.GOLD + "[Limbo] " + ChatColor.GRAY + text);
	}
	
	public static void severe(String text)
	{
		Bukkit.getLogger().severe(ChatColor.GOLD + "[Limbo] " + ChatColor.GRAY + text);
	}
}
