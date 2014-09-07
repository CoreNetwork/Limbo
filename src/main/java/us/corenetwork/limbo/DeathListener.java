package us.corenetwork.limbo;

import java.util.Date;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import us.corenetwork.limbo.io.LimboIO;

public class DeathListener implements Listener {

	private final String MOD_GROUP = "Overseer";
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		Environment env = player.getWorld().getEnvironment();
		
		if(env != Environment.THE_END)
		{
			if(LimboManager.getPrisonerStatus(player) == PrisonerStatus.OUTSIDE && shouldGoToLimbo(player))
			{
				Logs.debug(player.getName() + " gone to limbo!");
				LimboIO.saveDeath(player.getUniqueId().toString(), (new Date()).getTime(), event.getDeathMessage());
				LimboManager.imprison(player);
			}
		}
		else
		{
			//Died somewhere in limbo, note that, will decide what commands to run on respawn?
			//or decide based on what we have in prisoner record + status
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
					LimboManager.moveOut(player);
				}
			});
			Logs.debug(player.getName() + " respawned as RELEASED");
			break;
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoinEvent(PlayerJoinEvent  event)
	{
		Player player = event.getPlayer();
		
		if (player.isDead() == false)
		{
			switch (LimboManager.getPrisonerStatus(player))
			{
			case OUTSIDE:			
				break;
			case DURING:
				Util.Message(Settings.MESSAGE_NOTIFICATION.string().replace("<Time>", Util.getSimpleTimeMessage(LimboManager.getMilisLeft(player))), player);
				break;
			case AFTER:
				LimboManager.moveOut(player);
				Logs.debug(player.getName() + " respawned as RELEASED");
				break;
			}
		}
	}
}
