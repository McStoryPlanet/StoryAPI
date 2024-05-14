package netzwerk.api.story.elements;

import java.util.List;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import netzwerk.api.story.StoryElement;

public class Chat implements StoryElement {
	Component message;
	private Chat(Component message) {
		this.message = message;
	}

	public static Chat of(Component message) {
		return new Chat(message);
	}
	
	public static Chat of(Component sender, Component message) {
		return new Chat(Component.empty().append(Component.text("<")).append(sender).append(Component.text("> ")).append(message));
	}
	
	@Override
	public int show(ArmorStand as) {
		List<Player> players = as.getLocation().getWorld().getNearbyPlayers(as.getLocation(), 0.1).stream().filter(player -> player.getSpectatorTarget().equals(as)).toList();
		players.forEach(player -> {
			player.sendMessage(message);
		});
		return 0;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}

}
