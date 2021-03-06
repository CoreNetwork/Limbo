package us.corenetwork.limbo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.evilmidget38.NameFetcher;

public class Util {

    private static Map<String, UUID> playerNameToUUID = new HashMap<String, UUID>();
    private static Map<UUID, String> UUIDToPlayerName = new HashMap<UUID, String>();
	
	public static void Message(String message, CommandSender sender)
	{
		message = message.replaceAll("\\&([0-9abcdefklmnor])", ChatColor.COLOR_CHAR + "$1");

		final String newLine = "\\[NEWLINE\\]";
		String[] lines = message.split(newLine);

		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].trim();

			if (i == 0)
				continue;

			int lastColorChar = lines[i - 1].lastIndexOf(ChatColor.COLOR_CHAR);
			if (lastColorChar == -1 || lastColorChar >= lines[i - 1].length() - 1)
				continue;

			char lastColor = lines[i - 1].charAt(lastColorChar + 1);
			lines[i] = Character.toString(ChatColor.COLOR_CHAR).concat(Character.toString(lastColor)).concat(lines[i]);	
		}		

		for (int i = 0; i < lines.length; i++)
			sender.sendMessage(lines[i]);


	}
	
	public static void Broadcast(String message)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
				Util.Message(message, p);
		}

	}

	public static void Broadcast(String message, String exclusion)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (!p.getName().equals(exclusion))
				Util.Message(message, p);
		}

	}

	public static void Multicast(String message, List<Player> players)
	{
		for (Player p : players)
		{
			Util.Message(message, p);
		}
	}
	
	public static boolean hasPermission(CommandSender player, String permission)
	{
		while (true)
		{
			if (player.hasPermission(permission))
				return true;

			if (permission.length() < 2)
				return false;

			if (permission.endsWith("*"))
				permission = permission.substring(0, permission.length() - 2);

			int lastIndex = permission.lastIndexOf(".");
			if (lastIndex < 0)
				return false;

			permission = permission.substring(0, lastIndex).concat(".*");  
		}
	}
	
	public static Boolean isInteger(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static long currentTime()
	{
		return (new Date()).getTime();
	}
	
	//Parsing 50m or 1h or 10s to ticks
	//If letter is omitted, treating it like minutes
	public static long parseTimeToMilis(String time)
	{
		int timeInt;
		int multip;
		
		switch (time.charAt(time.length()-1))
		{
		case 'h':
			multip = 3600000;
			break;
		case 'm':
			multip = 60000;
			break;
		case 's':
			multip = 1000;
			break;
			
		default://default is m
			if(isInteger(time))
			{
				multip = 60000;
				timeInt = Integer.parseInt(time);
				return timeInt * multip;
			}
			else
			{
				throw new IllegalArgumentException("Wrong time syntax! " + time);
			}
		}
		
		if(isInteger(time.substring(0, time.length()-1)))
		{
			timeInt = Integer.parseInt(time.substring(0, time.length()-1));
			return timeInt * multip;
		}
		else
		{
			throw new IllegalArgumentException("Wrong time syntax! " + time);
		}
	}

	public static boolean isCorrectTimeFormat(String time)
	{
		if(time.length() == 0)
			return false;
		
		if(isInteger(time))
			return true;
		
		char c = time.charAt(time.length() - 1);
		String sTime = time.substring(0, time.length()-1);
		
		if(c == 's' || c == 'h' || c == 'm')
		{
			return isInteger(sTime);
		}
		else
		{
			return false;
		}
	}
	
	
	public static List<String> PrepareCommands(List<String> commands, HashMap<String, String> hashMap)
	{
		List<String> newCommands = new ArrayList<String>();
		for(String command : commands)
		{
			for(Entry<String, String> entry : hashMap.entrySet())
			{
				command = command.replace(entry.getKey(), entry.getValue());
			}
			newCommands.add(command);
		}
		return newCommands;
	}
	
	public static void RunCommands(List<String> commands)
	{
		for(String command : commands)
		{
			LimboPlugin.instance.getServer().dispatchCommand(LimboPlugin.instance.getServer().getConsoleSender(), command);
		}
	}

	public static String getDetailedTimeMessage(long miliseconds)
	{
		String timeMessage;
		long minutesLeft = miliseconds / 1000 / 60;
		long secondsLeft = (miliseconds / 1000) % 60;
		
		timeMessage = Settings.MESSAGE_TIME_DETAILED.string();
		timeMessage = timeMessage.replace("<Minutes>", minutesLeft+"");
		timeMessage = timeMessage.replace("<Seconds>", secondsLeft+"");
		return timeMessage;
	}

	public static String getSimpleTimeMessage(String timeSyntax)
	{
		long miliseconds = parseTimeToMilis(timeSyntax);
		return getSimpleTimeMessage(miliseconds);
	}
	
	public static String getSimpleTimeMessage(long miliseconds)
	{
		long minutesLeft = miliseconds / 1000 / 60;
		long secondsLeft = miliseconds / 1000 % 60;
		
		String message;
		
		if(secondsLeft >= 30)
			minutesLeft++;
		
		if(minutesLeft == 1)
			message = Settings.MESSAGE_TIME_SING.string().replace("<Minutes>", 1+"");
		else
			message = Settings.MESSAGE_TIME_PLUR.string().replace("<Minutes>", minutesLeft+"");
		
		return message;
	}
	
	public static String getPlayerNameFromUUID(final UUID uuid) {
        if (uuid == null) {
            return "<no player>";
        }
        String name = UUIDToPlayerName.get(uuid);
        if (name == null) {
            name = Bukkit.getOfflinePlayer(uuid).getName();
        }
        if (name == null) {
            NameFetcher fetcher = new NameFetcher(new ArrayList<UUID>(){{add(uuid);}});
            try {
                Map<UUID, String> names = fetcher.call();
                name = names.get(uuid);
                if (name != null) {
                    UUIDToPlayerName.put(uuid, name);
                    playerNameToUUID.put(name, uuid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return name != null ? name : "<" + uuid.toString() + ">";
    }

	public static UUID getUUIDFromString(String uuid)
	{
		if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
	}
}
