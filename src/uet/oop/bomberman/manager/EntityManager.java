package uet.oop.bomberman.manager;

import uet.oop.bomberman.base.IBombManager;
import uet.oop.bomberman.base.ICharacterManager;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.base.IGameInfoManager;
import uet.oop.bomberman.base.ITileManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.level.LevelLoader;

public class EntityManager implements IEntityManager {

    private ITileManager tileManager;
    private ICharacterManager characterManager;
    private IBombManager bombManager;

    private LevelLoader levelLoader;

    public EntityManager(LevelLoader levelLoader, IGameInfoManager gameInfoManager) {
        this.levelLoader = levelLoader;
        this.tileManager = new TileManager(levelLoader.getWidth(), levelLoader.getHeight());
        this.characterManager = new CharacterManager(gameInfoManager);
        this.bombManager = new BombManager();
    }


	@Override
	public Entity getEntityAtExcluding(double x, double y, Character m) {

		Entity res = null;

		if (x < 0) return null;
		if (y < 0) return null;
		if (x >= levelLoader.getWidth()) return null;
		if (y >= levelLoader.getHeight()) return null;

		res = bombManager.getFlameSegmentAt((int) x, (int) y);
		if (res != null)
			return res;

		res = bombManager.getBombAt(x, y);
		if (res != null)
			return res;

		res = characterManager.getCharacterAtExcluding((int) x, (int) y, m);
		if (res != null)
			return res;

		res = tileManager.getTileAt((int) x, (int) y);

		return res;
	}

	@Override
	public boolean isEnemyCleared() {
		return !characterManager.getCharacters().stream()
			.anyMatch(character -> character != characterManager.getPlayer());
	}


    @Override
    public void update() {
        tileManager.update();
        characterManager.update();
        bombManager.update();
    }


    @Override
    public void render(Screen screen) {
        tileManager.render(screen);
        characterManager.render(screen);
        bombManager.render(screen);
    }

    @Override
    public Character getPlayer() {
        return characterManager.getPlayer();
    }


    @Override
    public ITileManager getTileManager() {
        return tileManager;
    }


    @Override
    public ICharacterManager getCharacterManager() {
        return characterManager;
    }


    @Override
    public IBombManager getBombManager() {
        return bombManager;
    }
    
}
