package netzwerk.api.story;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;

public class Listener implements org.bukkit.event.Listener{
	@EventHandler
	public void onCancelSpectate(PlayerStopSpectatingEntityEvent event) {
		if (event.getSpectatorTarget() instanceof ArmorStand as) {
			event.setCancelled(StoryManager.isStoryArmorstand(as));
		}
	}
}
