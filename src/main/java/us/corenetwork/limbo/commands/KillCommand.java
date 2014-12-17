package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ginaf on 2014-12-17.
 */
public class KillCommand extends BaseLimboCommand {

    public KillCommand()
    {
        permission = "kill";
        desc = "Kill the player";
        needPlayer = true;
    }


    @Override
    public void run(CommandSender sender, String[] args)
    {
        Player player = (Player) sender;
        player.setHealth(0);
    }
}
