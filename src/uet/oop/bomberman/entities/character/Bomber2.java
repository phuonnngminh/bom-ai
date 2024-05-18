package uet.oop.bomberman.entities.character;

import java.util.ArrayList;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard1;
import uet.oop.bomberman.input.Keyboard2;

import java.util.Iterator;
import java.util.List;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.sound.Sound;

public class Bomber2 extends Character {

    protected Keyboard2 _input2;
    private List<Bomb> _bombs2;

    public Bomber2(int x, int y, Board board) {
        super(x, y, board);
        _bombs2 = _board.getBombs();
        _input2 = _board.getInput2();
        _sprite = Sprite.player_right;
    }

    @Override
    public void calculateXOffset() {
        // TODO Auto-generated method stub
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    @Override
    protected void calculateMove() {
        // TODO Auto-generated method stub
        int xa = 0, ya = 0;
        if (_input2.up2)
            ya--;
        if (_input2.down2)
            ya++;
        if (_input2.left2)
            xa--;
        if (_input2.right2)
            xa++;

        if (xa != 0 || ya != 0) {
            move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
            _moving = true;
        } else {
            _moving = false;
        }
    }

    @Override
    protected void detectPlaceBomb() {
        if (_input2.space && Game.getBombRate() > 0 && _timeBetweenPutBombs < 0) {

            int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile((_y + _sprite.getSize() / 2) - _sprite.getSize()); // subtract half player
                                                                                                // height and minus 1 y
                                                                                                // position

            placeBomb(xt, yt);
            Game.addBombRate(-1);

            _timeBetweenPutBombs = 30;
        }
    }

    @Override
    protected void clearBombs() {
        // TODO Auto-generated method stub
        Iterator<Bomb> bs = _bombs2.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }
    }
}
