package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.ChallengeManager;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.PrisonerStatus;
import us.corenetwork.limbo.Util;

public class ExitChallengeCommand extends BaseLimboCommand{
	
	public ExitChallengeCommand()
	{
		permission = "exit";
		desc = "Notes the fact that player exited a challenge, stops tracking time.";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		if(args.length != 2)
		{
			Util.Message("Usage: /lim exit <player> <challenge>", sender);
			return;
		}
	
		Player player = null;
		
		player = LimboPlugin.instance.getServer().getPlayer(args[0]);
		if(player == null)
		{
			Util.Message("Could not find player called " + args[0], sender);
			return;
		}
		
		if(LimboManager.getPrisonerStatus(player) != PrisonerStatus.DURING)
		{
			Util.Message("You cannot leave the challenge outside of Limbo", sender);
			return;
		}
		
		
		String challenge = args[1].toLowerCase();
		
		if(ChallengeManager.challengeExists(challenge))
		{
			if(ChallengeManager.isPlayerTakingPartIn(player, challenge)) 
			{
				ChallengeManager.exitChallenge(player, challenge);
			}
			else
			{
				Util.Message("You cannot leave a challenge you don't take part in!", sender);
				return;
			}
		}
		else
		{
			Util.Message("Specified challenge doesn't exist in config!", sender);
			return;
		}
	}
}
