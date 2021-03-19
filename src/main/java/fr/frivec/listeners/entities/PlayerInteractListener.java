package fr.frivec.listeners.entities;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.frivec.SkyFly;
import fr.frivec.core.menus.ShopMenu;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.statement.States;
import fr.frivec.core.teams.Teams;
import fr.frivec.core.utils.SkyFlyUtils;
import fr.frivec.spigot.menus.MenuManager;
import fr.frivec.spigot.title.Title;

public class PlayerInteractListener implements Listener {
	
	@EventHandler
	public void onRightClickEntity(final PlayerInteractEntityEvent event) {
		
		final Player player = event.getPlayer();
		final SkyFlyPlayer skyFlyPlayer = SkyFlyPlayer.getSFPlayer(player);
		final Teams team = Teams.getPlayerTeam(player);
		final Entity entity = event.getRightClicked();
		
		if(!States.isState(States.GAME) || !event.getHand().equals(EquipmentSlot.HAND))
			
			return;
		
		final boolean spectator = team.equals(Teams.SPECTATOR);
		
		if(spectator)
			
			return;
		
		if(entity.getType().equals(EntityType.IRON_GOLEM)) {
			
			if(entity.getCustomName().equals(team.getGolem().getName())) {
			
				if(skyFlyPlayer.hasKey()) {
					
					final Title title = new Title("§6Point marqué !", 
							team.equals(Teams.RED) ? "§c" : "§b" + player.getName() + " §amarque un point !", 10, 20, 10);
					
					player.getInventory().remove(SkyFlyUtils.feather);
					player.updateInventory();
					
					skyFlyPlayer.setKey(false);
					skyFlyPlayer.setPoints(skyFlyPlayer.getPoints() + 1);
					
					for(Player players : Bukkit.getOnlinePlayers()) {
						
						title.send(players);
							
						if(team.equals(Teams.getPlayerTeam(players)))
							
							players.playSound(players.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
						
						else
								
							players.playSound(players.getLocation(), Sound.ENTITY_PANDA_DEATH, 1.0f, 1.0f);
						
					}
					
					for(Player teamPlayers : team.getPlayersObjects())
						
						teamPlayers.giveExp(30);
					
					Teams.increasePoints(team);
					
					if(team.getPoints() == 10) {
						
						States.setCurrentState(States.END);
						
						Bukkit.broadcastMessage("§6§m------------------------------");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§aL'équipe " + (team.equals(Teams.RED) ? "§cRouge" : "§bBleue") + " §aremporte la partie !");
						Bukkit.broadcastMessage("§bMerci d'avoir joué !");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§6§m------------------------------");
						
						new BukkitRunnable() {
							
							private int timer = 60;
							
							@Override
							public void run() {
								
								if(timer == 0) {
									
									for(Player players : Bukkit.getOnlinePlayers())
										
										players.kickPlayer(String.join("\n", Arrays.asList("§bMerci d'avoir joué", "", "§aN'hésitez pas à donner votre avis", "§asur le serveur Discord !")));
									
									cancel();
									
									Bukkit.getServer().spigot().restart();
									
								}
								
								timer--;
								
							}
							
						}.runTaskTimer(SkyFly.getInstance(), 0L, 20L);
						
						return;
						
					}else {
						
						new BukkitRunnable() {
							
							private int ticks = 1200;
							private final BossBar bossBar = Bukkit.createBossBar("§aProchaine clef dans: " + (int) Math.floor((this.ticks * 50 * 0.001)), BarColor.YELLOW, BarStyle.SOLID, new BarFlag[] {});
							
							@Override
							public void run() {
								
								final double progress = (double) this.ticks / (double) 1200;
								
								bossBar.setTitle("§aProchaine clef dans: §6" + (int) Math.floor(this.ticks * 50 * 0.001) + " §a" + (ticks <= 40 ? "seconde" : "secondes"));
								bossBar.setProgress(progress);
								
								for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values())
									
									if(players.getBukkitPlayer() != null)
										
										if(ticks != 20)
									
											bossBar.addPlayer(players.getBukkitPlayer());
								
										else
											
											bossBar.removePlayer(players.getBukkitPlayer());
								
								if(ticks == 20) {
									
									SkyFlyUtils.spawnKey();
									Bukkit.broadcastMessage("§6Attention ! Une nouvelle clef est apparue au centre de la carte !");
										
									cancel();
									
								}
								
								ticks--;
								
							}
							
						}.runTaskTimer(SkyFly.getInstance(), 1L, 1L);
						
						return;
						
					}
					
				}else {
					
					player.sendMessage("§cVous n'avez pas de clef. Repassez me voir plus tard.");
					return;
					
				}
				
			}else {
				
				player.sendMessage("§cPTDR T KI ?");
				player.setVelocity(new Vector(-2, 2, 0));
				return;
				
			}
			
		}else if(entity.getType().equals(EntityType.VILLAGER)) {
			
			event.setCancelled(true);
			
			if(entity.equals(Teams.getPlayerTeam(player).getVillager().getVillager()))
				
				MenuManager.getInstance().onOpenMenu(player, new ShopMenu());
				
			else
				
				player.sendMessage("§cT'es qui ? Retourne chez toi.");
			
			return;
			
		}
		
	}

}
