package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Prisoners;

public class StatsCommand extends BaseLimboCommand {

	public StatsCommand()
	{
		permission = "stats";
		desc = "Prints current prison statistics.";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		int prisonersTotal = Prisoners.getTotalCount();
		int activePrisoners = Prisoners.getActiveCount();
		
		Util.Message(Settings.MESSAGE_STATS.string().replace("<Total>", prisonersTotal +"").replace("<Active>", activePrisoners+""), sender);
		
	}

}
