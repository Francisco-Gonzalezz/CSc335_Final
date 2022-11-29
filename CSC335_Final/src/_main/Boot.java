package _main;
import ui.SceneManager;
import ui.titleScreen.TitleScreenUI;

public class Boot {
	public static void main(String[] args) {
		SceneManager.setScene(new TitleScreenUI());
	}
}