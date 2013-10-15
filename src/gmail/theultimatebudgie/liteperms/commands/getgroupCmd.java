package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.Group;
import gmail.theultimatebudgie.liteperms.LitePerms;
import gmail.theultimatebudgie.liteperms.LitePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getgroupCmd implements LiteCommand {

	@Override
	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args) {
		if (args.length <= 0 ) {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp getgroup (PlayerName)");
			return;
		}
		
		String pName = args[0];
		Player target = Bukkit.getPlayerExact(pName);
		if (target == null) {
			Group g = plugin.getYaml().getPlayerGroup(pName);
			sender.sendMessage(LitePerms.INFO + pName + " is offline but their group is: " + g.getName());
			return;
		}
		
		LitePlayer litePlayer = LitePlayer.getPlayerByName(pName);
		Group g = litePlayer.getGroup();
		sender.sendMessage(LitePerms.INFO + pName + " is in the group: " + g.getName());
		
		return;
	}

}
