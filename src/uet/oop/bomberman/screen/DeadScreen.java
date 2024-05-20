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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DeadScreen extends GameScreen {
    ArrayList<String> options = new ArrayList<>();
    int selectorIndex = 0;
    private Game game;
    private Keyboard _input;
    private BufferedImage restartIcon;
    private BufferedImage homeIcon;

    public DeadScreen(Keyboard input, Game game) {
        _input = input;
        this.game = game;
        options.add("Restart");
        options.add("Back Home");
        try {
            restartIcon = ImageIO.read(getClass().getResource("/menu/icons8-restart-50.png"));
            homeIcon = ImageIO.read(getClass().getResource("/menu/icons8-menu-50.png"));
            restartIcon = colorizeIcon(restartIcon, Color.YELLOW);
            homeIcon = colorizeIcon(homeIcon, Color.YELLOW);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    case ENTER:
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
    
    // Rest of the class remains unchanged
    private BufferedImage colorizeIcon(BufferedImage icon, Color color) {
        BufferedImage newIcon = new BufferedImage(icon.getWidth(), icon.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < icon.getWidth(); x++) {
            for (int y = 0; y < icon.getHeight(); y++) {
                int argb = icon.getRGB(x, y);
                if ((argb >> 24) != 0x00) { // Check if pixel is not transparent
                    newIcon.setRGB(x, y, color.getRGB());
                } else {
                    newIcon.setRGB(x, y, argb);
                }
            }
        }
        return newIcon;
    }

    @Override
    public void drawScreen(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Global.screenWidth, Global.screenHeight);
        drawTitle(g);
        drawPOINTS(g, game.getBoard().getPoints());
        drawTIMES(g, game.getBoard().getTime());
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
        int marginTop = 100;
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
        int marginTop = 190;
        int y = marginTop + fm.getAscent();

        g.drawString(Point, x, y);

    }
    private void drawTIMES(Graphics g,int times)
    {
       String Point = "TIME : " + times;
        Font font = new Font("Arial", Font.BOLD, 10 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.yellow);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(Point);
        int x = (Global.screenWidth - textWidth) / 2; // Vị trí x để chuỗi ở giữa màn hình
        int marginTop = 230;
        int y = marginTop + fm.getAscent();

        g.drawString(Point, x, y);

    }
    private void drawOptions(Graphics g) {
        int w = Global.screenWidth;
        int h = Global.screenHeight;
        int iconHeight = restartIcon.getHeight();
        int marginTop = (h - iconHeight) / 2; 
    
        int spacing = 100; 
        int totalOptionsWidth = restartIcon.getWidth() + spacing + homeIcon.getWidth();
    
        int startX = (w - totalOptionsWidth) / 2;
    
        g.drawImage(restartIcon, startX, marginTop, null);
    
        int homeIconX = startX + restartIcon.getWidth() + spacing;
        g.drawImage(homeIcon, homeIconX, marginTop, null);
    }
    
    private void drawSelector(Graphics g) {
        int w = Global.screenWidth;
        int h = Global.screenHeight;
        int iconHeight = restartIcon.getHeight();
        int marginTop = (h - iconHeight) / 2; 
    
        int spacing = 90; 
        int totalOptionsWidth = restartIcon.getWidth() + spacing + homeIcon.getWidth();
    
        int startX = (w - totalOptionsWidth) / 2;
    
        int selectorX = selectorIndex == 0 ? startX - 40 : startX + restartIcon.getWidth() + spacing - 40;
        int y = marginTop + (iconHeight / 2)  + 10;
    
        g.drawString(">", selectorX, y);
    }
    

    @Override
    public void update() {
    }
}
