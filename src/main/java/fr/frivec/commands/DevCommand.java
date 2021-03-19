package fr.frivec.commands;

import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.frivec.SkyFly;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.statement.States;
import fr.frivec.core.tasks.WaitTask;
import fr.frivec.core.utils.SkyFlyUtils;
import fr.frivec.spigot.commands.AbstractCommand;

public class DevCommand extends AbstractCommand {
	
	/**
	 * 
	 * Dev Command. Used to test some features of the plugin
	 * Use the AbstractCommand class as parent from the API
	 * 
	 */
	
	//DevMod is used to modify the map while the plugin is installed and is running
	public static boolean devMode = false;
	
	//Creation of the command
	public DevCommand() {
		super("devgame", "§c/devgame coucou", "Dev command for the skyfly", "§cPetit malandrin. Tu n'as pas le droit.", Arrays.asList("meh"));
	}
	
	//Command
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			
			final Player player = (Player) sender;
			
			if(!player.isOp()) {
				
				player.sendMessage(this.permMessage);
				return true;
				
			}
				
			if(args.length > 0) {
				
				//Change the state of the game in order to modify the map for example.
				if(args[0].equalsIgnoreCase("states")) {
					
					final String stateName = args[1];
					
					for(States states : States.values())
						
						if(states.toString().equalsIgnoreCase(stateName))
							
							States.setCurrentState(states);
					
					player.sendMessage("§aState changed to: " + stateName);
					
					return true;
					
				}else if(args[0].equalsIgnoreCase("start")) { //Force start for the game. Only works if the initial timer isn't at less than 10 seconds.
					
					if(States.isState(States.WAIT) && !WaitTask.running && !WaitTask.forced) {
						
						WaitTask.forced = true;
						
						if(WaitTask.running && WaitTask.getInstance().getTimer() > 10)
							
							WaitTask.getInstance().setTimer(10);
						
						else {
						
							new WaitTask().runTaskTimer(SkyFly.getInstance(), 0, 20l);
							WaitTask.getInstance().setTimer(10);
							
						}
						
						for(SkyFlyPlayer sfPlayers : SkyFlyPlayer.getPlayers().values())
							
							sfPlayers.getBukkitPlayer().playSound(sfPlayers.getBukkitPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
						
						player.sendMessage("§aDémarrage forcé lancé");
						
					}
					
					return true;
					
				}else if(args[0].equalsIgnoreCase("key")) {
					
					//Give a keyto the player in order to give a point to the player's team.
					
					SkyFlyPlayer.getSFPlayer(player).setKey(true);
					player.getInventory().addItem(SkyFlyUtils.feather);
					player.sendMessage("§6Cadeau espèce de tricheur");
					
					return true;
					
				}else if(args[0].equalsIgnoreCase("devmode")) {
					
					//Enable or disable the devMode.
					
					if(devMode) {
						
						devMode = false;
						player.sendMessage("§aDev mode désactivé");
						
					}else {
						
						devMode = true;
						player.sendMessage("§aDev mode activé.");
						
					}
					
				}
				
			}
			
		}else {
			
			//The command only works with a player object. The console can't use it.
			
			sender.sendMessage("§cSeulement pour les joueurs");
			
			return true;
			
		}
		
		return false;
	}

}
