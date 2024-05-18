package uet.oop.bomberman.entities.character;

import java.util.ArrayList;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard1;

import java.util.Iterator;
import java.util.List;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.level.Coordinates;

public class Bomber extends Character {

    protected Keyboard1 _input1;
    public static List<Item> _items = new ArrayList<Item>();// xu li Item
    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong
     * mỗi lần update()
     */
    private List<Bomb> _bombs1;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs1 = _board.getBombs();
        _input1 = _board.getInput1();
        _sprite = Sprite.player_right;
    }

    @Override
    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện
     * tại của Bomber
     */
    @Override
    protected void detectPlaceBomb() {
        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị
        // _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO: Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời
        // điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị
        // trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs
        // về 0
        if (_input1.space && Game.getBombRate() > 0 && _timeBetweenPutBombs < 0) {

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
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input1 và gọi move() để
        // thực
        // hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        int xa = 0, ya = 0;
        if (_input1.up)
            ya--;
        if (_input1.down)
            ya++;
        if (_input1.left)
            xa--;
        if (_input1.right)
            xa++;

        if (xa != 0 || ya != 0) {
            move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
            _moving = true;
        } else {
            _moving = false;
        }
    }

    @Override
    protected void clearBombs() {
        // TODO Auto-generated method stub
        Iterator<Bomb> bs = _bombs1.iterator();

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
