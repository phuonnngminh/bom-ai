package uet.oop.bomberman.base;

import java.awt.Graphics;

import uet.oop.bomberman.graphics.IRender;
import uet.oop.bomberman.graphics.Screen;

public interface IGameInfoManager extends IActiveItemManager, IMessageManager, IRender {

    public int subtractTime();
    public int getTime();

    public int getPoints();
    public void addPoints(int points);

    public boolean isPaused();
    public void pause();
    public void unpause();

    public void setEntityManager(IEntityManager entityManager);
    public void render(Screen screen, Graphics g);

}