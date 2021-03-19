package fr.frivec.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.frivec.SkyFly;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.teams.Teams;
import fr.frivec.core.utils.SkyFlyUtils;
import fr.frivec.spigot.packets.PacketUtils;
import fr.frivec.spigot.scoreboard.ScoreboardPattern;
import fr.frivec.spigot.scoreboard.server.ServerScoreboard;
import fr.frivec.spigot.title.Title;
import net.minecraft.server.v1_16_R2.PacketPlayInClientCommand;
import net.minecraft.server.v1_16_R2.PacketPlayInClientCommand.EnumClientCommand;

public class PlayerDeathListener implements Listener {
	
	@EventHandler
	public void onDeath(final PlayerDeathEvent event) {
		
		final Player player = event.getEntity(),
					killer = player.getKiller();
		
		final int levels = player.getLevel();
		final float exp = player.getExp();
		
		event.setDeathMessage(null);
		
		player.getInventory().clear();
		event.getDrops().clear();
		
		//Get the instances of SkyFlyPlayer for the victim
		final SkyFlyPlayer victim = SkyFlyPlayer.getSFPlayer(player);
		
		//Update the stats of the victim
		victim.setDeaths(victim.getDeaths() + 1);
		
		//The killer is a player
		if(killer != null) {
			
			//Get infos of the killer. The scoreboard and the stats
			final SkyFlyPlayer damager = SkyFlyPlayer.getSFPlayer(killer);
			
			final ServerScoreboard scoreboard = ServerScoreboard.getScoreboard(killer);
			final ScoreboardPattern pattern = scoreboard.getPattern();
			
			//Update stats of the killer
			damager.setKills(damager.getKills() + 1);
			
			//Update the "kills" line in the scoreboard
			pattern.setLine(5, "  -§aKills: §r" + SkyFlyPlayer.getSFPlayer(killer).getKills());
			
			//Apply the pattern
			scoreboard.setPattern(pattern);
			scoreboard.refreshScoreboard();
			
			//Send a message to killer and the victim
			killer.sendMessage("§aVous avez tué: §6" + player.getName());
			killer.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1.0f, 1.0f);
			killer.giveExp(20);
			
			player.sendMessage("§cVous avez été tué(e) par: §6" + killer.getName());
			
			//If the player had the key
			if(victim.hasKey()) {
			
				//Add key in the killer's inventory
				killer.getInventory().addItem(SkyFlyUtils.feather);
				
				//Broadcast to alert players
				Bukkit.broadcastMessage("§6Attention ! " + (Teams.getPlayerTeam(killer).equals(Teams.RED) ? "§c" : "§b") + killer.getName() + " §6a récupéré la clef !");
			
				//Desactivate the key tag
				victim.setKey(false);
				
				//activate the key tag on the killer player
				damager.setKey(true);
				
			}
			
		}else {
			
			player.sendMessage("§cVous êtes mort");
			
			if(victim.hasKey()) {
				
				//Disabled the key tag on the player
				victim.setKey(false);
				
				//Spawn a new key
				SkyFlyUtils.spawnKey();
				
				//Send a message to all players and play a sound
				Bukkit.broadcastMessage("§6Attention ! Une nouvelle clef est apparue au centre de la carte !");
				
				for(Player players : Bukkit.getOnlinePlayers())
					
					players.playSound(players.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				
			}
			
		}
		
		//Skip the death menu
		autoRespawnPacket(player);
		
		player.setGameMode(GameMode.SPECTATOR);
		
		new BukkitRunnable() {
			
			int timer = 5;
			
			@Override
			public void run() {
				
				if(timer == 0) {
					
					player.teleport(Teams.getPlayerTeam(player).equals(Teams.RED) ? SkyFlyUtils.red : SkyFlyUtils.blue);
					player.sendMessage("§aRéapparition !");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
					player.setGameMode(GameMode.ADVENTURE);
					victim.getKit().giveKit(player);
					player.setExp(exp);
					player.setLevel(levels);
					
					player.setMetadata("SPAWN_PROTECTION", new FixedMetadataValue(SkyFly.getInstance(), true));
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(SkyFly.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							
							player.removeMetadata("SPAWN_PROTECTION", SkyFly.getInstance());
							player.sendMessage("§cVous n'êtes plus protégé des dégats adversaires.");
							
						}
						
					}, 40L);
					
					cancel();
					
				}
				
				new Title("§cVous êtes mort(e)", "§bRéapparition dans §6" + timer + " §bsecondes !", 0, 10, 0).send(player);
				
				timer--;
				
			}
			
		}.runTaskTimer(SkyFly.getInstance(), 0, 20);
		
	}
	
	private void autoRespawnPacket(final Player player) {
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(SkyFly.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				final PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
				
				PacketUtils.getConnection(player).a(packet);
				player.teleport(SkyFlyUtils.center.clone().add(new Vector(0, 1, 0)));
				
			}
		}, 5L);
		
	}

}
