package fr.frivec.core.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import fr.frivec.core.hitboxs.HitBox;
import fr.frivec.core.kits.KitManager;
import fr.frivec.core.kits.abstracts.AbstractKit;

public class SkyFlyPlayer {
	
	private static Map<String, SkyFlyPlayer> players = new HashMap<>();
	
	private String name;
	private UUID uuid;
	private HitBox hitbox;
	private KitManager kits;
	private boolean key;
	
	private HashSet<MaterialLevel> items;
	
	private int kills = 0,
				deaths = 0,
				points = 0;
	
	public SkyFlyPlayer(final Player player) {
		
		this.name = player.getName();
		this.uuid = player.getUniqueId();
		this.key = false;
		this.kits = KitManager.PYROMANIAC;
		
		this.items = new HashSet<MaterialLevel>();
		
		players.put(player.getName(), this);
		
	}
	
	public AbstractKit getKit() {
		
		if(getKits() != null)
			
			return getKits().getKit();
		
		return null;
		
	}
	
	public void giveMaterialsLevels() {
		
		final PlayerInventory inventory = this.getBukkitPlayer().getInventory();
		
		//Get the items with levels - See the class MaterialLevel for more information - Gives the sword and the 
		for(MaterialLevel item : this.getItems()) {
			
			final int level = item.getLevel();
			
			if(item.isLeatherArmor()) {
				
				final String materialName = item.getMaterial().name();
				
				if(materialName.contains("HELMET"))
					
					inventory.setHelmet(item.getItemByLevel(level, false));
				
				else if(materialName.contains("LEGGING"))
					
					inventory.setLeggings(item.getItemByLevel(level, false));
				
				else if(materialName.contains("BOOTS"))
					
					inventory.setBoots(item.getItemByLevel(level, false));
				
			}else
				
				inventory.setItem(0, item.getItemByLevel(level, false));
			
		}	
		
	}
	
	public static SkyFlyPlayer getSFPlayer(final Player player) {
		
		if(players.containsKey(player.getName()))
			
			return players.get(player.getName());
		
		return new SkyFlyPlayer(player);
		
	}
	
	public String getName() {
		return name;
	}
	
	public HashSet<MaterialLevel> getItems() {
		return items;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}

	public KitManager getKits() {
		return kits;
	}

	public void setKits(KitManager kits) {
		this.kits = kits;
	}
	
	public static Map<String, SkyFlyPlayer> getPlayers() {
		return players;
	}
	
	public HitBox getHitbox() {
		return hitbox;
	}
	
	public void setHitbox(HitBox hitbox) {
		this.hitbox = hitbox;
	}
	
	public boolean hasKey() {
		return this.key;	
	}
	
	public void setKey(boolean key) {
		this.key = key;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isKey() {
		return key;
	}

}
