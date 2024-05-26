package uet.oop.bomberman.manager;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.base.IEntityManager;
import uet.oop.bomberman.base.IGameInfoManager;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.character.CanUseItem;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.utils.EScreenName;
import uet.oop.bomberman.utils.Global;

public class GameInfoManager implements IGameInfoManager {

    private int time;
    private int points;
    private boolean paused;
    private List<Message> messages = new ArrayList<>();

	private Game game;
    private IEntityManager entityManager;

	public GameInfoManager(Game game) {
		this.game = game;
        this.time = Game.TIME;
        this.points = Game.POINTS;
    }

    public void setEntityManager(IEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
	public List<Item> getPlayerActiveItems() {
		Character player = entityManager.getPlayer();
		if (!(player instanceof CanUseItem)) return new ArrayList<>();
		return ((CanUseItem)player).getActiveItems().collect(Collectors.toList());
	}

	@Override
	public void addMessage(Message e) {
		messages.add(e);
	}

	@Override
	public int subtractTime() {
		if (!isPaused() && time > 0)
			return --time;
		else
			return time;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public void addPoints(int points) {
		this.points += points;
	}

    @Override
    public void update() {
        updateMessages();
		if (getTime() <= 0) endGame();
    }

    @Override
    public void render(Screen screen) {}

	private void updateMessages() {
		Message m;
		int left;
		for (int i = 0; i < messages.size(); i++) {
			m = messages.get(i);
			left = m.getDuration();

			if (left > 0)
				m.setDuration(--left);
			else
				messages.remove(i);
		}
	}

    public void render(Screen screen, Graphics g) {
        renderMessages(g);
    }

	public void renderMessages(Graphics g) {
		Message m;
		for (int i = 0; i < messages.size(); i++) {
			m = messages.get(i);

			g.setFont(new Font("Arial", Font.PLAIN, m.getSize()));
			g.setColor(m.getColor());
			g.drawString(m.getMessage(), (int) m.getX() - Screen.xOffset * Game.SCALE, (int) m.getY());
		}
	}

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void unpause() {
        paused = false;
    }

	@Override
	public void endGame() {
		Global.currentScreen = EScreenName.END_GAME_SCREEN;
		game.setScreenToShow(1);
		game.resetScreenDelay();
		pause();
	}
    
}
