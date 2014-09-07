package us.corenetwork.limbo.commands;

import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.ChallengeManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Record;

public class TopCommand extends BaseLimboCommand {

	private int offset, perPage;
	int maxPointLength;
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		int page;
		
		if(args.length < 1 || args.length > 3)
		{
			Util.Message("Usage: /lim top <challenge> [<page>]  or  /lim top <challenge> <player> [<page>]", sender);
			return;
		}
		
		page = 1;
		perPage = Settings.TOP_PER_PAGE.integer();
		Player player = null;
		
		String challenge = args[0];
		
		if(ChallengeManager.challengeExists(challenge) == false)
		{
			Util.Message("Challenge not found!", sender);
			return;
		}
		
		if(args.length == 2)
		{
			if(Util.isInteger(args[1]))
			{
				page = Integer.parseInt(args[1]);
			}
			else
			{
				player = LimboPlugin.instance.getServer().getPlayer(args[1]);
				if(player == null)
				{
					Util.Message("Could not find player called " + args[1], sender);
					return;
				}
			}
		}
		else if(args.length == 3)
		{
			player = LimboPlugin.instance.getServer().getPlayer(args[1]);
			if(player == null)
			{
				Util.Message("Could not find player called " + args[1], sender);
				return;
			}
			
			if(Util.isInteger(args[2]))
			{
				page = Integer.parseInt(args[2]);
			}
			else
			{
				Util.Message("Usage: /lim top <challenge> [<page>]  or  /lim top <challenge> <player> [<page>]", sender);
				return;
			}
		}
		
		
		
		int countAll = LimboIO.getRecordsCountFor(challenge, player);
		
		int pagesAll = (int)Math.ceil((double)countAll/perPage);
		if (page > pagesAll)
			page = pagesAll;
		if (page < 1)
			page = 1;
		
		offset = (page-1)*perPage;
		
		List<Record> records = LimboIO.getRecords(challenge, player, offset, perPage);
		
		displayHeader(sender);
		int counter = 1;
		for(Record record : records)
		{
			String place = getPlaceString(counter++);
			String time = getTimeString(record.duration);
			String name = getNameString(Util.getPlayerNameFromUUID(Util.getUUIDFromString(record.uuid)));
			
			String line = place + time + name;
			Util.Message(line, sender);
		}
		
		if (pagesAll > 1)
		{
			Util.Message(Settings.TOP_FOOTNOTE_COLOR.string() + "Page " + page + "/" + pagesAll, sender);
		}
	}

	private void displayHeader(CommandSender sender)
	{
		String header = Settings.TOP_PLACE_COLUMN_HEADER.string() + " " 
				+ Settings.TOP_POINTS_COLUMN_HEADER.string() + " "
				+ Settings.TOP_NAME_COLUMN_HEADER.string();
		Util.Message(header, sender);
	}
	
	private String getPlaceString(int counter)
	{
		int place = offset + counter;
		String color = Settings.TOP_PLACE_COLUMN_COLOR.string();
		String zeroColor = Settings.TOP_PLACE_COLUMN_LEADING_ZERO_COLOR.string();
		String placeString = padString(color+String.valueOf(place), String.valueOf(offset + perPage).length() + color.length(), zeroColor+ "0"); 
		placeString = Settings.TOP_PLACE_COLUMN_DISPLAY.string().replace("<Place>", placeString);
		return color + placeString + ". ";
	}
	
	private String getTimeString(long duration)
	{
		
		
		long minutesLeft = duration / 1000 / 60;
		long secondsLeft = (duration / 1000) % 60;
	
		String minutesString = minutesLeft+"";
		if(minutesString.length() > maxPointLength)
			maxPointLength = minutesString.length();
		
		String color = Settings.TOP_POINTS_COLUMN_COLOR.string();
		String zeroColor = Settings.TOP_POINTS_COLUMN_LEADING_ZERO_COLOR.string();
		String pointsString ="";
		if(minutesLeft == 0)
			minutesString = "";
		
		pointsString = padString(color + minutesString, maxPointLength + color.length(), zeroColor + "0");
		
		pointsString += "m ";
		pointsString += padString(color + secondsLeft, 2 + color.length(), zeroColor + "0");
		pointsString += "s";
		pointsString = Settings.TOP_POINTS_COLUMN_DISPLAY.string().replace("<Points>", pointsString);
		return color + pointsString + " ";
	}
	
	private String getNameString(String name)
	{
		String nameString = Settings.TOP_NAME_COLUMN_DISPLAY.string().replace("<Player>", name);
		return Settings.TOP_NAME_COLUMN_COLOR.string() + nameString + " ";
	}
	
	
	private String padString(String string, int length, String padding)
	{
		int currentLength = string.length();
		for(int i = 0;i<length-currentLength;i++)
			string = padding + string;
		return string;
	}
	
}
