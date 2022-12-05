/**
 * This class holds the stat box for the leaderboard ui
 */
package ui.leaderboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import _main.WordleGameResult;
import ui.Scene;

public class LeaderboardStatsUI extends Scene {
	
	int[] categories = new int[] {1, 1, 0, 100};
	String[] categoryNames = new String[] {"Played", "Win", "Lost", "Win %"};
	
	int[] statAmounts = new int[] {1, 2, 3, 4, 5, 6};
	double[] statAmountPercent = new double[] {0, 0, 0.4, 0.7, 0.3, 1};
	int selectedBar;
	
	Font largeFont, smallFont;
	WordleGameResult results;
	
	public LeaderboardStatsUI(WordleGameResult results) {
		this.largeFont = new Font("Arial", Font.BOLD, 32);
		this.smallFont = new Font("Arial", Font.BOLD, 12);
		this.results = results;
		this.selectedBar = results.guessAmount-1;
		
		categories[0] = results.gamesPlayed;
		categories[1] = results.gamesWon;
		categories[2] = results.gamesLost;
		categories[3] = (int)(results.winRate*100);
		
		statAmounts = results.getDistroStats();
		statAmountPercent = results.getDistroPercentages(statAmounts);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(contrastColor(getHylightColor(), 3));
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		g2.setStroke(new BasicStroke(6));
		g2.setColor(getHylightColor());
		g2.drawRect(0, 0, getWidth(), getHeight());
		
		int statTileBuffer = 10;
		int tileWidth = 50;
		int tileHeight = 70;
		for(int i = 0; i < categories.length; i++) {
			drawStat(g2, categoryNames[i], categories[i], statTileBuffer*(i+1) + tileWidth*i, statTileBuffer-10, tileWidth, tileHeight);
		}
		
		int offset = 70;
		int areaHeight = getHeight() - 100;
		int barHeight = 15;
		int buffer = (int)Math.round((areaHeight - (statAmounts.length * barHeight)) / (statAmounts.length-1));
		for(int i = 0; i < statAmounts.length; i++) {
			drawDisBar(g2, i, statAmounts[i], statAmountPercent[i], buffer * (i) + barHeight * i + offset, getWidth(), barHeight);
		}
		
		g2.drawString("Secret Word: " + results.word, 10, offset + areaHeight + 18);
	}
	
	/**
	 * This will draw the little stat tile at the top
	 *
	 * @author Ethan Rees 
	 */
	void drawStat(Graphics2D g2, String name, int value, int x, int y, int width, int height) {
		g2.setStroke(new BasicStroke(1));
		
		// draw the number
		Rectangle2D valueSize = getFontMetrics(largeFont).getStringBounds(value + "", g2);
		g2.setFont(largeFont);
		g2.setColor(getTextColor());
		g2.drawString(value + "", centeredWidthXOffset(new Dimension(width, 0), (int)valueSize.getWidth())+(int)x, y + (int)valueSize.getHeight());

		// draw the label
		Rectangle2D labelSize = getFontMetrics(smallFont).getStringBounds(name, g2);
		g2.setFont(smallFont);
		g2.drawString(name, centeredWidthXOffset(new Dimension(width, 0), (int)labelSize.getWidth())+(int)x, y + 40 + (int)labelSize.getHeight());
	}

	/**
	 * This will draw the little bars at the bottom
	 *
	 * @author Ethan Rees 
	 */
	void drawDisBar(Graphics2D g2, int index, int number, double percent, int y, int width, int height) {
		Color c = contrastColor(getHylightColor(), 1);
		if(selectedBar == index)
			c = contrastColor(getRightPlaceColor(), 2);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(c);
		g2.fillRect(20, y, (int)((width-55)*percent) + 20, height);
		g2.setColor(contrastColor(g2.getColor(), 1));
		g2.drawRect(20, y, (int)((width-55)*percent) + 20, height);
		
		// draw index
		g2.setColor(getTextColor());
		g2.drawString((index+1) + "", 9, y+(int)(height*0.5)+4);

		// draw number
		g2.setColor(getTextColor());
		g2.drawString(number + "", (int)((width-55)*percent) + 26, y+(int)(height*0.5)+4);
		
	}
	
	// this wont work lol
	@Override
	public void onThemeChange(boolean isDarkMode) {}
}
