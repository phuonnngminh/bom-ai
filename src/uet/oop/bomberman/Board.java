package uet.oop.bomberman;

import uet.oop.bomberman.agent.Agent;
import uet.oop.bomberman.base.Copyable;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.base.IGameInfoManager;
import uet.oop.bomberman.base.ILevelManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.action.Action;
import uet.oop.bomberman.entities.character.exceptions.CharacterActionException;
import uet.oop.bomberman.graphics.IRender;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.manager.EntityManager;
import uet.oop.bomberman.manager.GameInfoManager;
import uet.oop.bomberman.manager.LevelManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý thao tác điều khiển, load level, render các màn hình của game
 */
public class Board implements Copyable, IRender {
	protected Game _game;
	protected Screen _screen;

	private List<Agent> agents = new ArrayList<>();

	private IEntityManager entityManager;
	private IGameInfoManager gameInfoManager;
	private ILevelManager levelManager;

	public Board(Game game, Screen screen) {
		_game = game;
		_screen = screen;
		levelManager = new LevelManager(this);
		levelManager.loadGlobalLevel();
	}

	private void snapCameraToPlayer() {
        int xScroll = calculateXOffset(entityManager.getPlayer());
        Screen.setOffset(xScroll, 0);
    }

    private int calculateXOffset(Entity entity) {
    	if(entity == null) return 0;
    	int temp = Screen.xOffset;
    	
    	double x = entity.getX() / 16;
    	double complement = 0.5;
    	int firstBreakpoint = levelManager.getBoardWidth() / 4;
    	int lastBreakpoint = levelManager.getBoardWidth() - firstBreakpoint;
    	
    	if( x > firstBreakpoint + complement && x < lastBreakpoint - complement) {
    		temp = (int)entity.getX()  - (Game.WIDTH / 2);
    	}
    	
    	return temp;
    }

	@Override
	public synchronized void update() {
		if (gameInfoManager.isPaused())
			return;

		entityManager.update();
		gameInfoManager.update();

		processAgentAction();

		snapCameraToPlayer();
	}

	private void clearAgents() {
		agents.clear();
	}

	public void addAgent(Agent agent) {
		agents.add(agent);
	}

	private void processAgentAction() {
		for (Agent agent: agents) {
			List<Action> actions = agent.getNextActions();
			for (Action action: actions) {
				try {
					agent.getCharacter().performAction(action);
				} catch (CharacterActionException ignored) {}
			}
		}
	}

	@Override
	public synchronized void render(Screen screen) {
		if (gameInfoManager.isPaused())
			return;
		entityManager.render(screen);
	}

	public synchronized void init() {
		gameInfoManager = new GameInfoManager(_game);
		entityManager = new EntityManager(
			levelManager.getBoardWidth(),
			levelManager.getBoardHeight(),
			gameInfoManager
		);
		gameInfoManager.setEntityManager(entityManager);
		gameInfoManager.pause();
		_game.setScreenToShow(2);
		_game.resetScreenDelay();
	}

	public void clear() {
		clearAgents();
	}

	public IEntityManager getEntityManager() {
		return entityManager;
	}

	public IGameInfoManager getGameInfoManager() {
		return gameInfoManager;
	}

	public ILevelManager getLevelManager() {
		return levelManager;
	}

	@Override
	public Board copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
