package uet.oop.bomberman.base;

import uet.oop.bomberman.entities.character.Character;

public interface ICharacterManager {

    Character getCharacterAtExcluding(int x, int y, Character a);

    void addCharacter(Character e);

    void setPlayer(Character character);

    Character getPlayer();

    void handleOnDeath(Character character, Character killer);

}