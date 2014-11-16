package us.corenetwork.limbo;

import java.util.ArrayList;
import java.util.List;
import us.corenetwork.limbo.io.IO;

public enum Settings {

	DEBUG("Debug", false),
	
	DEFAULT_DURATION("DefaultDuration", "60m"),
	NOTIFICATION_TIMES_LEFT("NotificationTimesLeft", new Integer[]{50, 40, 30, 20, 10, 5, 4, 3, 2, 1}),
	
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
	
	CHALLENGES("Challenges", new ArrayList<String>()),
	
	TOP_COMPACT("TopCompact", true),
	TOP_PER_PAGE("TopPerPage", 10),
	TOP_FOOTNOTE_COLOR("Top.FootnoteColor", "&7"),
    TOP_PLACE_COLUMN_HEADER("Top.Place.Header", "&6Place"),
    TOP_PLACE_COLUMN_DISPLAY("Top.Place.Display", "<Place>"),
    TOP_PLACE_COLUMN_COLOR("Top.Place.Color", "&6"),
    TOP_PLACE_COLUMN_LEADING_ZERO_COLOR("Top.Place.LeadingZeroColor", "&8"),
    TOP_POINTS_COLUMN_HEADER("Top.Points.Header", "&ePoints"),
    TOP_POINTS_COLUMN_DISPLAY("Top.Points.Display", "<Points>"),
    TOP_POINTS_COLUMN_COLOR("Top.Points.Color", "&e"),
    TOP_POINTS_COLUMN_LEADING_ZERO_COLOR("Top.Points.LeadingZeroColor", "&8"),
    TOP_NAME_COLUMN_HEADER("Top.Name.Header", "&rName"),
    TOP_NAME_COLUMN_DISPLAY("Top.Name.Display", "<Player>"),
    TOP_NAME_COLUMN_COLOR("Top.Name.Color", "&r"),
    
	MESSAGE_CHECK_SELF("Messages.CheckSelf", "&bYou will stay in Limbo for <Time>"),
	MESSAGE_CHECK_PLAYER("Messages.CheckPlayer", "&6<Player> is in Limbo, <Time> left"),
	MESSAGE_CHECK_PLAYER_NEG("Messages.CheckPlayerNeg", "&6<Player> is not in Limbo"),
	
	MESSAGE_TIME_SING("Messages.TimeSingularSyntax", "<Minutes> minute"),
	MESSAGE_TIME_PLUR("Messages.TimePluralSyntax", "<Minutes> minutes"),
	MESSAGE_TIME_DETAILED("Messages.DetailedTimeSyntax", "<Minutes>m <Seconds>s"),
	
	MESSAGE_ENTRY("Messages.Entry", new ArrayList<String>(){{add("&bYour death erased your corporeal form and all possessions. &7You will linger in &fLimbo &7for &f<Time>&7.");
	}}),
	MESSAGE_EXIT("Messages.Exit", "&bYou have been released from Limbo."),
	MESSAGE_DEATH_RESPAWN("Messages.DeathRespawn", "&bYour incorporeal form cannot be damaged."),
	MESSAGE_DEATH_REASON("Messages.DeathReason", "&bYour death: <Death>."),
	MESSAGE_NOTIFICATION("Messages.Notification", "&bRemaining time: <Time>"),
	MESSAGE_NOTIFICATION_ON("Messages.NotificationOn", "Notifications ON!"),
	MESSAGE_NOTIFICATION_OFF("Messages.NotificationOff", "Notifications OFF!"),
	MESSAGE_NOTIFICATION_CANT_USE("Messages.NotificationCantUse", "You can only toggle notifications while in Limbo."),
	
	MESSAGE_INCREASED_SELF("Messages.IncreasedSelf", "Your time here has been extended by <Time>"),
	MESSAGE_DECREASED_SELF("Messages.DecreasedSelf", "Your time here has been shortened by <Time>"),
	
	MESSAGE_INCREASED("Messages.Increased", "<Player>'s stay has been extended by <Time>"),
	MESSAGE_DECREASED("Messages.Decreased", "<Player>'s stay has been shortened by <Time>"),
	
	MESSAGE_STATS("Messages.Stats", "Total : <Total>   Active : <Active>."),
	
	MESSAGE_NO_TOP("Messages.NoTop", "&cNobody finished this challenge yet!"),
	MESSAGE_PLAYER_NO_TOP("Messages.PlayerNoTop", "&c<Player> did not finish this challenge yet!"),
	
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

	public Integer integer()
	{
		return (Integer) IO.config.get(name, def);
	}
	
	public Boolean bool()
	{
		return (Boolean) IO.config.get(name, def);
	}

	public List<String> stringList()
	{
		return IO.config.getStringList(name);
	}
	
	public List<Integer> intList()
	{
		return IO.config.getIntegerList(name);
	}
	
	public static String getCommandDescription(String cmd, String type, String def)
	{
		String path = "CommandDescriptions." + type + "." + cmd;
		
		Object descO = IO.config.get(path);
		if (descO == null)
		{
			IO.config.set(path, "&a/" + type + " " + cmd + " &8-&f " + def);
			IO.saveConfig();
			descO = IO.config.get(path);
		}
		
		return (String) descO;
		
	}
}
