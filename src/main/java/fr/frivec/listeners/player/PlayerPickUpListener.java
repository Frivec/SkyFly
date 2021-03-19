package fr.frivec.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import fr.frivec.SkyFly;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.teams.Teams;
import fr.frivec.core.utils.SkyFlyUtils;
import fr.frivec.spigot.title.ActionBar;

public class PlayerPickUpListener implements Listener {
	
	@EventHandler
	public void onPickUp(final EntityPickupItemEvent event) {
		
		if(event.getEntity() instanceof Player) {
			
			final Player player = (Player) event.getEntity();
			final SkyFlyPlayer flyPlayer = SkyFlyPlayer.getSFPlayer(player);
			final ItemStack item = event.getItem().getItemStack();
			
			if(item.getItemMeta().getDisplayName().contentEquals(SkyFlyUtils.feather.getItemMeta().getDisplayName()) && item.getType().equals(SkyFlyUtils.feather.getType())) {
				
				flyPlayer.setKey(true);
				Bukkit.broadcastMessage("§6Attention ! " + (Teams.getPlayerTeam(player).equals(Teams.RED) ? "§c" : "§b") + player.getName() + " §6a récupéré la clef !");
				
				player.setMetadata("PROPULSOR", new FixedMetadataValue(SkyFly.getInstance(), true));
				
				new BukkitRunnable() {
					
					int timer = 10;
					
					@Override
					public void run() {
						
						new ActionBar("§cPropulseur disponible dans: §6" + timer + " §c" + (timer == 1 ? "seconde" : "secondes"), 10, 20, 10).send(player);
						
						if(timer == 0) {
						
							player.removeMetadata("PROPULSOR", SkyFly.getInstance());
							
							new ActionBar("§aPropulseur disponible !", 10, 20, 10).send(player);
							
							cancel();
							
						}
						
						timer--;
						
					}
					
				}.runTaskTimer(SkyFly.getInstance(), 0L, 20L);
				
			}else
				
				return;
			
		}
		
	}

}
