package us.corenetwork.limbo;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import us.corenetwork.limbo.io.Death;
import us.corenetwork.limbo.io.LimboIO;

public class DeathListener implements Listener {

	private final String MOD_GROUP = "Overseer";
	private List<Player> suiciders = new ArrayList<Player>();
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
	{
		if(event.getPlayer().getWorld().getEnvironment() == Environment.THE_END)
		{
			return;
		}
		Player player = event.getPlayer();
		String message = event.getMessage();
		String[] arr = message.split(" ");
		if(arr[0].equalsIgnoreCase("/kill"))
		{
			if(suiciders.contains(player) == false)
				suiciders.add(player);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		Environment env = player.getWorld().getEnvironment();
		String deathMessage = event.getDeathMessage();
		Logs.debug(deathMessage);
		
		if(Settings.CLEAR_ITEMS_ON_DEATH.bool())
		{
			event.getDrops().clear();
		}
		
		if(env != Environment.THE_END)
		{
			if(LimboManager.getPrisonerStatus(player) == PrisonerStatus.OUTSIDE && shouldGoToLimbo(player))
			{
				String strippedDeathMessage = ChatColor.stripColor(event.getDeathMessage());
				Logs.debug(player.getName() + " gone to limbo!");
				LimboIO.insertDeath(new Death(player.getUniqueId().toString(), Util.currentTime(), strippedDeathMessage));
				LimboManager.imprison(player, false);
				
				if((player.getName()+ " died").equals(strippedDeathMessage) || suiciders.contains(player))
				{
					strippedDeathMessage = Settings.MESSAGE_SUICIDE.string().replace("<Player>", player.getName());
					suiciders.remove(player);
				}
				
				deathMessage = ChatColor.translateAlternateColorCodes('&', Settings.DEATH_MESSAGE_COLOR.string() + strippedDeathMessage);
				
				Util.Broadcast(deathMessage);
				Logs.sendLog(deathMessage);
				event.setDeathMessage(null);
			}
		}
		else
		{
			event.setDeathMessage(null);
		}
	}
	
	private boolean shouldGoToLimbo(Player player)
	{
		return LimboPlugin.permission.playerInGroup(player, MOD_GROUP) == false;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		final Player player = event.getPlayer();
		
		switch (LimboManager.getPrisonerStatus(player))
		{
		case OUTSIDE:
			//Only for ppl who didnt get imprisoned on death?
			Logs.debug(player.getName() + " respawned as ALIVE");			
			break;
		case DURING:
			LimboPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(LimboPlugin.instance, new Runnable() {
				
				public void run()
				{
					LimboManager.respawnIn(player);
				}
			});
			
			Logs.debug(player.getName() + " respawned as LIMBO");
			break;
		case AFTER:
			LimboPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(LimboPlugin.instance, new Runnable() {
				public void run()
				{
					LimboManager.moveOut(player, false);
				}
			});
			Logs.debug(player.getName() + " respawned as RELEASED");
			break;
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoinEvent(PlayerJoinEvent  event)
	{
		final Player player = event.getPlayer();
		
		if (player.isDead() == false)
		{
			switch (LimboManager.getPrisonerStatus(player))
			{
			case OUTSIDE:			
				break;
			case DURING:
				LimboPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(LimboPlugin.instance, new Runnable() {
					public void run()
					{
						Util.Message(Settings.MESSAGE_NOTIFICATION.string().replace("<Time>", Util.getSimpleTimeMessage(LimboManager.getMilisLeft(player))), player);
					}
				});
				break;
			case AFTER:
				
					LimboManager.moveOut(player, false);
					Logs.debug(player.getName() + " respawned as RELEASED");
				break;
			}
		}
		
	}
}
