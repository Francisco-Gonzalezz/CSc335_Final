package ui.help;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;

import ui.Scene;

public class HelpPageUI extends Scene {
	JLabel title;
	Font titleFont, contentFont;
	
	JLabel content;
	
	public HelpPageUI(Dimension size) {
		setLayout(null);
		createExitButton();
		
		// create the title
		titleFont = new Font("Arial", Font.BOLD, 32);
		String text = " - Help Page -";
		int titleWidth = getFontMetrics(titleFont).stringWidth(text)+10;
		title = new JLabel(text);
		title.setBounds(centeredWidthXOffset(size, titleWidth), 10, titleWidth, 35);
		title.setFont(titleFont);
		add(title);
		
		// create the content
		contentFont = new Font("Arial", Font.PLAIN, 14);
		String contents = "";
		try {
			Scanner file = new Scanner(new File("./helpPage.html"));
			while(file.hasNextLine())
				contents += file.nextLine();
			file.close();
		} catch (FileNotFoundException e) {
			contents = "File could not be loaded for some reason. Expected helpPage.html in the project folder!";
		}
		content = new JLabel(contents);
		content.setFont(contentFont);
		content.setBounds(30, 70, size.width-75, size.height-30-70-60);
		add(content);
	}

	@Override
	public void onThemeChange(boolean isDarkMode) {
		setBackground(getBackgroundColor());
		title.setForeground(getTextColor());
		content.setForeground(getTextColor());
	}
}
