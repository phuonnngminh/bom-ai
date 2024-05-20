package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.sound.Sound;

public abstract class Player extends Character {
    public Player(int x, int y, Board board) {
        super(x, y, board);
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500)
            _timeBetweenPutBombs = 0;
        else
            _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    protected void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính
        // toán hay không và thực hiện thay đổi tọa độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
        if (xa > 0)
            _direction = 1;
        if (xa < 0)
            _direction = 3;
        if (ya > 0)
            _direction = 2;
        if (ya < 0)
            _direction = 0;

        if (canMove(0, ya)) { // separate the moves for the player can slide when is colliding
            _y += ya;
        }

        if (canMove(xa, 0)) {
            _x += xa;
        }
    }

    public void kill() {
        if (!_alive)
            return;
        _alive = false;
        Sound.play("endgame3");
    }

    protected void afterKill() {
        if (_timeAfter > 0)
            --_timeAfter;
        else {
            _board.endGame();
        }
    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb b = new Bomb(x, y, _board);
        _board.addBomb(b);
        Sound.play("BOM_SET");
    }

    protected boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di
        // chuyển tới đó hay không
        for (int c = 0; c < 4; c++) { // colision detection for each corner of the player
            double xt = ((_x + x) + c % 2 * 9) / Game.TILES_SIZE; // divide with tiles size to pass to tile coordinate
            double yt = ((_y + y) + c / 2 * 10 - 13) / Game.TILES_SIZE; // these values are the best from multiple tests

            Entity a = _board.getEntity(xt, yt, this);

            if (!a.collide(this))
                return false;
        }
        return true;

    };

    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
        if (e instanceof Flame) {
            this.kill();
            return false;
        }
        if (e instanceof Enemy) {
            this.kill();
            return true;
        }
        if (e instanceof LayeredEntity)
            return (e.collide(this));
        return true;
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    @Override
    protected void clearBombs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearBombs'");
    }

    @Override
    protected void calculateMove() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateMove'");
    }

    @Override
    protected void detectPlaceBomb() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'detectPlaceBomb'");
    }

    @Override
    public void render(Screen screen) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }

    public abstract void chooseSprite();
}
