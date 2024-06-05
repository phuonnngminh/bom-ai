package uet.oop.bomberman.screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Optional;

import javax.imageio.ImageIO;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.gui.GameScreen;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.utils.EGameControl;
import uet.oop.bomberman.utils.EGameLevel;
import uet.oop.bomberman.utils.EGameMode;
import uet.oop.bomberman.utils.EScreenName;
import uet.oop.bomberman.utils.Global;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class WinScreen extends GameScreen {
    ArrayList<String> levels = new ArrayList<String>();
    int selectorIndex = 0;
    private Optional<Keyboard> _input;
    private Board _board;
    private BufferedImage backgroundImage;

    public WinScreen(Board board) {
        _board = board;

        try {
            if (Global.gameMode == EGameMode.TWO_PLAYER) {
                backgroundImage = ImageIO.read(getClass().getResource("/menu/WinScreen2player.png"));
            } else {
                backgroundImage = ImageIO.read(getClass().getResource("/menu/WinScreen1player.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScreen(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, Global.screenWidth, Global.screenHeight, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Global.screenWidth, Global.screenHeight);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void onDestroy() {
        this._input.get().keyboardInputCallback = Optional.ofNullable(null);
    }

    public void setInput() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setInput'");
    }

}
