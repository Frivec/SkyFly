package fr.frivec.core.pvp.hitbox;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.frivec.core.player.SkyFlyPlayer;

public class HitBoxTask extends Thread {
	
	public static boolean running = true;
	
	public HitBoxTask() {
		
		super("HitBoxes");
		
	}
	
	@Override
	public void run() {
		
		while(running) {
			
			for(SkyFlyPlayer players : SkyFlyPlayer.getPlayers().values()) {
				
				final Player player = players.getBukkitPlayer();
				final Location location = player.getLocation();
				final World world = player.getWorld();
				
				final float yaw = player.getLocation().getYaw();
				final double radians = Math.toRadians(yaw);
				
				final Vector min = new Vector(location.getX() + Math.sin(radians + Math.PI/4) * 0.4, location.getY(), location.getZ() + Math.cos(radians + Math.PI/4) * 0.4),
							max = new Vector(location.getX() - Math.sin(radians + Math.PI/4) * 0.4, location.getY() + 1.8, location.getZ() - Math.cos(radians + Math.PI/4) * 0.4);
				
				player.getBoundingBox();
	        	
			}
			
			try {
				
				HitBoxTask.sleep(50);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		super.run();
	}

}
