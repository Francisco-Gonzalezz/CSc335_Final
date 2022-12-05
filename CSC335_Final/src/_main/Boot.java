package _main;
import db.DBAdaptor;
import player.Player;
import ui.SceneManager;
import ui.leaderboard.LeaderboardUI;
import ui.titleScreen.TitleScreenUI;

public class Boot {
	public static void main(String[] args) {
		//SceneManager.setScene(new TitleScreenUI());

		WordleGameResult result = new WordleGameResult();
		result.isGuest = true;
		result.word = "THESE";
		SceneManager.setScene(new LeaderboardUI(SceneManager.size, result));
	}
}