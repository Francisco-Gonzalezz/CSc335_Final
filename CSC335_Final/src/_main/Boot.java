package _main;
import java.awt.Dimension;

import javax.swing.JFrame;

import ui.SceneManager;
import ui.UIAnimator;
import ui.titleScreen.TitleScreenUI;
import ui.wordleGameBoard.WordleGameBoardUI;

public class Boot {
	public static void main(String[] args) {
		SceneManager.setScene(new TitleScreenUI());
	}
}