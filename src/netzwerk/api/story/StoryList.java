package netzwerk.api.story;

import java.util.HashMap;

public class StoryList {
	HashMap<Integer, StoryElement> story = new HashMap<>();
	int end;
	
	public void add(int time, StoryElement storyElement) {
		story.put(time, storyElement);
		if (end < time) {
			end = time;
		}
	}

	public boolean contains(int tick) {
		return story.containsKey(tick);
	}

	public StoryElement get(int tick) {
		return story.get(tick);
	}
	
	public int getEnd() {
		return end;
	}
	
	public boolean isEnd(int tick) {
		return tick > getEnd();
	}
}
