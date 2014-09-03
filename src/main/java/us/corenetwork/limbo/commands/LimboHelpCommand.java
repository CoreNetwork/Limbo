package us.corenetwork.limbo.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Util;

public class LimboHelpCommand extends BaseLimboCommand {

	public LimboHelpCommand()
	{
		permission = "help";
		desc = "List all possible commands";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		int page = 1;
		if (args.length > 0 && Util.isInteger(args[0])) page = Integer.parseInt(args[0]);
		List<String> descriptions = new ArrayList<String>();

		for (Entry<String, AbstractLimboCommand> e : LimboPlugin.limboCommands.entrySet())
		{
			descriptions.add("&a/" + "lim" + " " + e.getKey() + " &8-&f " + e.getValue().desc);
		}  		
		String[] descArray = descriptions.toArray(new String[0]);
		Arrays.sort(descArray);
		
		int maxpage = (int) Math.ceil((double) descArray.length / (sender instanceof Player ? 15.0 : 30.0));
		
		if (page > maxpage)
			page = maxpage;
		
		Util.Message("List of all commands:", sender);
		Util.Message("&8Page " + String.valueOf(page) + " of " + String.valueOf(maxpage), sender);

		for (int i = (page - 1) * 15; i < page * 15; i++)
		{
			if (descArray.length < i + 1 || i < 0) break;	
			Util.Message(descArray[i], sender);
		}   
	}

}
