package us.corenetwork.limbo;

import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;

public class TimeChecker implements Runnable {

	public void run()
	{
		for(Prisoner prisoner : Prisoners.getAllPrisoners())
		{
			Player player = LimboPlugin.instance.getServer().getPlayer(Util.getUUIDFromString(prisoner.uuid));
			
			
			if(player != null && player.isOnline())
			{
				PrisonerStatus status = Prisoners.getStatus(prisoner);
				if(status == PrisonerStatus.DURING && prisoner.notify)
				{
					long minPassed = (Util.currentTime() - prisoner.startTime) / 1000 / 60;
					if(minPassed == prisoner.notificationTimes.get(0))
					{
						Util.Message(Settings.MESSAGE_NOTIFICATION.string().replace("<Time>", Util.getSimpleTimeMessage(LimboManager.getMilisLeft(player))), player);
						prisoner.notificationTimes.remove(0);
					}
				}
				
				if(status == PrisonerStatus.AFTER && player.isDead() == false)
				{
					LimboManager.moveOut(player);
				}
			}
		}
	}

}
