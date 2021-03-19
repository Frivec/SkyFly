package fr.frivec.core.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import fr.frivec.SkyFly;
import fr.frivec.core.teams.Teams;
import fr.frivec.spigot.item.ItemCreator;
import fr.frivec.spigot.menus.AbstractMenu;

public class TeamMenu extends AbstractMenu {
	
	public TeamMenu() {
		
		super(SkyFly.getInstance(), "§9Choix des équipes", 9);
		
	}
	
	@Override
	public void close(Player player) {}
	
	@Override
	public void onInteract(Player player, ItemStack item, int slot, InventoryAction action) {
		
		if(!this.actions.containsKey(slot))
			
			return;
		
		if(this.actions.get(slot).contains("JOINTEAM")) {
			
			Teams team = null;
			
			if(this.actions.get(slot).contains("RED")) {
				
				team = Teams.RED;
				
				player.sendMessage("§aVous rejoignez l'équipe §crouge§a.");
			
			}else {
				
				team = Teams.BLUE;
				
				player.sendMessage("§aVous rejoignez l'équipe §bbleue§a.");
				
			}
			
			Teams.addPlayerInTeam(player, team);
			
		}
		
		return;
		
	}
	
	@Override
	public void open(Player player) {
		
		addItem(new ItemCreator(Material.LAPIS_LAZULI, 1).setDisplayName("§bÉquipe bleue").build(), 3, "JOINTEAM_BLUE");
		addItem(new ItemCreator(Material.REDSTONE, 1).setDisplayName("§cÉquipe rouge").build(), 5, "JOINTEAM_RED");
		
		player.openInventory(this.inventory);
		
	}

}
