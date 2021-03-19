package fr.frivec.core.managers;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.frivec.SkyFly;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.statement.States;
import fr.frivec.core.tasks.WaitTask;
import fr.frivec.core.teams.Teams;
import fr.frivec.core.utils.SkyFlyUtils;
import fr.frivec.spigot.API;
import fr.frivec.spigot.effects.gloweffect.GlowEffect;
import fr.frivec.spigot.effects.gloweffect.GlowEffect.EffectColor;
import fr.frivec.spigot.item.ItemCreator;
import fr.frivec.spigot.managers.AbstractManager;
import fr.frivec.spigot.scoreboard.server.ServerScoreboard;
import fr.frivec.spigot.title.Title;

public class PlayerManager extends AbstractManager {
	
	private SkyFly main;
	private GlowEffect effect;
	
	public PlayerManager(final JavaPlugin javaPlugin) {
		
		super(javaPlugin);
		
		this.main = SkyFly.getInstance();
		this.effect = API.getInstance().getGlowEffect();
		
	}
	
	@Override
	public void onConnect(Player player) {
		
		/*Dev pvp 1.8*/
//		this.main.getPacketsManager().registerPlayer(player, new PacketsDev(player));
		
		if(States.isState(States.WAIT)) {
			
			int size = SkyFlyPlayer.getPlayers().size();
			
			if(size == 8) {
				
				player.kickPlayer(String.join("\n", Arrays.asList("§b§lSkyFly", "", "§cDésolé, la partie est pleine :c", "§aRéessayez plus tard !")));
				return;
				
			}
			
			SkyFlyPlayer.getSFPlayer(player);
			
			size++;
			
			//Teleport to the lobby and clear the inventory
			player.teleport(SkyFlyUtils.lobby);
			player.getInventory().clear();
			player.setGameMode(GameMode.ADVENTURE);
			player.setHealth(20.0);
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
			player.setLevel(0);
			player.setExp(0.0f);
			
			//Remove all potions effects
			for(PotionEffect effect : player.getActivePotionEffects())
				
				player.removePotionEffect(effect.getType());
			
			//Send the title
			new Title("§bSkyFly v3", null, 20, 10, 20).send(player);
			
			player.getInventory().setItem(0, new ItemCreator(Material.BUBBLE_CORAL_FAN, 1).setDisplayName("§3Choix des équipes").build());
			player.getInventory().setItem(8, new ItemCreator(Material.RED_BED, 1).setDisplayName("§cQuitter la partie").build());
			player.getInventory().setItem(4, new ItemCreator(Material.NAME_TAG, 1).setDisplayName("§3Choisir un kit").build());
			
			new ServerScoreboard(player, this.main.getWaitBoard()).set();
			
			Bukkit.broadcastMessage("§bSkyfly §7>> §6" + player.getName() + " §aa rejoint la partie ! §7(" + SkyFlyPlayer.getPlayers().size() + "/8)");
			
			if(size >= 4 && !WaitTask.running && !WaitTask.forced)
			
				new WaitTask().runTaskTimer(this.main, 0, 20l);
			
			if(size >= 6 && WaitTask.running && !WaitTask.forced) {
				
				WaitTask.getInstance().setTimer(30);
				
				for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values())
					
					players.getBukkitPlayer().playSound(players.getBukkitPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					
			}
				
			return;
			
		}else {
			
			if(SkyFlyPlayer.getPlayers().containsKey(player.getName())) {
				
				Bukkit.broadcastMessage("§bSkyFly §7>> §6" + player.getName() + " §as'est reconnecté !");
				
				ServerScoreboard.getScoreboard(player).refreshScoreboard();
				
				updateGlowColor(player, Teams.getPlayerTeam(player));
				
				return;
				
			}else {
				
				player.sendMessage("§cVous avez rejoint les spectateurs.");
				player.setGameMode(GameMode.SPECTATOR);
				
				player.teleport(new Location(Bukkit.getWorld("world"), 0, 150, 0));
				Teams.addPlayerInTeam(player, Teams.SPECTATOR);
				
				for(SkyFlyPlayer inGame : SkyFlyPlayer.getPlayers().values())
					
					this.effect.addViewer(inGame.getBukkitPlayer(), player);
				
				return;
				
			}
			
		}
		
	}
	
	@Override
	public void onDisable() {}
	
	@Override
	public void onDisconnect(Player player) {
		
		final Teams team = Teams.getPlayerTeam(player);
		
		/*Dve pvp 1.8*/
//		this.main.getPacketsManager().removePlayer(player);
		
		if(States.isState(States.WAIT)) {
			
			if(SkyFlyPlayer.getPlayers().containsKey(player.getName())) {
				
				SkyFlyPlayer.getPlayers().remove(player.getName());
				
				Bukkit.broadcastMessage("§bSkyfly §7>> §6" + player.getName() + " §ca quitté la partie ! §7(" + SkyFlyPlayer.getPlayers().size() + "/8)");
				
				if(SkyFlyPlayer.getPlayers().size() < 4 && !WaitTask.forced && WaitTask.running) {
					
					Bukkit.broadcastMessage("§cIl n'y a plus assez de joueurs pour lancer la partie. Annulation du chronomètre.");
					WaitTask.running = false;
					
				}
				
				Teams.removePlayer(player);
				
			}
			
			return;
			
		}else if(States.isState(States.GAME)) {
			
			if(team == null || team.equals(Teams.SPECTATOR))
				
				return;
			
			if(SkyFlyPlayer.getPlayers().containsKey(player.getName())) {
				
				final SkyFlyPlayer skyFlyPlayer = SkyFlyPlayer.getSFPlayer(player);
								
				this.effect.setGlowColor(player, team.equals(Teams.RED) ? EffectColor.RED : EffectColor.CYAN, false);
								
				Bukkit.broadcastMessage("§bSkyFly §7>> §6" + player.getName() + " §cs'est déconnecté. Il peut encore se reconnecter jusqu'à la fin de la partie.");
				
				if(skyFlyPlayer.hasKey()) {
					
					Bukkit.broadcastMessage("§6Une nouvelle clef est apparue au centre de la carte !");
					
					player.getInventory().remove(SkyFlyUtils.feather);
					player.updateInventory();
					
					SkyFlyUtils.spawnKey();
					
				}
				
			}
			
			return;
			
		}else
			
			Bukkit.broadcastMessage("§bSkyFly §7>> §6" + player.getName() + " §7a quitté la partie. §7(" + SkyFlyPlayer.getPlayers().size() + "/8)");
		
		return;
		
	}
	
	private void updateGlowColor(final Player connected, final Teams playerTeam) {
		
		for(SkyFlyPlayer sfPlayers : SkyFlyPlayer.getPlayers().values()) {
			
			final Player player = sfPlayers.getBukkitPlayer();
			final Teams teams = Teams.getPlayerTeam(player);
			
			if(!sfPlayers.getName().equals(connected.getName()))
				
				if(teams.equals(playerTeam))
				
					this.effect.addViewer(player, connected);
			
		}
		
		this.effect.setGlowColor(connected, playerTeam.equals(Teams.RED) ? EffectColor.RED : EffectColor.CYAN, true);
		
		@SuppressWarnings("unchecked")
		final HashSet<String> viewers = (HashSet<String>) playerTeam.getPlayers().clone();
		
		viewers.remove(connected.getName());
		
		this.effect.addViewerByNames(connected, viewers);
		
	}

}
