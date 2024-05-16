package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Sprite;

public class BombItem extends Item {

	public static final int BOMB_LIMIT_BONUS = 1;

    public BombItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	protected void handleItemActive() {
		Game.addBombRate(1);
	}

	@Override
	protected void handleItemInactive() {
		Game.addBombRate(-1);
	}

	@Override
	public String getDisplayActiveItem() {
		return "Bomb:";
	}

}
