package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.Group;
import gmail.theultimatebudgie.liteperms.LitePerms;
import gmail.theultimatebudgie.liteperms.LitePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addpermCmd implements LiteCommand{

	@Override
	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args) {
		if (args.length <= 2 ) {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp addperm [Group/Player] (GroupName/PlayerName) (PermissionName)");
			return;
		}
		
		if (args[0].equalsIgnoreCase("group")){
			Group group = null;
			if (Group.getGroupByName(args[1]) != null){
				group = Group.getGroupByName(args[1]);
				group.addPermission(args[2]);
				plugin.getYaml().updateGroupPermissions(group);
				
				//Update all online players
				for (LitePlayer p : LitePlayer.getAllPlayers() ) {
					p.updatePermissions();
				}
				
				sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + "The group: " + "'" + args[1] + "' has been given the permission: "  + args[2]);
			} else {
				sender.sendMessage(LitePerms.WARNING + "No group with the name: " + "'" + args[1] + "' exists.");
				return;
			}
			
			
		} else if (args[0].equalsIgnoreCase("player")) {
			
			String pName = args[1];
			Player target = Bukkit.getPlayerExact(pName);
			if (target == null) {
				plugin.getYaml().addPlayerExtraPermission(pName, args[2]);
				sender.sendMessage(LitePerms.INFO + pName + " is offline but will be get the permission " +  args[2] + " on next login.");
				return;
			}
			
			LitePlayer litePlayer = LitePlayer.getPlayerByName(pName);
			litePlayer.addExtraPermission(args[2]);
			plugin.getYaml().addPlayerExtraPermission(pName, args[2]);
			litePlayer.updatePermissions();
			sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + pName + " has been given the permission: "  + args[2]);
			
			
		} else {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp addperm [Group/Player] (GroupName/PlayerName) (PermissionName)");
			return;
		}
	}
	
}
