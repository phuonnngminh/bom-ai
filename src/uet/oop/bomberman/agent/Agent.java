package uet.oop.bomberman.agent;

import uet.oop.bomberman.entities.character.Character;

public abstract class Agent implements IAgent {
    
    private Character character;

    public Agent(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

}
