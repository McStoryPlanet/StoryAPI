package netzwerk.api.story;

import org.bukkit.entity.ArmorStand;

public interface StoryElement {
	public int show(ArmorStand as);
	public void tick();
	public void end();
}
