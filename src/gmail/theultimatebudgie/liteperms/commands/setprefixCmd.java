package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.Group;
import gmail.theultimatebudgie.liteperms.LitePerms;
import gmail.theultimatebudgie.liteperms.LitePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setprefixCmd implements LiteCommand {

	@Override
	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args) {
		if (args.length <= 1 ) {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp setprefix [Player/Group] (PlayerName/GroupName) \"(*OPTIONAL*:Prefix)\"");
			return;
		}
		
		if (args[0].equalsIgnoreCase("group")){
			Group group = null;
			if (Group.getGroupByName(args[1]) != null){
				group = Group.getGroupByName(args[1]);
			} else {
				sender.sendMessage(LitePerms.WARNING + "No group with the name: " + "'" + args[1] + "' exists.");
				return;
			}
			
			if (args.length <= 2) {
				group.removeChatPrefix();
				plugin.getYaml().setGroupChatPrefix(group, null);
				sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + "The group: " + "'" + args[1] + "' has had their prefix removed. ");
			} else {
				StringBuilder builder = new StringBuilder();
				builder.append(args[2]);
				for (int i = 3; i < args.length; i++) {
					builder.append(" ");
					builder.append(args[i]);
				}
				String prefix = builder.toString();
				if (prefix.startsWith("\"") || prefix.startsWith("\'")) {
					prefix = prefix.substring(1);
				}
				if (prefix.endsWith("\"") || prefix.endsWith("\'")) {
					prefix = prefix.substring(0, prefix.length() - 1);
				}
				plugin.getYaml().setGroupChatPrefix(group, prefix);
				group.setChatPrefix(prefix);
				sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + "The group: " + "'" + args[1] + "' has been given the prefix: "  + prefix);
			}
			
		} else if (args[0].equalsIgnoreCase("player")) {
			
			String pName = args[1];
			
			boolean remove;
			String prefix = null;
			if (args.length <= 2) {
				remove = true;
				plugin.getYaml().setPlayerChatPrefix(pName, null);
			} else {
				remove = false;
				StringBuilder builder = new StringBuilder();
				builder.append(args[2]);
				for (int i = 3; i < args.length; i++) {
					builder.append(" ");
					builder.append(args[i]);
				}
				prefix = builder.toString();
				if (prefix.startsWith("\"")) {
					prefix = prefix.substring(1);
				}
				if (prefix.endsWith("\"")) {
					prefix = prefix.substring(0, prefix.length() - 1);
				}
				plugin.getYaml().setPlayerChatPrefix(pName, prefix);
			}
			
			
			Player target = Bukkit.getPlayerExact(pName);
			if (target == null) {
				if (remove) {
					sender.sendMessage(LitePerms.INFO + pName + " is offline but will have their prefix removed on next login.");
				} else {
					sender.sendMessage(LitePerms.INFO + pName + " is offline but will be get the prefix: " +  prefix + " on next login.");
				}
				return;
			} 
			LitePlayer litePlayer = LitePlayer.getPlayerByName(pName);
			litePlayer.setChatPrefix(prefix);
			if (remove) {
				sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + pName + " has had their prefix removed.");
			} else {
				sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + pName + " has been given the prefix: "  + prefix);
			}
			return;
			
		} else {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp addperm [Group/Player] (GroupName/PlayerName) (PermissionName)");
			return;
		}

	}
	
}
