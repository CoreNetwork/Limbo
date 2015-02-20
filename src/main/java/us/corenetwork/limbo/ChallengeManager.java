package us.corenetwork.limbo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.Death;
import us.corenetwork.limbo.io.IO;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;
import us.corenetwork.limbo.io.Record;

public class ChallengeManager {

	private static Map<String, ConfigurationSection> chMap;
	
	public static void reloadChMap()
	{
		ConfigurationSection chSection = IO.config.getConfigurationSection("Challenges");

		chMap = new HashMap<String, ConfigurationSection>();
		if(chSection == null)
		{
			Logs.warning("No challenges defined in config!");
			return;
		}
		for(String key : chSection.getKeys(false))
		{
			chMap.put(key.toLowerCase(), chSection.getConfigurationSection(key));
		}
	}
	
	public static void enterChallenge(final Player player, String challenge)
	{
		updateChallenge(player, challenge, Util.currentTime());
		Util.RunCommands(Util.PrepareCommands(ChallengeManager.getEntryCommands(challenge), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
	}
	
	public static void exitChallenge(final Player player, String challenge)
	{
		updateChallenge(player, "", 0);
		Util.RunCommands(Util.PrepareCommands(ChallengeManager.getExitCommands(challenge), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
	}
	
	private static void updateChallenge(Player player, String challenge, long time)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		prisoner.challenge = challenge;
		prisoner.challengeStartTime = time;
		Prisoners.save(prisoner);
	}
	
	public static void finishChallenge(Player player, String challenge)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		long duration = Util.currentTime() - prisoner.challengeStartTime;
		Record bestRecord = LimboIO.getBestRecord(challenge);
		Record bestRecordSelf = LimboIO.getBestRecord(challenge, player);
		
		LimboIO.insertRecord(new Record(player.getUniqueId().toString(), challenge, duration));
		updateChallenge(player, "", 0);

		//Add experience based on time spent
		rewardExperience(player, prisoner, challenge, duration);
		
		if(bestRecord != null && bestRecord.duration > duration)
		{
			OfflinePlayer prevBestPlayer = LimboPlugin.instance.getServer().getOfflinePlayer(UUID.fromString(bestRecord.uuid));
			
			List<String> msgList = chMap.get(challenge).getStringList("VictoryRecord");
			for(String msg : msgList)
			{
				Util.Broadcast(msg.replace("<Player>", player.getName()).replace("<PreviousPlayer>", prevBestPlayer.getName()).replace("<DetailedTime>",  
						Util.getDetailedTimeMessage(duration)).replace("<PreviousDetailedTime>",  Util.getDetailedTimeMessage(bestRecord.duration)));
			}
		}
		else if(bestRecordSelf != null && bestRecordSelf.duration > duration)
		{
			List<String> msgList = chMap.get(challenge).getStringList("VictoryPersonalBest");
			for(String msg : msgList)
			{
				Util.Broadcast(msg.replace("<Player>", player.getName()).replace("<DetailedTime>",  
						Util.getDetailedTimeMessage(duration)).replace("<PreviousDetailedTime>",  Util.getDetailedTimeMessage(bestRecordSelf.duration)));
			}
		}
		else
		{
			Util.Broadcast(chMap.get(challenge).getString("Victory").replace("<Player>", player.getName()).replace("<DetailedTime>",  Util.getDetailedTimeMessage(duration)));
		}
	}

	private static void rewardExperience(final Player player, Prisoner prisoner, String challenge, long durationInMilis)
	{
		double timeItTook = durationInMilis/1000.0/60;
		List<?> expBackList = chMap.get(challenge).getList("ExperienceRewardedBack", new ArrayList());

		double mult = 0;

		for(Object o : expBackList )
		{
			Map<String, Double> map = (HashMap) o;
			Logs.debug(map.get("TimeUnderMinutes") + " " + timeItTook);
			if(map.get("TimeUnderMinutes") > timeItTook)
			{
				mult = map.get("ExpBack");
				break;
			}
		}

		player.setExp(0);
		player.setLevel(0);
		player.giveExp((int)(prisoner.expOnDeath * (float)mult));
	}

	public static boolean isPlayerTakingPartIn(Player player, String challenge)
	{
		String dbCh = Prisoners.getPrisoner(player).challenge;
		if(dbCh == null)
			return false;
		else
			return dbCh.equals(challenge);
	}
	
	public static boolean isPlayerTakingPart(Player player)
	{
		String chDb = Prisoners.getPrisoner(player).challenge;
		if(chDb == null)
			return false;
		if(chDb.equals(""))
			return false;
		return true;
	}

	public static String getChallange(Player player)
	{
		return Prisoners.getPrisoner(player).challenge;
	}

	public static boolean challengeExists(String challenge)
	{
		return chMap.get(challenge) != null;
	}	
	
	public static List<String> getRespawnCommands(String challenge)
	{
		return chMap.get(challenge).getStringList("CommandsOnRespawn");
	}
	public static List<String> getEntryCommands(String challenge)
	{
		return chMap.get(challenge).getStringList("CommandsOnEntry");
	}
	public static List<String> getExitCommands(String challenge)
	{
		return chMap.get(challenge).getStringList("CommandsOnExit");
	}

}
