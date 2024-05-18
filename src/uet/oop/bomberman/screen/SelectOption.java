package uet.oop.bomberman.screen;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.gui.GameScreen;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.sound.Sound;
import uet.oop.bomberman.utils.EGameControl;
import uet.oop.bomberman.utils.EScreenName;
import uet.oop.bomberman.utils.Global;
import java.awt.*;
import java.util.ArrayList;

public class SelectOption extends GameScreen {
    ArrayList<String> options = new ArrayList<String>();
    int selectorIndex = 0;
    private Game game;
    private Keyboard _input;
    public SelectOption(Keyboard input, Game game) {
        _input = input;
        this.game = game;
        options.add("Restart");
        options.add("Back Home");

        _input.keyboardInputCallback = java.util.Optional.of(new Keyboard.KeyboardInputCallback() {
            @Override
            public void onKeyPressed(EGameControl gameControl) {
                switch (gameControl) {
                    case LEFT:
                        selectorIndex--;
                        break;
                    case RIGHT:
                        selectorIndex++;
                        break;
                    case X:
                        if (selectorIndex == 0) {
                            game.restartGame();
                        } else if (selectorIndex == 1) {
                            game.startNewGame();
                        }
                        break;
                }

                if (selectorIndex < 0) {
                    selectorIndex = options.size() - 1;
                } else if (selectorIndex > options.size() - 1) {
                    selectorIndex = 0;
                }
            }
        });
    }

    @Override
    public void drawScreen(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Global.screenWidth, Global.screenHeight);
        drawTitle(g);
        drawPOINTS(g, game.getBoard().getPoints());
        drawOptions(g);
        drawSelector(g);
    }

    private void drawTitle(Graphics g) {
        String title = "GAME OVER";
        Font font = new Font("Arial", Font.BOLD, 20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);

        FontMetrics fm = g.getFontMetrics();
        int x = (Global.screenWidth - fm.stringWidth(title)) / 2;
        int marginTop = 20;
        int y = marginTop + fm.getAscent();

        g.drawString(title, x, y);
    }
    private void drawPOINTS(Graphics g,int points)
    {
       String Point = "POINTS: " + points;
        Font font = new Font("Arial", Font.BOLD, 10 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.yellow);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(Point);
        int x = (Global.screenWidth - textWidth) / 2; // Vị trí x để chuỗi ở giữa màn hình
        int marginTop = 100;
        int y = marginTop + fm.getAscent();

        g.drawString(Point, x, y);

    }
    private void drawOptions(Graphics g) {
        Font font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);

        int w = Global.screenWidth;
        int h = Global.screenHeight;
        FontMetrics fm = g.getFontMetrics();
        int textHeight = fm.getAscent() + fm.getDescent();
        int marginTop = (h - options.size() * textHeight) / 2;

        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            int x = (w - fm.stringWidth(option)) / 2;
            int y = marginTop + fm.getAscent() + textHeight * i;

            g.drawString(option, x, y);
        }
    }

    private void drawSelector(Graphics g) {
        String option = this.options.get(selectorIndex);
        int w = Global.screenWidth;
        int h = Global.screenHeight;
        FontMetrics fm = g.getFontMetrics();
        int textHeight = fm.getAscent() + fm.getDescent();
        int marginTop = (h - options.size() * textHeight) / 2;

        int x = (w - fm.stringWidth(option)) / 2 - 30;
        int y = marginTop + fm.getAscent() + textHeight * selectorIndex;

        g.drawString(">", x, y);
    }

    @Override
    public void update() {
        // No need to update anything in this screen
    }
}
