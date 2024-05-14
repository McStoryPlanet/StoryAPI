package netzwerk.api.story.elements;

import org.bukkit.entity.ArmorStand;

import netzwerk.api.story.StoryElement;

public class Sound implements StoryElement {
	org.bukkit.Sound sound;
	private Sound(org.bukkit.Sound sound) {
		this.sound = sound;
	}

	public static Sound of(org.bukkit.Sound sound) {
		return new Sound(sound);
	}
	
	@Override
	public int show(ArmorStand as) {
		as.getWorld().playSound(as, sound, 2, 1);;
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
