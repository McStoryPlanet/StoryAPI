package netzwerk.api.story.elements;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.kyori.adventure.text.Component;
import netzwerk.api.story.StoryElement;

public class Message implements StoryElement{
	private static HashMap<ArmorStand, TextDisplay> displays = new HashMap<>();
	private static HashMap<ArmorStand, Float> offsetXs = new HashMap<>();
	private static HashMap<ArmorStand, Float> offsetYs = new HashMap<>();
	private static HashMap<ArmorStand, Float> distances = new HashMap<>();
	
	private final String text;
	private final Timing timing;
	private TextDisplay display;
	int tickUntilNext;
	int state;
	int currentChar, maxChar;
	float offsetX, offsetY;
	float distance;
	
	private Message(String text, Timing timing, float offsetX, float offsetY, float distance) {
		this.text = text;
		this.timing = timing;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.distance = distance;
	}

	public static Message of(String text, Timing timing, float offsetX, float offsetY, float distance) {
		if (text.isEmpty()) {
			throw new IllegalArgumentException("Text can't be empty");
		}
		return new Message(text, timing, offsetX, offsetY, distance);
	}
	
	@Override
	public int show(ArmorStand as) {
		Location loc = as.getEyeLocation();
		System.out.println(loc.getDirection());
		Vector dir = getDirection(loc.getYaw() + offsetX, loc.getPitch() + offsetY);
		loc.add(dir.multiply(0.1F * distance));
		System.out.println(loc);
		float yaw = (loc.getYaw() + 180);
		float pitch = -loc.getPitch();
		loc.setYaw(0);//loc.getYaw() + 180);
		loc.setPitch(0);//-loc.getPitch());
		System.out.println(loc);
		display = loc.getWorld().spawn(loc, TextDisplay.class);
		display.setTransformation(new Transformation(new Vector3f(0, 0, 0), yawPitchToQuaternion(yaw, pitch), new Vector3f(0.1f, 0.1f, 0.1f), new Quaternionf(0,0,0,1)));
		display.setInterpolationDelay(-1);
		display.setInterpolationDuration(1);
		display.text(Component.text(text.charAt(0) + genSpaces(text.length() - 1)));
		displays.put(as, display);
		offsetXs.put(as, offsetX);
		offsetYs.put(as, offsetY);
		distances.put(as, distance);
		tickUntilNext = timing.ticksPerChar;
		state = 0;
		currentChar = 0;
		maxChar = text.length();
		return timing.ticksPerChar * (text.length() - 1) * 2 + timing.ticksShown + timing.ticksPerChar;
	}
	
	private String genSpaces(int count) {
		String s = "";
		for (int i = 0; i < count; i++) {
			s += " ";
		}
		return s;
	}

	@Override
	public void tick() {
		if (tickUntilNext != 0) {
			tickUntilNext--;
			return;
		}
		if (state == 0 || state == 2) {
			currentChar ++;
			tickUntilNext = timing.ticksPerChar;
			if (state == 0) {
				display.text(Component.text(text.substring(0, currentChar + 1) + genSpaces(maxChar - 1 - currentChar)));
				if (currentChar + 1 == maxChar) {
					state = 1;
					currentChar = 0;
					tickUntilNext = timing.ticksShown;
				}
			}
			if (state == 2) {
				display.text(Component.text(genSpaces(currentChar + 1) + text.substring(currentChar + 1)));
				if (currentChar + 1 == maxChar) {
					state = 3;
					currentChar = 0;
					tickUntilNext = -1;
				}
			}
		}else if (state == 1) {
			state = 2;
			display.text(Component.text(" " + text.substring(1)));
			tickUntilNext = timing.ticksPerChar;
		}
	}
	
	@Override
	public void end() {
		display.remove();
		display = null;
	}
	
	public static class Timing {
		int ticksPerChar;
		int ticksShown;
		
		public Timing(int ticksPerChar, int ticksShown) {
			super();
			this.ticksPerChar = ticksPerChar;
			this.ticksShown = ticksShown;
		}

		public int getTicksPerChar() {
			return ticksPerChar;
		}

		public void setTicksPerChar(int ticksPerChar) {
			this.ticksPerChar = ticksPerChar;
		}

		public int getTicksShown() {
			return ticksShown;
		}

		public void setTicksShown(int ticksShown) {
			this.ticksShown = ticksShown;
		}
	}
	
	private static Quaternionf yawPitchToQuaternion(double yaw, double pitch) {
        // Convert yaw and pitch to radians
		double yawRad = Math.toRadians(-yaw);
        double pitchRad = Math.toRadians(pitch);

        // Create a new Quaternionf object
        Quaternionf quaternion = new Quaternionf();

        // Set the quaternion using yaw and pitch
        quaternion.rotationYXZ((float)yawRad, (float)pitchRad, 0.0f);

        // Return the quaternion
        return quaternion;
    }

	protected static void onMove(ArmorStand p, Location eyeLoc, boolean instant) {
		TextDisplay display = displays.get(p);
		if (display != null) {
			if (!display.isDead()) {
				System.out.println("Move display");
				Location loc1 = eyeLoc;
				Vector dir = getDirection(loc1.getYaw() + offsetXs.get(p), loc1.getPitch() + offsetYs.get(p));
				
				loc1.add(dir.multiply(0.1F * distances.get(p)));
				System.out.println(loc1);
				float yaw = (loc1.getYaw() + 180);
				float pitch = -loc1.getPitch();
				loc1.setYaw(0);//loc.getYaw() + 180);
				loc1.setPitch(0);//-loc.getPitch());
				display.setTransformation(new Transformation(loc1.subtract(display.getLocation()).toVector().toVector3f(), yawPitchToQuaternion(yaw, pitch), new Vector3f(0.1f, 0.1f, 0.1f), new Quaternionf(0,0,0,1)));
				display.setInterpolationDelay(0);
				display.setInterpolationDuration(instant ? 0 : 2);
				//display.teleport(loc1);
			} else {
				displays.remove(p);
			}
		}
	}
	
	private static Vector getDirection(float yaw, float pitch) {
        Vector vector = new Vector();

        vector.setY(-Math.sin(Math.toRadians(pitch)));

        double xz = Math.cos(Math.toRadians(pitch));

        vector.setX(-xz * Math.sin(Math.toRadians(yaw)));
        vector.setZ(xz * Math.cos(Math.toRadians(yaw)));

        return vector;
    }
}
