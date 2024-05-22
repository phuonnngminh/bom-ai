package uet.oop.bomberman.base;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.IRender;

public interface IEntityManager extends IRender {

    public Entity getEntityAtExcluding(double x, double y, Character m);
    public default Entity getEntityAt(double x, double y) {
        return getEntityAtExcluding(x, y, null);
    };
    
    public boolean isEnemyCleared();
    public Character getPlayer();

    public ITileManager getTileManager();
    public ICharacterManager getCharacterManager();
    public IBombManager getBombManager();

}