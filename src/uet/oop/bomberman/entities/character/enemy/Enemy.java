package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.action.ActionConstants;
import uet.oop.bomberman.entities.character.enemy.ai.AI;
import uet.oop.bomberman.entities.character.exceptions.CharacterActionException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Enemy extends Character {

	protected int _points;
	
	protected double _speed;
	protected AI _ai;

	protected final double MAX_STEPS;
	protected final double rest;
	protected double _steps;
	
	protected int _finalAnimation = 30;
	protected Sprite _deadSprite;

	public Enemy(int x, int y, IEntityManager entityManager, Sprite dead, double speed, int points) {
		super(x, y, speed, entityManager);

		_points = points;
		_speed = speed;
		
		MAX_STEPS = Game.TILES_SIZE / _speed;
		rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
		_steps = MAX_STEPS;
		
		timerDeathAnimation = 20;
		_deadSprite = dead;
	}
	
	@Override
	public void handleUpdate() {
		animate();
		calculateMove();
	}
	
	@Override
	public void render(Screen screen) {
		
		if(_alive)
			chooseSprite();
		else {
			if(timerDeathAnimation > 0) {
				_sprite = _deadSprite;
				_animate = 0;
			} else {
				_sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, _animate, Game.TICKS_PER_SECOND);
			}
				
		}
			
		screen.renderEntity((int)_x, (int)_y - _sprite.SIZE, this);
	}
	
	@Override
	public void calculateMove() {
		int direction = _ai.calculateDirection();
		try {
			switch (direction) {
				case 0:
					performAction(ActionConstants.MOVE_UP);
					break;
				case 1:
					performAction(ActionConstants.MOVE_DOWN);
					break;
				case 2:
					performAction(ActionConstants.MOVE_LEFT);
					break;
				case 3:
					performAction(ActionConstants.MOVE_RIGHT);
					break;
				default:
			}
		} catch (CharacterActionException ignored) {}
		
	}

	@Override
	public boolean collide(Entity e) {
		if (!super.collide(e)) return false;
		return true;
	}
	
	@Override
	protected void handleAfterDeath() {
		remove();
	}
	
	protected abstract void chooseSprite();

	@Override
	public int getPoints() {
		return _points;
	}
}
