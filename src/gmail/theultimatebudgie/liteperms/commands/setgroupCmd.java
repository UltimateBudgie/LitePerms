package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.Group;
import gmail.theultimatebudgie.liteperms.LitePerms;
import gmail.theultimatebudgie.liteperms.LitePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//TODO: Can't promote superior!
public class setgroupCmd implements LiteCommand {

	@Override
	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args) {
		if (args.length <= 1 ) {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp setgroup (PlayerName) (GroupName)");
			return;
		}
		
		Group newRank = null;
		if (Group.getGroupByName(args[1]) != null){
			newRank = Group.getGroupByName(args[1]);
		} else {
			sender.sendMessage(LitePerms.WARNING + "No group with the name: " + "'" + args[1] + "' exists.");
			return;
		}
		
		String pName = args[0];
		Player target = Bukkit.getPlayerExact(pName);
		if (target == null) {
			plugin.getYaml().setPlayerGroup(pName, newRank);
			sender.sendMessage(LitePerms.INFO + pName + " is offline but will be set to group: " + "'" + args[1] + "' on next login.");
			return;
		}
		
		LitePlayer litePlayer = LitePlayer.getPlayerByName(pName);
		litePlayer.setGroup(newRank);
		plugin.getYaml().setPlayerGroup(pName, newRank);
		litePlayer.updatePermissions();
		sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + pName + " has been set to group: "  + "'" + args[1] + "'");
		
		return;
	}

}
