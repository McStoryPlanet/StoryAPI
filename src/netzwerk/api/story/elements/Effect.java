package netzwerk.api.story.elements;

import org.bukkit.entity.ArmorStand;
import org.bukkit.potion.PotionEffect;

import netzwerk.api.story.StoryElement;

public class Effect implements StoryElement {
	PotionEffect effect;
	int duration;
	ArmorStand as;
	private Effect(PotionEffect effect, int duration) {
		this.effect = effect;
		this.duration = duration;
	}

	public static Effect of(PotionEffect effect, int duration) {
		return new Effect(effect, duration);
	}
	
	@Override
	public int show(ArmorStand as) {
		this.as = as;
		as.getWorld().getNearbyPlayers(as.getLocation(), 0.1).forEach(p -> {
			p.addPotionEffect(effect);
		});;
		return duration;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
		as.getWorld().getNearbyPlayers(as.getLocation(), 0.1).forEach(p -> {
			p.removePotionEffect(effect.getType());
		});;
	}

}
