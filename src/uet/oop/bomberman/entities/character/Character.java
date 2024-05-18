package uet.oop.bomberman.entities.character;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.graphics.Screen;

/**
 * Bao gồm Bomber và Enemy
 */
public abstract class Character extends AnimatedEntitiy {
	
	protected IEntityManager entityManager;
	protected int _direction = -1;
	protected boolean _alive = true;
	protected boolean _moving = false;
	public int _timeAfter = 40;

	private final double baseSpeed;

	private List<Item> activeItems = new ArrayList<>();
	
	public Character(int x, int y, double baseSpeed, IEntityManager entityManager) {
		_x = x;
		_y = y;
		this.entityManager = entityManager;
		this.baseSpeed = baseSpeed;
	}
	
	@Override
	public abstract void update();
	
	@Override
	public abstract void render(Screen screen);

	/**
	 * Tính toán hướng đi
	 */
	protected abstract void calculateMove();
	
	public abstract void move(double xa, double ya);

	/**
	 * Được gọi khi đối tượng bị tiêu diệt
	 */
	public abstract void kill();

	/**
	 * Xử lý hiệu ứng bị tiêu diệt
	 */
	protected abstract void afterKill();

	/**
	 * Kiểm tra xem đối tượng có di chuyển tới vị trí đã tính toán hay không
	 * @param x
	 * @param y
	 * @return
	 */
	protected abstract boolean canMove(double x, double y);

	protected double getXMessage() {
		return (_x * Game.SCALE) + (_sprite.SIZE / 2 * Game.SCALE);
	}
	
	protected double getYMessage() {
		return (_y* Game.SCALE) - (_sprite.SIZE / 2 * Game.SCALE);
	}

	public boolean isPlayer() {
		return entityManager.getPlayer() == this;
	}
	
	public boolean isAlive() {
		return _alive;
	}

	public void setMoving(boolean moving) {
		this._moving = moving;
	}

	public Stream<Item> getActiveItems() {
		return activeItems.stream().filter(Item::isActive);
	}

	public void addActiveItem(Item item) {
		this.activeItems.add(item);
		entityManager.addActiveItem(item);
	}

	public double getSpeed() {
		double speedMultiplier = 1;
		for (Item item: activeItems) {
			if (!item.isActive()) continue;
			if (item instanceof SpeedItem) {
				speedMultiplier += SpeedItem.SPEED_MULTIPLIER;
			}
		}
		return speedMultiplier * this.baseSpeed;
	}

}
