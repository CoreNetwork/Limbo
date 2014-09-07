package us.corenetwork.limbo;

import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;

public class LimboManager {

	
	public static void imprison(Player player)
	{
		Prisoner prisoner = new Prisoner(player.getUniqueId().toString(), Util.currentTime(), Util.parseTimeToMilis(Settings.DEFAULT_DURATION.string()), false, false, null, 0);
		Prisoners.add(prisoner);
	}
	
	public static void moveIn(final Player player)
	{
		Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_ENTRY.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
		List<String> msgList = Settings.MESSAGE_ENTRY.stringList();
		for(String msg : msgList)
		{
			Util.Message(msg.replace("<Time>", Util.getSimpleTimeMessage(Settings.DEFAULT_DURATION.string())).replace("<Death>", "LOO death message"), player);
		}
		
	}
	
	public static void release(Player player)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		prisoner.toRelease = true;
		Prisoners.save(prisoner);
	}
	
	public static void moveOut(final Player player)
	{
		//passing whatever, we only need an object with uuid
		Prisoners.remove(Prisoners.getPrisoner(player));
		Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_EXIT.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
		Util.Message(Settings.MESSAGE_EXIT.string(), player);
	}
	

	public static void respawnIn(final Player player)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		if(prisoner.spawnedOnce == false)
		{
			moveIn(player);
			prisoner.spawnedOnce = true;
			Prisoners.save(prisoner);
		}
		else
		{
			if(ChallengeManager.isPlayerTakingPart(player))
			{
				Util.RunCommands(Util.PrepareCommands(ChallengeManager.getRespawnCommands(prisoner.challenge), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
			}
			else
			{
				Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_RESPAWN.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
			}
			Util.Message(Settings.MESSAGE_DEATH_RESPAWN.string(), player);
		}
	}
	
	
	public static void modifyDuration(Player player, long modification)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		prisoner.duration += modification;
		Prisoners.save(prisoner);
	}
	
	public static PrisonerStatus getPrisonerStatus(Player player)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		return Prisoners.getStatus(prisoner);
	}

	public static long getMilisLeft(Player player)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		if(prisoner == null)
		{
			return 0;
		}
		else
		{
			long ticksLeft = prisoner.startTime + prisoner.duration - Util.currentTime();
			if(ticksLeft < 0)
				ticksLeft = 0;
			return ticksLeft;
		}
	}

}