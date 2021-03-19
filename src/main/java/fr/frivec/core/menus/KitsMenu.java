package fr.frivec.core.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import fr.frivec.SkyFly;
import fr.frivec.core.kits.KitManager;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.statement.States;
import fr.frivec.spigot.menus.AbstractMenu;

public class KitsMenu extends AbstractMenu {
	
	public KitsMenu() {
		
		super(SkyFly.getInstance(), "§aChoix du Kit", 9*1);
		
	}
	
	@Override
	public void close(Player player) {}
	
	@Override
	public void onInteract(Player player, ItemStack item, int slot, InventoryAction action) {
		
		final SkyFlyPlayer skyFlyPlayer = SkyFlyPlayer.getSFPlayer(player);
		
		if(States.isState(States.WAIT)) {
			
			if(item != null) {
				
				final String[] kitName = this.actions.get(slot).split("[_]");
				final KitManager kit = KitManager.getKitByName(kitName[1]);
				
				skyFlyPlayer.setKits(kit);
				
				player.sendMessage("§aVous avez choisi le kit: §b" + skyFlyPlayer.getKit().getName());
				
			}else
				
				return;
			
		}
		
	}
	
	@Override
	public void open(Player player) {
		
		for(KitManager kits : KitManager.values())
			
			addItem(kits.getKit().getIcon(), kits.getKit().getSlot(), "GETKIT_" + kits.getName());
		
		player.openInventory(this.inventory);
		
	}

}
