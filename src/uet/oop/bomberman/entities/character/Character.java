package uet.oop.bomberman.entities.character;

import java.util.Iterator;
import java.util.List;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

/**
 * Bao gồm Bomber và Enemy
 */
public abstract class Character extends AnimatedEntitiy {

	protected Board _board;
	protected int _direction = -1;
	protected boolean _alive = true;
	protected boolean _moving = false;
	public int _timeAfter = 40;
	protected int _timeBetweenPutBombs = 0;
	// private List<Bomb> _bombs;

	public Character(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
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

	protected abstract void clearBombs();

	@Override
	public void render(Screen screen) {
		calculateXOffset();

		if (_alive)
			chooseSprite();
		else
			_sprite = Sprite.player_dead1;

		screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
	};

	public abstract void calculateXOffset();

	/**
	 * Tính toán hướng đi
	 */
	protected abstract void calculateMove();

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

	/**
	 * Được gọi khi đối tượng bị tiêu diệt
	 */
	public void kill() {
		if (!_alive)
			return;
		_alive = false;
		Sound.play("endgame3");
	}

	/**
	 * Xử lý hiệu ứng bị tiêu diệt
	 */
	protected void afterKill() {
		if (_timeAfter > 0)
			--_timeAfter;
		else {
			_board.endGame();
		}
	}

	protected abstract void detectPlaceBomb();

	protected void placeBomb(int x, int y) {
		// TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
		Bomb b = new Bomb(x, y, _board);
		_board.addBomb(b);
		Sound.play("BOM_SET");
	}

	/**
	 * Kiểm tra xem đối tượng có di chuyển tới vị trí đã tính toán hay không
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
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
		// return false;
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

	protected double getXMessage() {
		return (_x * Game.SCALE) + (_sprite.SIZE / 2 * Game.SCALE);
	}

	protected double getYMessage() {
		return (_y * Game.SCALE) - (_sprite.SIZE / 2 * Game.SCALE);
	}

	// sprite
	private void chooseSprite() {
		switch (_direction) {
			case 0:
				_sprite = Sprite.player_up;
				if (_moving) {
					_sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
				}
				break;
			case 1:
				_sprite = Sprite.player_right;
				if (_moving) {
					_sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
				}
				break;
			case 2:
				_sprite = Sprite.player_down;
				if (_moving) {
					_sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
				}
				break;
			case 3:
				_sprite = Sprite.player_left;
				if (_moving) {
					_sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
				}
				break;
			default:
				_sprite = Sprite.player_right;
				if (_moving) {
					_sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
				}
				break;
		}
	}

}
