package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard2;

import java.util.Iterator;
import java.util.List;
import uet.oop.bomberman.level.Coordinates;

public class Bomber2 extends Player {

    protected Keyboard2 _input2;

    private List<Bomb> _bombs2;

    public Bomber2(int x, int y, Board board) {
        super(x, y, board);
        _bombs2 = _board.getBombs();
        _input2 = _board.getInput2();
        _sprite = Sprite.player_right;
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    };

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
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input2 và gọi move() để
        // thực
        // hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        int xa = 0, ya = 0;
        if (_input2.up)
            ya--;
        if (_input2.down)
            ya++;
        if (_input2.left)
            xa--;
        if (_input2.right)
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

    // sprite
    @Override
    public void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player2_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_up_1, Sprite.player2_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player2_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_right_1, Sprite.player2_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player2_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_down_1, Sprite.player2_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player2_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_left_1, Sprite.player2_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player2_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_right_1, Sprite.player2_right_2, _animate, 20);
                }
                break;
        }
    }

}
