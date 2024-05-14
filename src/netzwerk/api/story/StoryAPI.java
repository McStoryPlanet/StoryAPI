package netzwerk.api.story;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class StoryAPI extends JavaPlugin{
	public static StoryAPI INSTANCE;
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		Bukkit.getPluginManager().registerEvents(new Listener(), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, StoryManager::tick, 0, 1);
	}
}
