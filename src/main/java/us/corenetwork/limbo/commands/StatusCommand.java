package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Util;

public class StatusCommand extends BaseLimboCommand {

	public StatusCommand()
	{
		permission = "status";
		desc = "Retuns the limbo status of specified player (not time, status)";
		needPlayer = false;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		if(args.length != 1)
		{
			Util.Message("Usage : /lim status <player>", sender);
			return;
		}
		
		Player player = LimboPlugin.instance.getServer().getPlayer(args[0]);
		if(player == null)
		{
			Util.Message("Could not find player called " + args[0], sender);
			return;
		}	

		Util.Message(LimboManager.getPrisonerStatus(player).toString(), sender);
		
	}
}
