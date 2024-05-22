package uet.oop.bomberman.base;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;

public interface IEntityManager extends IBombManager, ITileManager, ICharacterManager {

    public Entity getEntityAtExcluding(double x, double y, Character m);
    public default Entity getEntityAt(double x, double y) {
        return getEntityAtExcluding(x, y, null);
    };
    
    public boolean isEnemyCleared();

}