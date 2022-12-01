/**
 * This reciever handles animation events
 * @author Ethan Rees
 */
package ui;

public interface UIAnimationReciever {
	public void onAnimationTick(String animationName, double time, double percentageComplete);
	public void onAnimationFinish(String animationName);
}
