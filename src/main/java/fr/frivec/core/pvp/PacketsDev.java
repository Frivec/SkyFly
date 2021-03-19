package fr.frivec.core.pvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.frivec.core.logger.Logger.LogLevel;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.spigot.API;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_16_R2.EnumHand;
import net.minecraft.server.v1_16_R2.PacketPlayInArmAnimation;

public class PacketsDev extends ChannelDuplexHandler {

	private Player player;
	
	public PacketsDev(final Player player) {
		
		this.player = player;
		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
		
		if(packet instanceof PacketPlayInArmAnimation) {
			
//			final EntityPlayer entityPlayer = ((CraftPlayer) this.player).getHandle();
			final PacketPlayInArmAnimation armAnimation = (PacketPlayInArmAnimation) packet;
			final EnumHand animation = armAnimation.b();
			
			if(animation.equals(EnumHand.MAIN_HAND)) {
				
				final List<Player> inRange = new ArrayList<>();
				
				for(SkyFlyPlayer sPlayers : SkyFlyPlayer.getPlayers().values()) {
				
					final Player pl = sPlayers.getBukkitPlayer();
					final Location location = pl.getLocation(),
									center = player.getLocation();
					
					if(Math.sqrt(Math.pow(location.getX() - center.getX(), 2) + Math.pow(location.getY() - center.getY(), 2) + Math.pow(location.getZ() - center.getZ(), 2)) <= 4.5)
						
						inRange.add(pl);
					
				}
				
				API.log(LogLevel.INFO, inRange.toString());
				
			}
			
		}
		
		super.channelRead(ctx, packet);
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
		
		
		
		super.write(ctx, packet, promise);
	}
	
}
