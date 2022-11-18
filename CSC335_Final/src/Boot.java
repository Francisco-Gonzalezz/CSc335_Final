import java.awt.Dimension;

import javax.swing.JFrame;

import ui.WordleGameBoardUI;

public class Boot {

	public static void main(String[] args) {
		
		Dimension size = new Dimension(800, 712);
		JFrame frame = new JFrame();
		frame.setTitle("Wordle - Csc335");
		
		frame.add(new WordleGameBoardUI(size));
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(size);
		frame.setResizable(false);
		frame.pack();
	}
}