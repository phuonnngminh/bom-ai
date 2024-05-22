package uet.oop.bomberman.entities.character;

import java.util.ArrayList;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.action.Action;
import uet.oop.bomberman.entities.character.action.ActionConstants;
import uet.oop.bomberman.entities.character.action.ActionPlaceBomb;
import uet.oop.bomberman.entities.character.exceptions.ActionOnCooldownException;
import uet.oop.bomberman.entities.character.exceptions.BombQuotaReachedException;
import uet.oop.bomberman.entities.character.exceptions.CannotPerformActionException;
import uet.oop.bomberman.entities.character.exceptions.InvalidActionException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Iterator;
import java.util.List;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.sound.Sound;

public class Bomber extends Character {

    private List<Bomb> _bombs;
    public static List<Item> _items = new ArrayList<Item>();//xu li Item

    private final int baseBombLimit;
    protected int bombCooldown = 0;

    private final int baseBombRadius;

    public int getBombCooldown() {
        return bombCooldown;
    }

    private Board _board;

    public Bomber(int x, int y, double baseSpeed, int baseBombLimit, int baseBombRadius, IEntityManager entityManager, Board board) {
        super(x, y, baseSpeed, entityManager);
        this.baseBombLimit = baseBombLimit;
        this.baseBombRadius = baseBombRadius;
        this._board = board;
        _bombs = entityManager.getBombs();
        _sprite = Sprite.player_right;
    }

    @Override
    public void handleUpdate() {
        clearExpiredBombs();
        if (bombCooldown < -7500) bombCooldown = 0;
        else bombCooldown--;
        animate();

    }

    @Override
    public void render(Screen screen) {
        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public int getBombLimit() {
        int countActiveItem = (int) getActiveItems().filter(item -> item instanceof BombItem).count();
        int bombLimitBonus = countActiveItem * BombItem.BOMB_LIMIT_BONUS;
        return this.baseBombLimit + bombLimitBonus;
    }

    public int getBombRemainingQuota() {
        return getBombLimit() - _bombs.size();
    }

    public int getBombRadius() {
        int countActiveItem = (int) getActiveItems().filter(item -> item instanceof FlameItem).count();
        int bombRadiusBonus = countActiveItem * FlameItem.BOMB_RADIUS_BONUS;
        return this.baseBombRadius + bombRadiusBonus;
    }

    public boolean placeBomb() {
        if(getBombRemainingQuota() > 0 && bombCooldown < 0) {
			
			int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
			int yt = Coordinates.pixelToTile( (_y + _sprite.getSize() / 2) - _sprite.getSize() ); //subtract half player height and minus 1 y position
			
			placeBomb(xt,yt);
			
			bombCooldown = 30;
            return true;
		}
        return false;
    }

    public void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb b = new Bomb(x, y, getBombRadius(), entityManager);
        entityManager.addBomb(b);
        Sound.play("BOM_SET");
    }

    private void clearExpiredBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
            }
        }

    }

    @Override
    protected void handleAfterDeath() {
        _board.endGame();
    }

    @Override
    public boolean collide(Entity e) {
        if (!super.collide(e)) return false;
        return true;
    }

    //sprite
    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (isMoving()) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (isMoving()) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (isMoving()) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (isMoving()) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (isMoving()) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }

    @Override
    public int getPoints() {
        return 0;
    }

    private static final List<? extends Action> VALID_ACTIONS = new ArrayList<Action>(){{
        addAll(ActionConstants.LIST_ACTION_MOVE);
        add(ActionConstants.PLACE_BOMB);
    }};
    @Override
	protected List<? extends Action> getValidActions() {
        return VALID_ACTIONS;
	}

    @Override
    protected void performAction(Action action, boolean isDryRun)
            throws InvalidActionException, CannotPerformActionException {
        super.performAction(action, isDryRun);
        if (action instanceof ActionPlaceBomb) {
            if (getBombRemainingQuota() < 0) throw new BombQuotaReachedException();
            if (bombCooldown > 0) throw new ActionOnCooldownException();
            if (!isDryRun) placeBomb();
        }
    }

}
