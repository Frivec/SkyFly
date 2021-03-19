package fr.frivec.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.frivec.SkyFly;

public class PlayerJoinListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(final PlayerJoinEvent event) {
		
		final Player player = event.getPlayer();
		
		event.setJoinMessage("");
		
		SkyFly.getInstance().getManagers().onConnect(player);
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onDisconnect(final PlayerQuitEvent event) {
		
		event.setQuitMessage("");
		
		final Player player = event.getPlayer();
		
		SkyFly.getInstance().getManagers().onDisconnect(player);
		
	}

}
