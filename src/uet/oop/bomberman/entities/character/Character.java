package uet.oop.bomberman.entities.character;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.action.Action;
import uet.oop.bomberman.entities.character.action.ActionConstants;
import uet.oop.bomberman.entities.character.action.ActionMove;
import uet.oop.bomberman.entities.character.exceptions.CannotPerformActionException;
import uet.oop.bomberman.entities.character.exceptions.CharacterActionException;
import uet.oop.bomberman.entities.character.exceptions.InvalidActionException;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.level.Coordinates;

/**
 * Bao gồm Bomber và Enemy
 */
public abstract class Character extends AnimatedEntitiy {
	
	protected final IEntityManager entityManager;
	protected int _direction = -1;
	protected boolean _alive = true;
	protected boolean _moving = false;
	protected int timerDeathAnimation = 40;

	private final double baseSpeed;

	private List<Item> activeItems = new ArrayList<>();
	
	public Character(int x, int y, double baseSpeed, IEntityManager entityManager) {
		_x = x;
		_y = y;
		this.entityManager = entityManager;
		this.baseSpeed = baseSpeed;
	}
	
	@Override
	public final void update() {
        if (!_alive) {
            if (timerDeathAnimation > 0) {
                timerDeathAnimation -= 1;
            } else {
                handleAfterDeath();
            }
            return;
        }
		handleUpdate();
	};

	protected abstract void handleUpdate();
	
	@Override
	public abstract void render(Screen screen);

	/**
	 * Tính toán hướng đi
	 */
	protected abstract void calculateMove();

	protected List<? extends Action> getValidActions() { return ActionConstants.LIST_ACTION_MOVE; }

	public boolean isValidAction(Action action) {
		return getValidActions().contains(action);
	}

	public final void performAction(Action action) throws InvalidActionException, CannotPerformActionException {
		performAction(action, false);
	}

	protected void performAction(Action action, boolean isDryRun) throws InvalidActionException, CannotPerformActionException {
		if (!isValidAction(action)) throw new InvalidActionException();
		if (action instanceof ActionMove) {
			ActionMove actionMove = (ActionMove) action;
			double dx = actionMove.getDx() * getSpeed();
			double dy = actionMove.getDy() * getSpeed();
			if (!isDryRun) move(dx, dy);
		}
	};

	public final boolean canPerformAction(Action action) {
		try {
			performAction(action, true);
			return true;
		} catch (CharacterActionException ex) {
			return false;
		}
	};

	public List<? extends Action> getPerformableActions() {
		return getValidActions().stream()
			.filter(this::canPerformAction)
			.collect(Collectors.toList());
	}

	/** Check if can be moved with vector (xa, ya).
	 * @param xa
	 * @param ya
	 */
	public void move(double xa, double ya) {
        if(xa > 0) _direction = 1;
		if(xa < 0) _direction = 3;
		if(ya > 0) _direction = 2;
		if(ya < 0) _direction = 0;
		
		if(canMove(0, ya)) _y += ya;
		if(canMove(xa, 0)) _x += xa;

        Entity collidingEntity = entityManager.getEntity(
            Coordinates.pixelToTile(getCenterX()),
            Coordinates.pixelToTile(getCenterY()),
            this
        );
        if (collidingEntity != null) {
            this.collide(collidingEntity);
            collidingEntity.collide(this);
        }
	}

	/**
	 * Được gọi khi đối tượng bị tiêu diệt
	 */
	public final void handleOnDeath() {
		if(!_alive) return;
		_alive = false;
		// TODO: determine killer
		entityManager.handleOnDeath(this, null);
	}

	/**
	 * Xử lý hiệu ứng bị tiêu diệt
	 */
	protected abstract void handleAfterDeath();

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

	public abstract int getPoints();

	@Override
	public boolean collide(Entity e) {
		if(e instanceof Flame){
			this.handleOnDeath();
			return false;
		}
		if (e instanceof Character) {
			Character other = (Character) e;
			if (this.isPlayer() && !other.isPlayer()) {
				this.handleOnDeath();
				return false;
			}
			if (other.isPlayer() && !this.isPlayer()) {
				other.handleOnDeath();
				return false;
			}
		}
		if( e instanceof LayeredEntity) return(e.collide(this));
		return true;
	}

	@Override
	public boolean canBePassedThroughBy(Entity other) {
		return true;
	}

}
