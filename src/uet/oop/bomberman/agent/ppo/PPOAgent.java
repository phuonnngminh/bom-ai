package uet.oop.bomberman.agent.ppo;

import java.util.ArrayList;
import java.util.List;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.agent.Agent;
import uet.oop.bomberman.agent.rl.PPO;
import uet.oop.bomberman.agent.state.base.IStateExtractor;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.action.Action;

public class PPOAgent extends Agent {

    private IStateExtractor stateExtractor;
    private Board board;
    private PPO ppo;
    private List<Action> validActions;

    private boolean isFirstAction = true;
    private float prevValue;

    public PPOAgent(Character character, Board board, IStateExtractor stateExtractor) {
        super(character);
        this.board = board;
        this.prevValue = stateExtractor.getValue(board);
        this.stateExtractor = stateExtractor;
        this.validActions = character.getValidActions();
        this.ppo = new PPO(
            stateExtractor.getDimension(),
            validActions.size(),
            64,
            0.99f,
            0.95f,
            0.001f,
            16,
            8,
            0.2f
        );
    }

    @Override
    public List<Action> getNextActions() {
        if (!isFirstAction) collectReward();
        Action action = getAction();
        List<Action> actions = new ArrayList<>();
        actions.add(action);
        prevValue = stateExtractor.getValue(board);
        if (isFirstAction) isFirstAction = false;
        return actions;
    }

    private void collectReward() {
        float currentValue = stateExtractor.getValue(board);
        ppo.collect(currentValue - prevValue, false);
    }

    private Action getAction() {
        float[] state = stateExtractor.getEmbedding(board);
        int actionIndex = ppo.react(state);
        Action action = validActions.get(actionIndex);
        return action;
    }

}
