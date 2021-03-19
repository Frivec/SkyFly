package fr.frivec.core.tasks;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import fr.frivec.SkyFly;
import fr.frivec.core.entities.Golem;
import fr.frivec.core.entities.ShopVillager;
import fr.frivec.core.player.MaterialLevel;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.statement.States;
import fr.frivec.core.teams.Teams;
import fr.frivec.core.utils.SkyFlyUtils;
import fr.frivec.spigot.API;
import fr.frivec.spigot.effects.gloweffect.GlowEffect;
import fr.frivec.spigot.effects.gloweffect.GlowEffect.EffectColor;
import fr.frivec.spigot.scoreboard.server.ServerScoreboard;
import fr.frivec.spigot.title.ActionBar;

public class WaitTask extends BukkitRunnable {
	
	private int timer = 120;
	
	public static boolean running = false,
							forced = false;
	
	private static WaitTask instance;
	
	public WaitTask() {
		
		this.timer = 120;
		
		running = true;
		
		instance = this;
		
	}
	
	@Override
	public void run() {
		
		if(!running)
			
			this.cancel();
		
		if(this.timer == 0) {
			
			Bukkit.broadcastMessage("§aLancement de la partie !");
			spawnEntities();
			teamTeleport();
			updateSoreboard();
			setGlowColor();
			States.setCurrentState(States.GAME);
			new GameTask().runTaskTimer(SkyFly.getInstance(), 0, 20);
			this.cancel();
			
		}
		
		for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values())
			
			new ActionBar("§aLancement de la partie dans §6" + this.timer + " §asecondes.", 10, 10, 10).send(players.getBukkitPlayer());
			
		timer--;
		
	}
	
	private void spawnEntities() {
		
		for(Teams teams : Teams.values()) {
			
			if(!teams.equals(Teams.SPECTATOR)) {
				
				teams.setGolem(new Golem(teams.equals(Teams.RED) ? "§cGolem rouge" : "§bGolem bleu", teams));
				teams.setVillager(new ShopVillager(teams.equals(Teams.RED) ? new Location(Bukkit.getWorld("world"), 2.5, 160.0, 143.5, 50.0f, 0.0f) : 
																			new Location(Bukkit.getWorld("world"), -1.5, 160.0, -142.5, -150.0f, 0.0f)));
				
				teams.getGolem().spawn();
				teams.getVillager().spawn();
				
			}
			
		}
		
	}
	
	private void setGlowColor() {
		
		final GlowEffect effect = API.getInstance().getGlowEffect();
		
		//Get all players in the game
		for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values()) {
			
			final Player player = players.getBukkitPlayer(); //Get the bukkit player instance
			final Teams teams = Teams.getPlayerTeam(player); //Get the team of the player
			
			if(teams.equals(Teams.SPECTATOR)) //If the player is a spectator, we disable the glow effect
				
				continue;
			
			effect.setGlowColor(player, teams.equals(Teams.RED) ? EffectColor.RED : EffectColor.CYAN, true);
			
			final HashSet<Player> teamPlayers = Teams.getPlayerTeam(player).getPlayersObjects();
			
			teamPlayers.remove(player);
			
			effect.addViewer(player, teamPlayers);
			
			teamPlayers.add(player);
			
		}
		
	}
	
	private void updateSoreboard() {
		
		for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values()) {
			
			final Player player = players.getBukkitPlayer();
			final ServerScoreboard scoreboard = ServerScoreboard.getScoreboard(player);
			
			scoreboard.setPattern(SkyFly.getInstance().getGameBoard());
			scoreboard.refreshScoreboard();
			
		}
		
	}
	
	private void teamTeleport() {
		
		for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values()) {
			
			final Player player = players.getBukkitPlayer();
			final Teams team = Teams.getPlayerTeam(player);
			
			if(team == null)
				
				selectTeam(player);
			
			player.getInventory().clear();
			
		}
		
		balancingTeams();
		
		for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values()) {
			
			final Player player = players.getBukkitPlayer();
			final Teams team = Teams.getPlayerTeam(player);
			
			player.setMetadata("SPAWN_PROTECTION", new FixedMetadataValue(SkyFly.getInstance(), true));
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(SkyFly.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					player.removeMetadata("SPAWN_PROTECTION", SkyFly.getInstance());
					player.sendMessage("§cVous n'êtes plus protégé des dégats adversaires.");
					
				}
				
			}, 40L);
			
			Color color = null;
			
			if(team.equals(Teams.RED)) {
				
				player.teleport(SkyFlyUtils.red);
				color = Color.RED;
			
			}else {
				
				player.teleport(SkyFlyUtils.blue);
				color = Color.BLUE;
				
			}
			
			players.getItems().add(new MaterialLevel("§aCasque", Material.LEATHER_HELMET, true, color, 1).registerPrice(2, 7).registerPrice(3, 15));
			players.getItems().add(new MaterialLevel("§aJambières", Material.LEATHER_LEGGINGS, true, color, 10).registerPrice(2, 8).registerPrice(3, 16));
			players.getItems().add(new MaterialLevel("§aBottes", Material.LEATHER_BOOTS, true, color, 19).registerPrice(2, 6).registerPrice(3, 13));
			players.getItems().add(new MaterialLevel("§aÉpée", Material.WOODEN_SWORD, 28).registerPrice(2, 7).registerPrice(3, 18));
			
			player.sendMessage("§aTéléportation.");
			SkyFlyPlayer.getSFPlayer(player).getKit().giveKit(player);
			
		}
		
	}
	
	private void selectTeam(final Player player) {
		
		/*
		 * 
		 * Algorithme qui place les joueurs dans une équipe
		 * s'ils n'en ont pas choisi une directement.
		 * 
		 */
			
		final int redSize = Teams.RED.getPlayers().size(),
				blueSize = Teams.BLUE.getPlayers().size();
		
		if(redSize < blueSize)
				
			Teams.addPlayerInTeam(player, Teams.RED);
			
		else if(redSize > blueSize)
				
			Teams.addPlayerInTeam(player, Teams.BLUE);
			
		else {
				
			final Random random = new Random();
				
			if(random.nextInt() == 0) {
					
				Teams.addPlayerInTeam(player, Teams.RED);
					
				player.sendMessage("§aVous avez rejoint l'équipe §crouge§a.");
					
			}else {
					
				Teams.addPlayerInTeam(player, Teams.BLUE);
					
				player.sendMessage("§aVous avez rejoint l'équipe §bbleue§a.");
					
			}
				
		}
		
	}
	
	private void balancingTeams() {
		
		final int redSize = Teams.RED.getPlayers().size(),
					blueSize = Teams.BLUE.getPlayers().size();
		
		if(redSize >= blueSize + 2 || blueSize >= redSize + 2) {
			
			Bukkit.broadcastMessage("§cLes équipes sont déséquilibrées. Rééquilibrage en cours...");
			
			for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values()) {
				
				final Player player = players.getBukkitPlayer();
				
				Teams.removePlayer(player);
				selectTeam(player);
				
			}
		
		}
			
	}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	public int getTimer() {
		return timer;
	}
	
	public static WaitTask getInstance() {
		return instance;
	}

}
