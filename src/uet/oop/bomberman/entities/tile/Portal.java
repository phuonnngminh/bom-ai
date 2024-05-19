package uet.oop.bomberman.entities.tile;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

public class Portal extends Tile {
	protected Board _board;

	public Portal(int x, int y, Board board, Sprite sprite) {
		super(x, y, sprite);
		_board = board;
	}

	@Override
	public boolean collide(Entity e) {// xu li khi 2 entity va cham
										// true cho di qua
										// false khong cho di qua
		// TODO: xử lý khi Bomber đi vào
		if (e instanceof Character && ((Character)e).isPlayer()) {

			if (!_board.isEnemyCleared())
				return false;

			if (e.getXTile() == getX() && e.getYTile() == getY()) {
				if (_board.isEnemyCleared()) {
					_board.nextLevel();
					Sound.play("CRYST_UP");
				}
			}

		}

		return true;
	}

}
