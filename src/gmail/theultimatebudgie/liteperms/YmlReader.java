package gmail.theultimatebudgie.liteperms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class YmlReader {
	LitePerms plugin;
	FileConfiguration ranksYml;
	File ranksFile;
	FileConfiguration playersYml;
	File playersFile;
	
	public YmlReader (LitePerms plugin){
		this.plugin = plugin;
	}
	
	public void checkConfigs() throws IOException{
		if (!plugin.getDataFolder().isDirectory()){
			plugin.getDataFolder().mkdir();
		}
		
		//Checks ranks file
		ranksFile = new File(plugin.getDataFolder().getPath(), "ranks.yml");
		boolean newRanks = false;
		if (!ranksFile.isFile()){
			try {
				ranksFile.createNewFile();
				newRanks = true;
			} catch (IOException e) {
				plugin.getLogger().severe("Error creating ranks file... ABORT!");
				e.printStackTrace();
			}
		}
		
		ranksYml = YamlConfiguration.loadConfiguration(ranksFile);
		if (newRanks){
			ranksYml.addDefault("groups.guest.default", true);
			ranksYml.addDefault("groups.guest.permissions", Arrays.asList("default.permission"));
			
			ranksYml.addDefault("groups.admin.parent", "guest");
			ranksYml.addDefault("groups.admin.allperms", true);
			ranksYml.addDefault("groups.admin.chatprefix", "&3[Staff] &1");
			ranksYml.addDefault("groups.admin.permissions", Arrays.asList("-default.permission"));
			
			ranksYml.options().copyDefaults(true);

			ranksYml.save(ranksFile);
		}
		
		//Checks players file
		playersFile = new File(plugin.getDataFolder().getPath(), "players.yml");
		boolean newPlayers = false;
		if (!playersFile.isFile()){
			try {
				playersFile.createNewFile();
				newPlayers = true;
			} catch (IOException e) {
				plugin.getLogger().severe("Error creating players file... ABORT!");
				e.printStackTrace();
			}
		}
		
		playersYml = YamlConfiguration.loadConfiguration(playersFile);
		if (newPlayers){
			playersYml.addDefault("players.defaultPlayer.group", "guest");
			playersYml.addDefault("players.defaultPlayer.permissions", "guest.extrapermission");
			playersYml.options().copyDefaults(true);
			
			playersYml.save(playersFile);
		}
	}
	
	public void addPlayerExtraPermission(String name, String perm) {
		perm = perm.trim();
		List<String> extraPerms = getPlayerExtraPermissions(name);
		if (extraPerms.size() <= 0){
			playersYml.set("players." + name.toLowerCase() + ".permissions", Arrays.asList(perm));
		} else {
			extraPerms.add(perm);
			playersYml.set("players." + name.toLowerCase() + ".permissions", extraPerms);
		}
		savePlayers();
	}
	
	public void updateGroupPermissions(Group g) {
		ranksYml.set("groups." + g.getName() + ".permissions", g.getPermissions());
		saveRanks();
	}
	
	public List<String> getPlayerExtraPermissions(String name) {
		if (playersYml.contains("players." + name.toLowerCase() + ".permissions")){
			return playersYml.getStringList("players." + name.toLowerCase() + ".permissions");
		}
			
		return new ArrayList<String>();
	}
	
	public void setPlayerGroup(String name, Group g) {
		playersYml.set("players." + name.toLowerCase() + ".group", g.getName());
		savePlayers();
	}
	
	
	public Group getPlayerGroup(String name) {
		if (playersYml.contains("players." + name.toLowerCase() + ".group")){
			String groupName = playersYml.getString("players." + name.toLowerCase() + ".group");
			if (Group.getGroupByName(groupName) != null) {
				return Group.getGroupByName(groupName);
			}
		} else {
			playersYml.set("players." + name.toLowerCase() + ".group", Group.getDefaultGroup().getName());
			savePlayers();
		}
			
		return Group.getDefaultGroup();
	}
	
	public void setGroupChatPrefix(Group g, String prefix) {
		if (prefix == null) {
			ranksYml.set("groups." + g.getName() + ".chatprefix", new String(""));
		} else {
			ranksYml.set("groups." + g.getName() + ".chatprefix", new String(prefix));
		}
		saveRanks();
	}
	
	public void setPlayerChatPrefix(String name, String prefix) {
		if (prefix == null) {
			playersYml.set("players." + name.toLowerCase() + ".chatprefix", new String(""));
		} else {
			playersYml.set("players." + name.toLowerCase() + ".chatprefix", new String(prefix));
		}
		savePlayers();
	}
	
	public String getPlayerChatPrefix(String name) {
		if (playersYml.contains("players." + name.toLowerCase() + ".chatprefix")){
			return parseChatPrefix(playersYml.getString("players." + name.toLowerCase() + ".chatprefix"));
		}
			
		return null;
	}
	
	public void loadPermissions(){	

		Group.setGroups(populateGroups());
		
		Group.setDefaultGroup(findDefaultGroup());
		
		addChildrenParents();
		
		addExtraInheritance();
		
		addPermissions();
		
		checkAllPerms();
		
		getChatPrefixes();
	}
	
	public void saveRanks() {
		try {
			ranksYml.save(ranksFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void savePlayers() {
		try {
			playersYml.save(playersFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void checkAllPerms() {
		for (Group g : Group.getGroups() ) {
			ConfigurationSection groupSection = ranksYml.getConfigurationSection("groups." + g.getName());
			if (groupSection.contains("allperms")) {
				if (groupSection.getBoolean("allperms")){
					g.setAllPerms(true);
					continue;
				}
			}
			g.setAllPerms(false);
		}
	}
	
	private void addPermissions() {
		for (Group g : Group.getGroups() ) {
			ConfigurationSection groupSection = ranksYml.getConfigurationSection("groups." + g.getName());
			if (groupSection.contains("permissions")) {
				g.setPermissions(groupSection.getStringList("permissions"));
			}
		}
	}
	
	private void addExtraInheritance() {
		for (Group g : Group.getGroups() ) {
			ConfigurationSection groupSection = ranksYml.getConfigurationSection("groups." + g.getName());
			List<Group> groupInherits = new ArrayList<>();
			if (groupSection.contains("inheritance")) {
				List<String> inherits = groupSection.getStringList("inheritance");
				for (String group : inherits) {
					groupInherits.add(Group.getGroupByName(group));
				}
			}
			g.setInherits(groupInherits);
		}
	}
	
	private void addChildrenParents() {
		for (Group g : Group.getGroups() ) {
			ConfigurationSection groupSection = ranksYml.getConfigurationSection("groups." + g.getName());
			if (groupSection.contains("parent")) {
				Group parent = Group.getGroupByName(groupSection.getString("parent"));
				g.setParent(parent);
				parent.setChild(g);
			}
		}
		
		
	}
	
	private ArrayList<Group> populateGroups() {
		Set<String> groupNames = ranksYml.getConfigurationSection("groups").getKeys(false);
		ArrayList<Group> groups = new ArrayList<>();
		
		for (String name : groupNames) {
			groups.add(new Group(name));
		}
		
		return groups;
	}
	
	private void getChatPrefixes() {
		for (Group g : Group.getGroups() ) {
			ConfigurationSection groupSection = ranksYml.getConfigurationSection("groups." + g.getName());
			if (groupSection.contains("chatprefix")) {
				String raw = groupSection.getString("chatprefix");
				if (raw == null) {
					continue;
				}
				raw = parseChatPrefix(raw);
				g.setChatPrefix(raw);
			}
		}
	}
	
	private Group findDefaultGroup() {
		Set<String> groupNames = ranksYml.getConfigurationSection("groups").getKeys(false);
		
		for (String name : groupNames) {
			ConfigurationSection groupSection = ranksYml.getConfigurationSection("groups." + name);
			if(groupSection.contains("default")){
				if (groupSection.getBoolean("default")){
					return Group.getGroupByName(name);
				}
			}
		}
		
		return null;
	}
	
	private String parseChatPrefix(String prefix) {
		if (prefix == null) {
			return null;
		}
		if (prefix.startsWith("\"")) {
			prefix = prefix.substring(1);
		}
		if (prefix.endsWith("\"")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}
		return prefix;
		
	}
}
