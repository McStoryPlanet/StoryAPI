package netzwerk.api.story.elements;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import netzwerk.api.story.StoryAPI;
import netzwerk.api.story.StoryElement;

public class Teleport implements StoryElement {
	Location to;
	private Teleport(Location to) {
		this.to = to;
	}

	public static Teleport of(Location to) {
		return new Teleport(to);
	}
	
	@Override
	public int show(ArmorStand as) {
		System.out.println("tp");
		List<Player> players = null;
		if (!as.getLocation().getWorld().equals(to.getWorld())) {
			players = as.getLocation().getWorld().getNearbyPlayers(as.getLocation(), 0.1).stream().filter(player -> player.getSpectatorTarget().equals(as)).toList();
			players.forEach(player -> {
				player.setSpectatorTarget(null);
			});
		}
		as.teleport(to);
		List<Player> finalPlayers = players;
		if (players != null) {
			players.forEach(player -> {
				player.teleport(to);
			});
			Bukkit.getScheduler().scheduleSyncDelayedTask(StoryAPI.INSTANCE, () -> {
				finalPlayers.forEach(player -> {
					player.teleport(to);
					player.setSpectatorTarget(as);
				});
			}, 10);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(StoryAPI.INSTANCE, () -> Message.onMove(as, to.add(0, as.getEyeHeight(), 0), true), 2);
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
