/**
 * @author Ethan Rees
 * This class handles creating and maintaining animations, it does this in a separate thread that is created and destroyed
 */
package ui;

import java.awt.Color;
import java.util.ArrayList;

public class UIAnimator {
	
	public static ArrayList<Animator> activeAnimations = new ArrayList<Animator>();
	public static Thread thread;
	public static boolean threadStop;
	public static final int FPS = 30;
	
	/**
	 * This method will begin the animation and create the thread if needed
	 * @author Ethan Rees
	 * 
	 * @param receiver the animation reciever
	 * @param delaySeconds the seconds to delay the animation
	 * @param secondsDuration the length of the animation in seconds
	 */
	public static void beginAnimation(UIAnimationReciever receiver, String animationName, double delaySeconds, double secondsDuration) {
		activeAnimations.add(new Animator(receiver, animationName, System.currentTimeMillis(), delaySeconds, secondsDuration));
		createThreadIfNeeded();
	}
	
	/**
	 * This method will create the animation thread if it doesn't already exist
	 * Inside the thread itself, it runs at a fixed frame rate and will tick the update function every FPS tick
	 * 
	 * @author Ethan Rees
	 */
	private static void createThreadIfNeeded() {
		if(thread == null) {
			threadStop = false;
			thread = new Thread(()-> {
				// FPS MANAGER
				// this will keep the game running at a steady fps
				// get the last time, the fps tick ratio, and the delta
				long lastTime = System.nanoTime();
				double ratio = 1000000000.0 / (double)FPS;
				double delta = 0;

				// run as fast as humanly possible
				while(!threadStop) {

					// Calculate the delta (how much time has passed between every tick)
					long currentTime = System.nanoTime();
					delta += (currentTime - lastTime) / ratio;
					lastTime = currentTime;

					// if more than a delta tick has passed, then actually update the screen
					// this COULD be an if statement, but using a while lets the scenes catchup incase it lags
					while(delta >= 1 && !threadStop) {
						// calculate the current time
						threadTickUpdate();
						delta--;
						
						// if there are no more animations, close the thread
						if(activeAnimations.size() == 0)
							threadStop = true;
					}
				}
				
				thread = null;
			});
			thread.start();
		}
	}
	
	/**
	 * This method handles each FPS tick of the thread, it iterates through the active animations and will
	 * calculate the relative time and will call each tick
	 * 
	 * @author Ethan Rees
	 * @param timeElapsedMS the time elapsed so far since the start of the thread
	 */
	private static void threadTickUpdate() {
		for(int i = 0; i < activeAnimations.size(); i++) {
			Animator animator = activeAnimations.get(i);
			double timeElapsed = System.currentTimeMillis() - animator.threadStartTimeElapsedMS;
			
			// calculate the time relative to this animation
			double timeMS = timeElapsed - animator.delaySeconds*1000;
			
			// if the delay is done, tick the animation
			if(timeMS >= 0)
				animator.receiver.onAnimationTick(animator.animationName, Math.min(Math.max(timeMS/(double)1000, 0), animator.secondsDuration), Math.min(Math.max(timeMS / (animator.secondsDuration*1000), 0), 1));
			
			// if the animation is done, close it out
			if(timeMS > animator.secondsDuration * 1000) {
				animator.receiver.onAnimationFinish(animator.animationName);
				activeAnimations.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * A nice helper method to lerp smoothly between 2 colors
	 * @author Ethan Rees
	 * 
	 * @param a color A
	 * @param b color B
	 * @param percent % of time to lerp [0-1]
	 * @return The new color
	 */
	public static Color lerpColor(Color a, Color b, double percent) {
		if(percent <= 0)
			return a;
		if(percent >= 1)
			return b;
		return new Color(
				(int)lerp(a.getRed(), b.getRed(), percent),
				(int)lerp(a.getGreen(), b.getGreen(), percent),
				(int)lerp(a.getBlue(), b.getBlue(), percent),
				(int)lerp(a.getAlpha(), b.getAlpha(), percent)
				);
	}
	
	/**
	 * A nice helper method to lerp smoothly between 2 doubles
	 * @author Ethan Rees
	 * 
	 * @param a double A
	 * @param b double B
	 * @param percent % of time to lerp [0-1]
	 * @return the new double
	 */
	public static double lerp(double a, double b, double percent) {
		if(percent < 0)
			return a;
		if(percent > 1)
			return b;

		// use the formula 3x^2 - 2x^3
		double influence = (3*Math.pow(percent, 2) - 2*Math.pow(percent, 3));
		// multiply by bounds
		return influence * (b-a) + a;
	}
}

/**
 * This sub class just handles how the animation data is stored
 * @author Ethan Rees
 */
class Animator {
	UIAnimationReciever receiver;
	String animationName;
	double threadStartTimeElapsedMS;
	double delaySeconds;
	double secondsDuration;
	
	public Animator(UIAnimationReciever receiver, String animationName, double threadStartTimeElapsedMS, double delaySeconds, double secondsDuration) {
		super();
		this.receiver = receiver;
		this.animationName = animationName;
		this.threadStartTimeElapsedMS = threadStartTimeElapsedMS;
		this.delaySeconds = delaySeconds;
		this.secondsDuration = secondsDuration;
	}
}