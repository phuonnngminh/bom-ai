package uet.oop.bomberman;

import uet.oop.bomberman.agent.Agent;
import uet.oop.bomberman.base.Copyable;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.base.IGameInfoManager;
import uet.oop.bomberman.entities.character.action.Action;
import uet.oop.bomberman.entities.character.exceptions.CharacterActionException;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.IRender;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.level.FileLevelLoader;
import uet.oop.bomberman.level.LevelLoader;
import uet.oop.bomberman.manager.EntityManager;
import uet.oop.bomberman.manager.GameInfoManager;
import uet.oop.bomberman.utils.Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý thao tác điều khiển, load level, render các màn hình của game
 */
public class Board implements Copyable, IRender {
	protected LevelLoader _levelLoader;
	protected Game _game;
	protected Screen _screen;

	private List<Agent> agents = new ArrayList<>();

<<<<<<< HEAD
	private EntityManager entityManager;

	@Override
	public List<Item> getPlayerActiveItems() {
		Character player = entityManager.getPlayer();
		if (!(player instanceof CanUseItem))
			return new ArrayList<>();
		return ((CanUseItem) player).getActiveItems().collect(Collectors.toList());
	}
=======
	private IEntityManager entityManager;
	private IGameInfoManager gameInfoManager;
>>>>>>> ab179389b9c2c4a18043972ca63a3276b071544d

	private int _screenToShow = -1; // 1:endgame, 2:changelevel, 3:paused

	public Board(Game game, Screen screen) {
		_game = game;
		_screen = screen;

		loadLevel(Global.gameLevel); // start in level 1
	}

	private void snapCameraToPlayer() {
		int xScroll = Screen.calculateXOffset(this, entityManager.getPlayer());
		Screen.setOffset(xScroll, 0);
	}

	@Override
	public void update() {
		if (gameInfoManager.isPaused())
			return;

		entityManager.update();
		gameInfoManager.update();
		detectEndGame();

		processAgentAction();

		snapCameraToPlayer();
	}

	public void addAgent(Agent agent) {
		agents.add(agent);
	}

	private void processAgentAction() {
<<<<<<< HEAD
		for (Agent agent : agents) {
			Action action = agent.getNextAction();
			try {
				agent.getCharacter().performAction(action);
			} catch (CharacterActionException ignored) {
			}
		}
	}

	private void processPlayerInput() {
		Character player = entityManager.getPlayer();
		if (!player.isAlive())
			return;

		processPlayerInputMove(player);

		if (player instanceof Bomber) {
			if (_input.space) {
				try {
					player.performAction(ActionConstants.PLACE_BOMB);
				} catch (CharacterActionException ignored) {
				}
=======
		for (Agent agent: agents) {
			List<Action> actions = agent.getNextActions();
			for (Action action: actions) {
				try {
					agent.getCharacter().performAction(action);
				} catch (CharacterActionException ignored) {}
>>>>>>> ab179389b9c2c4a18043972ca63a3276b071544d
			}
		}
	}

<<<<<<< HEAD
	private void processPlayerInputMove(Character player) {
		int xa = 0, ya = 0;
		if (_input.up)
			ya--;
		if (_input.down)
			ya++;
		if (_input.left)
			xa--;
		if (_input.right)
			xa++;

		if (xa != 0 || ya != 0) {
			ActionMove actionMove = new ActionMove(xa, ya);
			try {
				player.performAction(actionMove);
			} catch (CharacterActionException ignored) {
			}
		}
	}

=======
>>>>>>> ab179389b9c2c4a18043972ca63a3276b071544d
	@Override
	public void render(Screen screen) {
		if (gameInfoManager.isPaused())
			return;
		entityManager.render(screen);
	}

	public void nextLevel() {
		loadLevel(_levelLoader.getLevel() + 1);
	}

	public void loadLevel(int level) {
		_screenToShow = 2;
		_game.resetScreenDelay();
<<<<<<< HEAD
		_game.pause();

=======
		
>>>>>>> ab179389b9c2c4a18043972ca63a3276b071544d
		try {
			_levelLoader = new FileLevelLoader(this, level);
			gameInfoManager = new GameInfoManager();
			entityManager = new EntityManager(_levelLoader, gameInfoManager);
			gameInfoManager.setEntityManager(entityManager);
			gameInfoManager.pause();

			_levelLoader.createEntities();
		} catch (LoadLevelException e) {
			endGame();
		}
	}

	protected void detectEndGame() {
		if (gameInfoManager.getTime() <= 0) {
			endGame();
		}
	}

	public void endGame() {
		_screenToShow = 1;
		_game.resetScreenDelay();
		gameInfoManager.pause();
	}

	public void drawScreen(Graphics g) {
		switch (_screenToShow) {
			case 1:
				_screen.drawEndGame(g, gameInfoManager.getPoints());
				break;
			case 2:
				_screen.drawChangeLevel(g, _levelLoader.getLevel());
				break;
			case 3:
				_screen.drawPaused(g);
				break;
		}
	}

	public LevelLoader getLevel() {
		return _levelLoader;
	}

	public Game getGame() {
		return _game;
	}

	public int getShow() {
		return _screenToShow;
	}

	public void setShow(int i) {
		_screenToShow = i;
	}

	public int getWidth() {
		return _levelLoader.getWidth();
	}

	public int getHeight() {
		return _levelLoader.getHeight();
	}

	public IEntityManager getEntityManager() {
		return entityManager;
	}

	public IGameInfoManager getGameInfoManager() {
		return gameInfoManager;
	}

	@Override
	public Board copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
