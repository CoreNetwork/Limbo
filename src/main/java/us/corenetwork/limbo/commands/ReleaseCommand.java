package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;

public class ReleaseCommand extends BaseLimboCommand {
	
	public ReleaseCommand()
	{
		permission = "release";
		desc = "Release specified player from Limbo";
		needPlayer = false;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		
		boolean silent = false;
		if(args.length != 1 || args.length != 2)
		{
			Util.Message("Usage : /lim release <player> [silent]", sender);
			return;
		}
		
		Player player = LimboPlugin.instance.getServer().getPlayer(args[0]);
		if(player == null)
		{
			Util.Message("Could not find player called " + args[0], sender);
			return;
		}

		if(args.length == 2)
		{
			if(args[1].toLowerCase().equals("silent"))
			{
				silent = true;
			}
			else
			{
				Util.Message("Usage: /lim release <player> [silent]", sender);
				return;
			}
		}
		
		String message = player.getName();
		switch (LimboManager.getPrisonerStatus(player))
		{
		case DURING:
			LimboManager.release(player);
			if(player.isOnline() && player.isDead() == false)
			{
				LimboManager.moveOut(player, false);
			}
			message += " has been released.";
			break;
		case AFTER:
			message += " is already released.";
			break;
		case OUTSIDE:
			message += " is not in Limbo.";
			break;
		}
		
		if(silent == false)
		{
			Util.Message(message, sender);
		}
	}
}
