package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.Group;
import gmail.theultimatebudgie.liteperms.LitePerms;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getprefixCmd implements LiteCommand{

	@Override
	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args) {
		if (args.length <= 1 ) {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp getprefix [Group/Player] (GroupName/PlayerName)");
			return;
		}
		
		if (args[0].equalsIgnoreCase("group")){
			Group group = null;
			if (Group.getGroupByName(args[1]) != null){
				group = Group.getGroupByName(args[1]);			
				sender.sendMessage(LitePerms.INFO + "The group: " + "'" + args[1] + "' has the prefix: \""  + group.getChatPrefix() + "\"");
			} else {
				sender.sendMessage(LitePerms.WARNING + "No group with the name: " + "'" + args[1] + "' exists.");
				return;
			}
			
			
		} else if (args[0].equalsIgnoreCase("player")) {
			
			String pName = args[1];
			String prefix = plugin.getYaml().getPlayerChatPrefix(pName);
			Player target = Bukkit.getPlayerExact(pName);
			if (prefix != null) {
				if (target == null) {
					sender.sendMessage(LitePerms.INFO + pName + " is offline but has the prefix: \"" +  prefix + "\"");
					return;
				} else {
					sender.sendMessage(LitePerms.INFO + pName + " has the prefix: \"" +  prefix + "\"");
					return;
				}
			} else {
				if (target == null) {
					sender.sendMessage(LitePerms.INFO + pName + " is offline but has No prefix.");
					return;
				} else {
					sender.sendMessage(LitePerms.INFO + pName + " has No prefix.");
					return;
				}
			}	
			
		} else {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp getprefix [Group/Player] (GroupName/PlayerName)");
			return;
		}
		
	}

}
