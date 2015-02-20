package us.corenetwork.limbo;

import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.Death;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;

public class LimboManager {

	
	public static void imprison(Player player, boolean skipFirstRespawn)
	{
		Prisoner prisoner = new Prisoner(player.getUniqueId().toString(), Util.currentTime(), Util.parseTimeToMilis(Settings.DEFAULT_DURATION.string()), false, skipFirstRespawn, null, 0, true, calculateTotalExp(player));
		Prisoners.add(prisoner);
	}
	
	public static void moveIn(final Player player)
	{
 		Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_ENTRY.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
		List<String> msgList = Settings.MESSAGE_ENTRY.stringList();
		Death death = LimboIO.getLastDeath(player);
		
		for(String msg : msgList)
		{
			Util.Message(msg.replace("<Time>", Util.getSimpleTimeMessage(LimboManager.getMilisLeft(player))).replace("<Death>", death.deathMessage), player);
		}
		
	}
	
	public static void release(Player player)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		prisoner.toRelease = true;
		Prisoners.save(prisoner);
	}
	
	public static void moveOut(final Player player, boolean silent)
	{
		//passing whatever, we only need an object with uuid
		Prisoners.remove(Prisoners.getPrisoner(player));
		runOutCommands(player, silent);
	}
	
	public static void runOutCommands(final Player player, boolean silent)
	{
		Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_EXIT.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
		if(silent == false)
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
			if(Util.currentTime() - prisoner.startTime > 1000*10)
			{
				Death death = LimboIO.getLastDeath(player);
				Util.Message(Settings.MESSAGE_DEATH_REASON.string().replace("<Death>", death.deathMessage), player);
			}
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

	//Mainly because I dont trust the getTotalExperience method
	private static float calculateTotalExp(Player player)
	{
		return player.getExp() + levelToTotalExp(player.getLevel());
	}

	private static float levelToTotalExp(int level)
	{
		if(level < 16)
		{
			return level*level + 6*level;
		}
		else if (level < 31)
		{
			return 2.5F*level*level  - 40.5F*level + 360;
		}
		else
		{
			return 4.5F*level*level  - 162.5F*level + 2220;
		}
	}

}
