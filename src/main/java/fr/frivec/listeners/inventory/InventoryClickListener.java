package fr.frivec.listeners.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import fr.frivec.SkyFly;
import fr.frivec.core.menus.KitsMenu;
import fr.frivec.core.menus.TeamMenu;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.statement.States;
import fr.frivec.spigot.item.ItemCreator;
import fr.frivec.spigot.menus.MenuManager;
import fr.frivec.spigot.title.ActionBar;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		
		final Player player = event.getPlayer();
		final SkyFlyPlayer flyPlayer = SkyFlyPlayer.getSFPlayer(player);
		final ItemStack item = event.getItem();
		final Action action = event.getAction();
		
		if(States.isState(States.WAIT) && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
			
			if(item != null)
			
				if(item.getType().equals(Material.NAME_TAG) && MenuManager.getMenu(player) == null)
					
					SkyFly.getInstance().getManagers().getMenuManager().onOpenMenu(player, new KitsMenu());
				
				else if(item.getType().equals(Material.RED_BED))
					
					player.kickPlayer("§aMerci d'être passé ! En espérant vous revoir !");
				
				else if(item.getType().equals(Material.BUBBLE_CORAL_FAN) && MenuManager.getMenu(player) == null)
					
					SkyFly.getInstance().getManagers().getMenuManager().onOpenMenu(player, new TeamMenu());
			
			return;
			
		}else if(States.isState(States.GAME) && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
			
			if(item != null && item.getType().equals(Material.FIREWORK_ROCKET)) {
				
				if(!player.hasMetadata("PROPULSOR")) {
					
					if(!player.isGliding()) {
						
						player.sendMessage("§cVous ne volez pas. Sautez dans le vide et activez vos élytres d'abord.");
						event.setCancelled(true);
						
						return;
						
					}
							
					player.setMetadata("PROPULSOR", new FixedMetadataValue(SkyFly.getInstance(), true));
					
					Bukkit.getScheduler().runTask(SkyFly.getInstance(), () ->{
						
						player.getInventory().addItem(new ItemCreator(Material.FIREWORK_ROCKET, 1).setDisplayName("§bPropulseur").build());
						
					});
					
					new BukkitRunnable() {
						
						int timer = flyPlayer.hasKey() ? 10 : 5;
						
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
					
				}else {
					
					player.sendMessage("§cVeuillez patienter.");
					event.setCancelled(true);
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onClickInventory(final InventoryClickEvent event) {
		
		final Player player = (Player) event.getWhoClicked();
		final ItemStack item = event.getCurrentItem();
		
		if(States.isState(States.WAIT)) {
			
			if(item != null && MenuManager.getMenu(player) == null) {
				
				event.setCancelled(true);
				
				if(item.getType().equals(Material.NAME_TAG))
					
					SkyFly.getInstance().getManagers().getMenuManager().onOpenMenu(player, new KitsMenu());
				
				else if(item.getType().equals(Material.BUBBLE_CORAL_FAN))
					
					SkyFly.getInstance().getManagers().getMenuManager().onOpenMenu(player, new TeamMenu());
				
				else if(item.getType().equals(Material.RED_BED))
					
					player.kickPlayer("§aMerci d'être passé ! En espérant vous revoir !");
				
				else
					
					return;
				
			}else
				
				return;
			
		}
		
	}
	
	@EventHandler
	public void onDrop(final PlayerDropItemEvent event) {
		
		if(!States.isState(States.END))
			
			event.setCancelled(true);
		
	}

}
