package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Sprite;

public class FlameItem extends Item {

	public static final int BOMB_RADIUS_BONUS = 1;

	public FlameItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	protected void handleItemActive() {
		Game.addBombRadius(1);
	}

	@Override
	protected void handleItemInactive() {
		Game.addBombRadius(-1);
	}

	@Override
	public String getDisplayActiveItem() {
		return "Flame:";
	}
}
