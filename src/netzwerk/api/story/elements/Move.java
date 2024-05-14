package netzwerk.api.story.elements;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import netzwerk.api.story.StoryAPI;
import netzwerk.api.story.StoryElement;

public class Move implements StoryElement {
	Location to, step;
	int moveTime;
	ArmorStand as;
	int tick;
	private Move(Location to, int moveTime) {
		super();
		this.to = to;
		this.moveTime = moveTime;
	}
	
	public static Move of(Location to, int moveTime) {
		return new Move(to, moveTime);
	}

	@Override
	public int show(ArmorStand as) {
		this.as = as;
		Location distance = to.clone().subtract(as.getLocation());
		distance.setYaw(to.getYaw() - as.getLocation().getYaw());
		distance.setPitch(to.getPitch() - as.getLocation().getPitch());
		step = new Location(to.getWorld(), distance.getX() / moveTime, distance.getY() / moveTime, distance.getZ() / moveTime, distance.getYaw() / moveTime, distance.getPitch() / moveTime);
		return moveTime;
	}

	@Override
	public void tick() {
		tick++;
		Location to = as.getLocation().add(step);
		to.setYaw(to.getYaw() + step.getYaw());
		to.setPitch(to.getPitch() + step.getPitch());
		as.teleport(to);
		if (tick % 2 == 1) {
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(StoryAPI.INSTANCE, () -> Message.onMove(as, to.add(0, as.getEyeHeight(), 0), false), 1);
		}
	}

	@Override
	public void end() {
		as.teleport(to);
		Bukkit.getScheduler().scheduleSyncDelayedTask(StoryAPI.INSTANCE, () -> Message.onMove(as, to.add(0, as.getEyeHeight(), 0), false), 1);
	}

}
