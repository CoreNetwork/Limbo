package us.corenetwork.limbo.io;

public class Prisoner {

	public String uuid;
	public long startTime;
	public long duration;
	public boolean spawnedOnce;
	public boolean toRelease;
	public String challenge;
	public long challengeStartTime;
	
	public Prisoner(String uuid, long startTime, long duration, boolean toRelease, boolean spawnedOnce, String challenge, long challengeStartTime)
	{
		this.uuid = uuid;
		this.startTime = startTime;
		this.duration = duration;
		this.toRelease = toRelease;
		this.spawnedOnce = spawnedOnce;
		this.challenge = challenge;
		this.challengeStartTime = challengeStartTime;
	}
}
