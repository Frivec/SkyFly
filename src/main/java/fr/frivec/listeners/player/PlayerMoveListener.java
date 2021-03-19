package fr.frivec.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.frivec.core.statement.States;
import fr.frivec.core.utils.SkyFlyUtils;

public class PlayerMoveListener implements Listener {
	
	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		
		final Player player = event.getPlayer();
		
		if(States.isState(States.WAIT)) {
			
			if(!SkyFlyUtils.lobbyZone.isInCube(player)) {
				
				player.teleport(SkyFlyUtils.lobby);
				player.sendMessage("§7§oNe va pas trop loin ! Tu vas te perdre.");
				
			}else
				
				return;
			
		}else if(States.isState(States.GAME)) {
			
			if(player.getLocation().getY() <= 0) {
			
				player.damage(40.0);
				player.sendMessage("§7§oVous êtes tombé dans le vide...");
			
			}else
				
				return;
			
		}else
			
			if(player.getLocation().getY() <= 0)
			
				player.teleport(SkyFlyUtils.center);
		
	}

}
