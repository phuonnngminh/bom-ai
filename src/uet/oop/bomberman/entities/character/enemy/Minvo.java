package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.graphics.Sprite;

public class Minvo extends Enemy {

    public Minvo(int x, int y, Board board) {
        super(x, y, board, Sprite.minvo_dead, Game.getPlayerSpeed() * 2, 800);

        _sprite = Sprite.minvo_right1;

        _ai = new AIMedium(_board.getPlayer(), this);
        _direction = _ai.calculateDirection();
    }

    /*
     * |--------------------------------------------------------------------------
     * | Mob Sprite
     * |--------------------------------------------------------------------------
     */
    @Override
    protected void chooseSprite() {
        switch (_direction) {
            case 0:
            case 1:
                if (_moving)
                    _sprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3,
                            _animate, 60);
                else
                    _sprite = Sprite.minvo_left1;
                break;
            case 2:
            case 3:
                if (_moving)
                    _sprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, _animate,
                            60);
                else
                    _sprite = Sprite.minvo_left1;
                break;
        }
    }
}
