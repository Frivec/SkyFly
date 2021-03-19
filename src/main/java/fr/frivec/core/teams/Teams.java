package fr.frivec.core.teams;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.frivec.core.entities.Golem;
import fr.frivec.core.entities.ShopVillager;
import fr.frivec.spigot.API;
import fr.frivec.spigot.effects.gloweffect.GlowEffect.EffectColor;

public enum Teams {
	
	RED("§c[Rouge]", ChatColor.RED, "", "1A", new HashSet<>(), 0),
	BLUE("§b[Bleu]", ChatColor.DARK_BLUE, "", "2B", new HashSet<>(), 0),
	SPECTATOR("§7[Spectateur]", ChatColor.GRAY, "§7S", "9C", new HashSet<>(), 0);
	
	private String prefix,
					tagPrefix,
					tagName;
	private ChatColor nameColor;
	private HashSet<String> players;
	private int points;
	private Golem golem;
	private ShopVillager villager;
	
	private Teams(final String prefix, final ChatColor nameColor, final String tagPrefix, final String tagName, final HashSet<String> players, final int points) {
		
		this.prefix = prefix;
		this.tagPrefix = tagPrefix;
		this.tagName = tagName;
		this.players = players;
		this.points = points;
		
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getTagPrefix() {
		return tagPrefix;
	}

	public void setTagPrefix(String tagPrefix) {
		this.tagPrefix = tagPrefix;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public HashSet<String> getPlayers(){
		return this.players;
	}

	public HashSet<Player> getPlayersObjects() {
		
		final HashSet<Player> players  = new HashSet<Player>();
		
		for(String name : this.players)
			
			players.add(Bukkit.getPlayer(name));
		
		return players;
	}

	public void setPlayers(HashSet<String> players) {
		this.players = players;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public ChatColor getNameColor() {
		return nameColor;
	}
	
	public void setNameColor(ChatColor nameColor) {
		this.nameColor = nameColor;
	}
	
	public Golem getGolem() {
		return golem;
	}
	
	public void setGolem(Golem golem) {
		this.golem = golem;
	}
	
	public ShopVillager getVillager() {
		return villager;
	}
	
	public void setVillager(ShopVillager villager) {
		this.villager = villager;
	}

	public static void increasePoints(final Teams team) {
		
		team.setPoints(team.getPoints() + 1);
		
	}
	
	public static void removePlayer(final Player player) {
		
		if(getPlayerTeam(player) != null)
			
			getPlayerTeam(player).getPlayers().remove(player.getName());
		
		return;
		
	}
	
	
	public static void addPlayerInTeam(final Player player, final Teams teams) {
		
		if(getPlayerTeam(player) != null && !getPlayerTeam(player).equals(teams)) {
			
			removePlayer(player);
			teams.getPlayers().add(player.getName());		
			Teams.addTeamTag(player, teams);
			return;
			
		}else if(getPlayerTeam(player) == null) {
			
			teams.getPlayers().add(player.getName());
			Teams.addTeamTag(player, teams);
			
			return;
			
		}else
			
			return;
		
	}
	
	public static boolean isPlayerInTeam(final Player player, final Teams team) {
		
		if(getPlayerTeam(player).equals(team))
			
			return true;
		
		return false;
		
	}
	
	public static Teams getPlayerTeam(final Player player) {
		
		for(Teams teams : Teams.values())
			
			if(teams.getPlayers().contains(player.getName()))
				
				return teams;
		
		return null;
		
	}
	
	private static void addTeamTag(final Player player, final Teams team) {
		
		for(Player players : Bukkit.getOnlinePlayers())
			
			API.getInstance().getGlowEffect().getTeams().get(team.equals(RED) ? EffectColor.RED : team.equals(BLUE) ? EffectColor.CYAN : EffectColor.LIGHT_GRAY).createPacket(player, false, false, true, false, players);
		
	}
	
}
