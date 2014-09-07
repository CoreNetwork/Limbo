package us.corenetwork.limbo.io;

import java.util.ArrayList;
import java.util.List;
import us.corenetwork.limbo.Settings;

public class Prisoner {

	
	
	public String uuid;
	public long startTime;
	public long duration;
	public boolean spawnedOnce;
	public boolean toRelease;
	public String challenge;
	public long challengeStartTime;
	public boolean notify;
	
	public List<Integer> notificationTimes;
	
	public Prisoner(String uuid, long startTime, long duration, boolean toRelease, boolean spawnedOnce, String challenge, long challengeStartTime, boolean notify)
	{
		this.uuid = uuid;
		this.startTime = startTime;
		this.duration = duration;
		this.toRelease = toRelease;
		this.spawnedOnce = spawnedOnce;
		this.challenge = challenge;
		this.challengeStartTime = challengeStartTime;
		this.notify = notify;
		
		initializeNotifications();
	}
	
	private void initializeNotifications()
	{
		notificationTimes = Settings.NOTIFICATION_TIMES.intList();
	}
}
