package uet.oop.bomberman.screen;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.gui.GameScreen;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.utils.EGameLevel;
import uet.oop.bomberman.utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class SelectLevelScreen extends GameScreen {
    ArrayList<String> levels = new ArrayList<String>();
    int selectorIndex = 0;
    private Keyboard _input;

    public SelectLevelScreen(Keyboard input) {
        _input = input;

        levels.add(EGameLevel.EASY.getStringLevel());
        levels.add(EGameLevel.MEDIUM.getStringLevel());
        levels.add(EGameLevel.HARD.getStringLevel());
    }

    @Override
    public void drawScreen(Graphics g) {
        // set background
        g.setColor(Color.black);
        g.fillRect(0, 0, Global.screenWidth, Global.screenHeight);

        drawTitle(g);
        drawOptions(g);
        drawSelector(g);
    }

    private void drawTitle(Graphics g) {
        String title = "SELECT LEVEL";
        Font font = new Font("Arial", Font.BOLD, 20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);

        FontMetrics fm = g.getFontMetrics();
        int x = (Global.screenWidth - fm.stringWidth(title)) / 2;
        int marginTop = 20;
        int y = marginTop + fm.getAscent();

        g.drawString(title, x, y);
    }

    private void drawOptions(Graphics g) {
        Font font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);

        int w = Global.screenWidth;
        int h = Global.screenHeight;
        FontMetrics fm = g.getFontMetrics();
        int textHeight = fm.getAscent() + fm.getDescent();
        int boxHeight = textHeight * this.levels.size();
        int marginTop = (h - boxHeight) / 2;

        for (int i=0; i < this.levels.size(); i++) {
            String level = this.levels.get(i);
            int x = (w - fm.stringWidth(level)) / 2;
            int y = marginTop + fm.getAscent() + textHeight*i;

            g.drawString(level, x, y);
        }
    }

    private void drawSelector(Graphics g) {
        String level = this.levels.get(selectorIndex);
        int w = Global.screenWidth;
        int h = Global.screenHeight;
        FontMetrics fm = g.getFontMetrics();
        int textHeight = fm.getAscent() + fm.getDescent();
        int boxHeight = textHeight * this.levels.size();
        int marginTop = (h - boxHeight) / 2;

        int x = (w - fm.stringWidth(level)) / 2 - 30;
        int y = marginTop + fm.getAscent() + textHeight*selectorIndex;

        g.drawString(">", x, y);
    }

    @Override
    public void update() {
        if (_input.getSingleUp()) {
            System.out.print("vao day up");
            selectorIndex++;
        } else if (_input.getSingleDown()) {
            selectorIndex--;
            System.out.print("vao day down");
        }

        if (selectorIndex < 0) {
            selectorIndex = levels.size() - 1;
        } else if (selectorIndex > levels.size() - 1) {
            selectorIndex = 0;
        }
    }
}
