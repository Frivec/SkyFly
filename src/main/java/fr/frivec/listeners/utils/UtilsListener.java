package fr.frivec.listeners.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.server.ServerListPingEvent;

import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.statement.States;

public class UtilsListener implements Listener {
	
	@EventHandler
	public void onFoodChange(final FoodLevelChangeEvent event) {
		
		event.setFoodLevel(20);
		
	}
	
	@EventHandler
	public void onServerPing(final ServerListPingEvent event) {
		
		switch (States.getCurrentState()) {
		
		case WAIT:
			
			final int size = SkyFlyPlayer.getPlayers().size();
			
			if(size == 12)
				
				event.setMotd("§bSkyFly §b§a| §6Volez d'îles en îles pour atteindre la clef" + "\n" + "§aPartie pleine ! §7(" + SkyFlyPlayer.getPlayers().size() + "/8)");
				
			else
				
				event.setMotd("§bSkyFly §b§a| §6Volez d'îles en îles pour atteindre la clef" + "\n" + "§aEn attente de joueurs §7(" + SkyFlyPlayer.getPlayers().size() + "/8)");
				
			break;
			
		case GAME:
			
			event.setMotd("§bSkyFly §b§a| §6Volez d'îles en îles pour atteindre la clef" + "\n" + "§cPartie en cours de jeu !");
			
			break;

		case END:
			
			event.setMotd("§bSkyFly §b§a| §6Volez d'îles en îles pour atteindre la clef" + "\n" + "§cPartie terminée ! Redémarrage bientôt !");
			
			break;
			
		default:
			break;
		}
		
	}

}
