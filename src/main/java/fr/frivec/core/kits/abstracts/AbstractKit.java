package fr.frivec.core.kits.abstracts;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.frivec.SkyFly;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.spigot.item.ItemCreator;

public abstract class AbstractKit implements Listener {
	
	/**
	 * 
	 * AbstractKit - Created by Frivec
	 * Easier way to create a kit. 
	 * 
	 */
	
	protected String name;
	protected int price;
	protected ItemStack icon;
	protected int slot;
	
	/**
	 * Create a kit
	 * @param name - String: Name of the kit
	 * @param slot - Integer: Slot of the kit's icon in the kits' menu
	 * @param price - Integer: Theorical price of the kit | Not used !
	 * @param icon - ItemStack: Icon of the kit in the kits' menu
	 */
	public AbstractKit(String name, int slot, int price, ItemStack icon) {
		
		this.name = name;
		this.slot = slot;
		this.price = price;
		this.icon = icon;
		
		//Register this class as a bukkit listener 
		SkyFly.getInstance().getListenerManager().registerListener(this);
		
	}
	
	/**
	 * Give all the items of the kit to the player
	 * @param player - Player: Player that will receive all the items
	 */
	public abstract void getItems(final Player player);
	
	
	/**
	 * Give the kit to the player. Give the items specified to the kit and the items commons to all kits
	 * @param player - Player: the player that will receive the kit
	 */
	public void giveKit(Player player) {
		
		final PlayerInventory inventory = player.getInventory(); //Player's inventory
		
		//Get the items of the player's kit
		getItems(player);
		
		//Give the upgrade items 
		SkyFlyPlayer.getSFPlayer(player).giveMaterialsLevels();
		
		//Give an arrow and the elytras
		inventory.setItem(9, new ItemStack(Material.ARROW, 1));
		inventory.setChestplate(new ItemCreator(Material.ELYTRA, 1).unbreakable(true).build());	
		
		//Give the bow and a firework rocket to the player. 
		inventory.addItem(new ItemCreator(Material.BOW, 1).unbreakable(true).addEnchantment(Enchantment.ARROW_INFINITE, 1).setDisplayName("§aArc").build());
		inventory.addItem(new ItemCreator(Material.FIREWORK_ROCKET, 1).firework(0, new FireworkEffect[] {}).setDisplayName("§bPropulseur").build());
		
	}
	
	public int getSlot() {
		return slot;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getIcon() {
		return this.icon;
	}
		
}
