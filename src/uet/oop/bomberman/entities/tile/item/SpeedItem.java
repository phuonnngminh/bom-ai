package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Sprite;

public class SpeedItem extends Item {

	public SpeedItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	protected void handleItemActive() {
		Game.addBomberSpeed(0.5);
	}

	@Override
	protected void handleItemInactive() {
		Game.addBomberSpeed(-0.5);
	}

	@Override
	public String getDisplayActiveItem() {
		return "Speed:";
	}
}
