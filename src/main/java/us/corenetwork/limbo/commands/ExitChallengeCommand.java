package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.ChallengeManager;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.PrisonerStatus;
import us.corenetwork.limbo.Util;
import us.corenetwork.limbo.io.Prisoners;

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
		if(args.length != 1)
		{
			Util.Message("Usage: /lim exit <player>", sender);
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

		String challengeOfAPlayer = ChallengeManager.getChallange(player);

		if(!(challengeOfAPlayer == null || challengeOfAPlayer.equals("")))
		{
			ChallengeManager.exitChallenge(player, challengeOfAPlayer);
		}
	}
}
