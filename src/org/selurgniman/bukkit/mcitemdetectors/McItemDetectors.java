/**
 * 
 */
package org.selurgniman.bukkit.mcitemdetectors;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 * @author <a href="mailto:e83800@wnco.com">Chris Bandy</a>
 * Created on: Jun 7, 2011
 */
public class McItemDetectors extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft."+McItemDetectors.class.getName());
    private Configuration config=null;
    
    // NOTE: There should be no need to define a constructor any more for more info on moving from
    // the old constructor see:
    // http://forums.bukkit.org/threads/too-long-constructor.5032/

    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

    	config.save();
        log.info("McItemDetectors shut down");
    }

    public void onEnable() {
    	config = this.getConfiguration();
    	config.load();
    	List<World> worlds=this.getServer().getWorlds();
    	
    	for (World world:worlds){
    		String worldName=world.getName();
    		List<String> rules=config.getStringList("McItemDetectors."+worldName+".extinct", null);
    		if (rules.size()==0){
    			config.setProperty("McItemDetectors."+world.getName(),"");
    			config.save();
    		}
    		
    		Collections.sort(rules);
    	}
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, new PlayerListener(){
			public void onPlayerInteract(PlayerInteractEvent event) {
				Block clickedBlock = event.getClickedBlock();
				Block locationBlock = clickedBlock.getFace(event.getBlockFace());
				ItemStack heldItem = event.getPlayer().getItemInHand();
				Material heldItemType = heldItem.getType();
				if (clickedBlock.getType() == Material.CHEST && locationBlock.getType() == Material.AIR){
					if (heldItemType == Material.STONE_BUTTON ||
							heldItemType == Material.STONE_PLATE || 
							heldItemType == Material.WOOD_PLATE ||
							heldItemType == Material.LEVER){
						locationBlock.setType(heldItem.getType());
						heldItem.setAmount(heldItem.getAmount()-1);
					}
				} else if (clickedBlock.getType() == Material.STONE_BUTTON){
					Button button = (Button)clickedBlock;
					
				}
			}
		}, Priority.Normal, this);
		

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        
    }

}
