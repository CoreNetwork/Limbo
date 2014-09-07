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
		if(args.length != 1)
		{
			Util.Message("Usage : /lim release <player>", sender);
			return;
		}
		
		Player player = LimboPlugin.instance.getServer().getPlayer(args[0]);
		if(player == null)
		{
			Util.Message("Could not find player called " + args[0], sender);
			return;
		}

		switch (LimboManager.getPrisonerStatus(player))
		{
		case DURING:
			LimboManager.release(player);
			if(player.isOnline() && player.isDead() == false)
			{
				LimboManager.moveOut(player);
			}
			Util.Message(player.getName() + " has been released.", sender);
			break;
		case AFTER:
			Util.Message(player.getName() + " is already released.", sender);
			break;
		case OUTSIDE:
			Util.Message(player.getName() + " is not in Limbo.", sender);
			break;
		}
		
		
		
	}
}
