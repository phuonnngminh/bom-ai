package uet.oop.bomberman.entities.character.enemy;

import static java.lang.Math.random;
import java.util.Random;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.graphics.Sprite;

public class Balloon extends Enemy {

	public Balloon(int x, int y, Board board) {
		super(x, y, board, Sprite.balloom_dead, 0.5, 100);

		_sprite = Sprite.balloom_left1;

		_ai = new AILow();
		_direction = _ai.calculateDirection();

	}

	@Override
	protected void chooseSprite() {
		switch (_direction) {
			case 0:
			case 1:
				_sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3,
						_animate, 60);
				break;
			case 2:
			case 3:
				_sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3,
						_animate, 60);
				break;
		}
	}

	@Override
	protected void clearBombs() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'clearBombs'");
	}

	@Override
	public void calculateXOffset() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calculateXOffset'");
	}

	@Override
	protected void detectPlaceBomb() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'detectPlaceBomb'");
	}
}
