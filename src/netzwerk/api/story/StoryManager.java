package netzwerk.api.story;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class StoryManager {
	private static Story currentStory;
	private static StoryList list;
	private static int tick;
	private static HashMap<StoryElement, Integer> currentStoryElements = new HashMap<>();
	private static List<Player> players;
	private static ArmorStand camera;
	public static void tick() {
		if (currentStory == null) {
			return;
		}
		tick++;
		if (list == null) {
			list = new StoryList();
			currentStory.setStory(list);
			tick = 0;
			currentStoryElements.clear();
		}
		List<StoryElement> remove = new ArrayList<>();
		for (Map.Entry<StoryElement, Integer> entry : currentStoryElements.entrySet()) {
			System.out.println("tick storyElements");
			StoryElement key = entry.getKey();
			int val = entry.getValue();
			key.tick();
			val--;
			if (val == 0) {
				remove.add(key);
			}else {
				currentStoryElements.replace(key, val);
			}
		}
		for (StoryElement storyElement : remove) {
			storyElement.end();
			currentStoryElements.remove(storyElement);
		}
		if (list.contains(tick)) {
			StoryElement nextElement = list.get(tick);
			int time = nextElement.show(camera);
			if (time != 0) {
				currentStoryElements.put(nextElement, time);
			}
		}
		System.out.println(list.isEnd(tick) + " " + list.getEnd());
		if (currentStoryElements.isEmpty() && list.isEnd(tick)) {
			currentStory.onEnd(players);
			currentStory = null;
			list = null;
			camera.remove();
			camera = null;
			players.forEach(p -> {
				if (p.getGameMode() == GameMode.SPECTATOR) {
					p.setSpectatorTarget(null);
				}
			});
			players = null;
		}
	}
	
	public static void playStory(Story story, List<Player> players) {
		if (currentStory != null) {
			throw new IllegalStateException("There is currently a played Story");
		}
		currentStory = story;
		list = null;
		camera = players.get(0).getWorld().spawn(players.get(0).getLocation(), ArmorStand.class);
		camera.setInvisible(true);
		camera.setInvulnerable(true);
		camera.setAI(false);
		camera.setGravity(false);
		players.forEach(p -> {
			p.setGameMode(GameMode.SPECTATOR);
			p.setSpectatorTarget(camera);
		});
		StoryManager.players = players;
	}
	
	public static boolean isStoryArmorstand(ArmorStand armor) {
		if (armor == null || camera == null) {
			return false;
		}
		return armor.getEntityId() == camera.getEntityId();
	}
}
