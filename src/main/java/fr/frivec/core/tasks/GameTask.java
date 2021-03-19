package fr.frivec.core.tasks;

import java.text.SimpleDateFormat;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.frivec.SkyFly;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.teams.Teams;
import fr.frivec.core.utils.SkyFlyUtils;
import fr.frivec.spigot.scoreboard.server.ServerScoreboard;

public class GameTask extends BukkitRunnable {
	
	private static GameTask instance;
	
	private SkyFly main;
	private static int timer = 0;
	
	public GameTask() {
		
		instance = this;
		
		this.main = SkyFly.getInstance();
		
		SkyFlyUtils.spawnKey();
		
	}
	
	@Override
	public void run() {
		
		for(SkyFlyPlayer sfPlayers : SkyFlyPlayer.getPlayers().values()) {
			
			final Player player = sfPlayers.getBukkitPlayer();
			
			if(player == null || !player.isOnline())
				
				continue;
			
			if(Teams.getPlayerTeam(player).equals(Teams.SPECTATOR))
				
				continue;
			
			this.main.getGameBoard().setLine(8, "Temps: §a" + new SimpleDateFormat("mm:ss").format(GameTask.getTimer() * 1000));
			this.main.getGameBoard().setLine(1, "Scores: §c" + Teams.RED.getPoints() + " §r- §b" + Teams.BLUE.getPoints());
			this.main.getGameBoard().setLine(5, "  -§aKills: §r" + sfPlayers.getKills());
			this.main.getGameBoard().setLine(6, "  -§aPoints: §r" + sfPlayers.getPoints());

			ServerScoreboard.getScoreboard(player).setPattern(this.main.getGameBoard());
			ServerScoreboard.getScoreboard(player).refreshScoreboard();
			
		}
		
		if(timer%30 == 0)
			
			for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values())
				
				if(players.getBukkitPlayer() != null)
				
					players.getBukkitPlayer().giveExp(10);
		
		timer++;
		
	}
	
	public static GameTask getInstance() {
		return instance;
	}
	
	public static int getTimer() {
		return timer;
	}
	
	public void setTimer(int timer) {
		GameTask.timer = timer;
	}

}
