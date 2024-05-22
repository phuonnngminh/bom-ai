package uet.oop.bomberman.agent;

import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.action.Action;
import uet.oop.bomberman.entities.character.action.ActionConstants;
import uet.oop.bomberman.entities.character.enemy.ai.AI;

public class MovingAgent extends Agent {
    
    private AI ai;

    public MovingAgent(Character character, AI ai) {
        super(character);
        this.ai = ai;
    }

    @Override
    public Action getNextAction() {
        int direction = ai.calculateDirection();
        switch (direction) {
            case 0:
                return ActionConstants.MOVE_UP;
            case 1:
                return ActionConstants.MOVE_DOWN;
            case 2:
                return ActionConstants.MOVE_LEFT;
            case 3:
                return ActionConstants.MOVE_RIGHT;
            default:
                return ActionConstants.DO_NOTHING;
        }
    }
    
}
