package netzwerk.api.story;

import java.util.List;

import org.bukkit.entity.Player;

public abstract class Story {
	public abstract void setStory(StoryList list);
	public abstract void onEnd(List<Player> players);
}
