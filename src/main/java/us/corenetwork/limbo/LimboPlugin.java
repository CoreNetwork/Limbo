package us.corenetwork.limbo;

import java.util.HashMap;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import us.corenetwork.limbo.commands.AbstractLimboCommand;
import us.corenetwork.limbo.commands.CheckCommand;
import us.corenetwork.limbo.commands.DecreaseCommand;
import us.corenetwork.limbo.commands.EnterChallengeCommand;
import us.corenetwork.limbo.commands.ExitChallengeCommand;
import us.corenetwork.limbo.commands.FinishChallengeCommand;
import us.corenetwork.limbo.commands.ImprisonCommand;
import us.corenetwork.limbo.commands.IncreaseCommand;
import us.corenetwork.limbo.commands.LimboHelpCommand;
import us.corenetwork.limbo.commands.NotifyCommand;
import us.corenetwork.limbo.commands.ReleaseCommand;
import us.corenetwork.limbo.commands.ReloadCommand;
import us.corenetwork.limbo.commands.StatsCommand;
import us.corenetwork.limbo.commands.StatusCommand;
import us.corenetwork.limbo.commands.TopCommand;
import us.corenetwork.limbo.io.IO;
import us.corenetwork.limbo.io.Prisoners;

public class LimboPlugin extends JavaPlugin {
	public static LimboPlugin instance;

	public static Permission permission;

	public static HashMap<String, AbstractLimboCommand> limboCommands = new HashMap<String, AbstractLimboCommand>();
	
	@Override
	public void onEnable() 
	{
		instance = this;
		
		limboCommands.put("help", new LimboHelpCommand());
		limboCommands.put("reload", new ReloadCommand());
		limboCommands.put("check", new CheckCommand());
		limboCommands.put("status", new StatusCommand());
		limboCommands.put("release", new ReleaseCommand());
		limboCommands.put("increase", new IncreaseCommand());
		limboCommands.put("decrease", new DecreaseCommand());
		limboCommands.put("enter", new EnterChallengeCommand());
		limboCommands.put("exit", new ExitChallengeCommand());
		limboCommands.put("finish", new FinishChallengeCommand());
		limboCommands.put("imprison", new ImprisonCommand());
		limboCommands.put("stats", new StatsCommand());
		limboCommands.put("top", new TopCommand());
		limboCommands.put("notify", new NotifyCommand());
		
		
		
		IO.LoadSettings();
		IO.PrepareDB();
		Prisoners.readAll();
		
		if (!setupPermissions())
		{
			Logs.warning("Could not load Vault permissions - did you forget to install Vault?");
		}	
	
		getServer().getPluginManager().registerEvents(new DeathListener(), this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new TimeChecker(), 0, 100);
	}

	private boolean setupPermissions() 
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
	
	@Override
	public void onDisable() 
	{
		IO.freeConnection();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if (args.length < 1 || Util.isInteger(args[0]))
			return limboCommands.get("help").execute(sender, args, true);

		AbstractLimboCommand cmd = limboCommands.get(args[0]);
		if (cmd != null)
			return cmd.execute(sender, args, true);
		else
			return limboCommands.get("help").execute(sender, args, true);
	}
}
