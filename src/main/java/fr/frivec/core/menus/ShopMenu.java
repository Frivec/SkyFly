package fr.frivec.core.menus;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import fr.frivec.SkyFly;
import fr.frivec.core.player.MaterialLevel;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.spigot.item.ItemCreator;
import fr.frivec.spigot.menus.AbstractMenu;

public class ShopMenu extends AbstractMenu {
	
	public ShopMenu() {
		
		super(SkyFly.getInstance(), "§6Magasin", 9*4);
		
	}

	@Override
	public void open(Player player) {
		
		final SkyFlyPlayer skyFlyPlayer = SkyFlyPlayer.getSFPlayer(player);
		
		setItems(skyFlyPlayer);
		
		//TODO ajouter les items
		addItem(new ItemCreator(Material.GOLDEN_APPLE, 1).build(), 13, "BUY_GOLDENAPPLE");
		
		player.openInventory(this.inventory);
		
	}

	@Override
	public void close(Player player) {/*Not used*/}

	@Override
	public void onInteract(Player player, ItemStack itemStack, int slot, InventoryAction action) {
		
		final SkyFlyPlayer skyFlyPlayer = SkyFlyPlayer.getSFPlayer(player);
		
		final String itemAction = this.actions.get(slot);
		final String[] strs = itemAction.split("[_]");
		final String itemName = strs[1];
		
		if(itemAction.contains("UPGRADEITEM")) {
			
			/*
			 * 
			 * Armures
			 * 
			 */
			
			MaterialLevel item = null;
			
			for(MaterialLevel items : skyFlyPlayer.getItems())
				
				if(items.getName().equalsIgnoreCase(itemName))
					
					item = items;
			
			if(item != null) {
				
				if(player.getLevel() >= item.getPriceByLevel(item.getLevel() + 1)) {
					
					player.setLevel(player.getLevel() - item.getPriceByLevel(item.getLevel() + 1));
				
					item.setLevel(item.getLevel() + 1);
					skyFlyPlayer.giveMaterialsLevels();
					
					player.sendMessage("§aVous avez acheté: §6" + itemName + " niveau " + item.getLevel());
					
					setItems(skyFlyPlayer);
					
					player.updateInventory();
					
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
					
					return;
					
				}else {
					
					player.sendMessage("§cVous n'avez pas assez de niveaux.");
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
					
					return;
					
				}	
				
			}else {
				
				player.closeInventory();
				player.sendMessage("§cErreur. Cet item n'a pas été trouvé. Merci de signaler ce bug.");
				
				return;
				
			}
			
		}else if(itemAction.contains("BUY")) {
			
			/*
			 * 
			 * Items bonus
			 * 
			 */
			
			switch (itemName) {
			
			case "GOLDENAPPLE":
				
				if(player.getLevel() >= 2) {
					
					player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
					player.sendMessage("§aVous avez acheté: §6Pomme en or");
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
					player.setLevel(player.getLevel() - 2);
					
				}else {
					
					player.sendMessage("§cVous n'avez pas assez de niveaux pour acheter cet objet.");
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
					
				}
				
				break;

			default:
				
				player.sendMessage("§cErreur. Cet objet n'est pas reconnu. Merci de faire remonter ce bug !");
				
				break;
			}
			
		}
		
	}
	
	private void setItems(final SkyFlyPlayer skyFlyPlayer) {
		
		for(MaterialLevel items : skyFlyPlayer.getItems()) {
			
			if((items.getLevel() + 1) == 4)
				
				this.addItem(new ItemCreator(Material.BARRIER, 1).setDisplayName("§cNiveau maximum atteint").build(), items.getSlot(), "NOTHING");
			
			else 
				
				this.addItem(items.getItemByLevel(items.getLevel() + 1, true), items.getSlot(), "UPGRADEITEM_" + items.getName());
			
		}
		
		final ItemStack soon = new ItemCreator(Material.PLAYER_HEAD, 1).skull("MHF_Question").setDisplayName("§cBientôt").build();
		
		for(int i = 13; i <= 15; i++)
			
			this.addItem(soon, i, "NOTHING");
		
		for(int i = 22; i <= 24; i++)
			
			this.addItem(soon, i, "NOTHING");
		
	}
	
}
