package uet.oop.bomberman.base;

import uet.oop.bomberman.entities.tile.Tile;

public interface ITileManager {

    public Tile getTileAt(double x, double y);
    public void addTile(int pos, Tile e);

}