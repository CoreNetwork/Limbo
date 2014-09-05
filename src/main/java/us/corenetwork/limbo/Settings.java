package us.corenetwork.limbo;

import java.util.ArrayList;
import java.util.List;
import us.corenetwork.limbo.io.IO;

public enum Settings {

	DEBUG("Debug", false),
	
	DEFAULT_DURATION("DefaultDuration", "60m"),
	
	COMMANDS_ON_ENTRY("Limbo.CommandsOnEntry", new ArrayList<String>(){{
		add("rspawn unprotect silent");
		add("mantle hydration restore'");
		add("clear <Player> silent");
		add("core effect <Player> clear silent");
		add("heal <Player>");
		add("runalias /limbogear <Player>");
		add("warp limbo-library silent");
	}}),
	COMMANDS_ON_RESPAWN("Limbo.CommandsOnRespawn", new ArrayList<String>(){{
		add("runalias /limbogear <Player>");
		add("warp limbo-library silent");
	}}),
	
	COMMANDS_ON_EXIT("Limbo.CommandsOnExit", new ArrayList<String>(){{
		add("clear <Player> silent");
		add("core effect <Player> clear silent");
		add("heal <Player>");
		add("sudo <Player> checkpoint clear");
		add("sudo <Player> warp end-respawn silent");
	}}),
	
	MESSAGE_CHECK_SELF("Messages.CheckSelf", "&bYou will stay in Limbo for <Time>"),
	MESSAGE_CHECK_PLAYER("Messages.CheckPlayer", "&6<Player> is in Limbo, <Time> left"),
	MESSAGE_CHECK_PLAYER_NEG("Messages.CheckPlayerNeg", "&6<Player> is not in Limbo"),
	
	MESSAGE_TIME_SING("Messages.TimeSingularSyntax", "<Minutes> minute"),
	MESSAGE_TIME_PLUR("Messages.TimePluralSyntax", "<Minutes> minutes"),
	MESSAGE_TIME_DETAILED("Messages.DetailedTimeSyntax", "<Minutes>m <Seconds>s"),
	
	MESSAGE_ENTRY("Messages.Entry", new ArrayList<String>(){{add("&bYour death erased your corporeal form and all possessions. &7You will linger in &fLimbo &7for &f<Timer>&7.");
	add("&3Reason: <Death>");}}),
	MESSAGE_EXIT("Messages.Exit", "&bYou have been released from Limbo."),
	MESSAGE_DEATH_RESPAWN("Messages.DeathRespawn", "&bYour incorporeal form cannot be damaged."),
	MESSAGE_NOTIFICATION("Messages.Notification", "&bRemaining time: <Time>"),
	
	MESSAGE_INCREASED_SELF("Messages.IncreasedSelf", "Your time here has been extended by <Time>"),
	MESSAGE_DECREASED_SELF("Messages.DecreasedSelf", "Your time here has been shortened by <Time>"),
	
	MESSAGE_INCREASED("Messages.Increased", "<Player>'s stay has been extended by <Time>"),
	MESSAGE_DECREASED("Messages.Decreased", "<Player>'s stay has been shortened by <Time>"),
	
	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_CONFIGURATION_RELOADED("Messages.ConfigurationReloaded", "Configuration reloaded successfully!");

	private String name;
	private Object def;
	
	private Settings(String Name, Object Def)
	{
		name = Name;
		def = Def;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Object getDefault()
	{
		return def;
	}
	
	public String string()
	{
		return (String) IO.config.get(name, def);
	}

	public Boolean bool()
	{
		return (Boolean) IO.config.get(name, def);
	}

	public List<String> stringList()
	{
		return IO.config.getStringList(name);
	}
}
