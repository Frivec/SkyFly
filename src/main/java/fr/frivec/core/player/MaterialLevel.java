package fr.frivec.core.player;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import fr.frivec.spigot.item.ItemCreator;

public class MaterialLevel {
	
	private String name;
	private int level,
				slot;
	private Material material;
	private boolean leatherArmor;
	private Color color;
	private HashMap<Integer, Integer> prices;
	
	public MaterialLevel(final String name, final Material material, final int slot) {
		
		this(name, material, false, null, slot);
		
	}
	
	public MaterialLevel(final String name, final Material material, final boolean leatherArmor, final Color color, final int slot) {
	
		this.name = name;
		this.level = 1;
		this.material = material;
		this.leatherArmor = leatherArmor;
		this.color = color;
		this.slot = slot;
		this.prices = new HashMap<Integer, Integer>();
		
	}
	
	public MaterialLevel registerPrice(final int level, final int price) {
		
		if(this.prices.containsKey(level))
			
			this.prices.remove(level);
		
		this.prices.put(level, price);
		
		return this;
		
	}
	
	public int getPriceByLevel(final int level) {
		
		return this.prices.get(level);
		
	}
	
	public ItemStack getItemByLevel(final int level, final boolean buying) {
		
		ItemCreator item = new ItemCreator(this.material, 1);
		
		switch (level) {
		
		case 1:
			
			if(this.leatherArmor)
				
				item.leatherArmor(this.material, this.color);
			
			break;
			
		case 2:
			
			if(this.leatherArmor)
				
				item.leatherArmor(this.material, this.color).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			
			else
				
				item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			
			break;
			
		case 3:
			
			if(this.leatherArmor)
				
				item.leatherArmor(this.material, this.color).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			
			else
				
				item.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			
			break;

		default:
			
			item = new ItemCreator(Material.BARRIER, 1).setDisplayName("§cErreur 404").setLores(Arrays.asList("§5Item non chargé correctement.", "Level not found"));
			
			break;
			
		}
		
		if(!item.build().getType().equals(Material.BARRIER)) {
			
			item.unbreakable(true).addItemFlag(new ItemFlag[] {ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}).setDisplayName(this.name + " niveau " + level);
			
			if(buying)
				
				item.setLores(Arrays.asList("§aPrix: §3" + this.prices.get(level) + " §aniveaux"));
			
		}
		
		return item.build();
		
	}
	
	public int getSlot() {
		return slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public boolean isLeatherArmor() {
		return leatherArmor;
	}

	public void setLeatherArmor(boolean leatherArmor) {
		this.leatherArmor = leatherArmor;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
