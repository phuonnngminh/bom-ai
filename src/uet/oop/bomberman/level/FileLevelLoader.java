package uet.oop.bomberman.level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.agent.Agent;
import uet.oop.bomberman.agent.MovingAgent;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Doll;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

public class FileLevelLoader extends LevelLoader {

    /**
     * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đọc được từ
     * ma trận bản đồ trong tệp cấu hình
     */
    private static char[][] _map;

    public FileLevelLoader(Board board, int level) throws LoadLevelException {
        super(board, level);
    }

    @Override
    public void loadLevel(int level) {
        // TODO: đọc dữ liệu từ tệp cấu hình /levels/Level{level}.txt
        // TODO: cập nhật các giá trị đọc được vào _width, _height, _level, _map
        List<String> list = new ArrayList<>();
        try {
            FileReader fr = new FileReader("res/levels/Level" + level + ".txt");//doc tep luu map
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (!line.equals("")) {
                list.add(line);
                line = br.readLine();
                //doc file txt luu vao list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] arrays = list.get(0).trim().split(" ");
        _level = Integer.parseInt(arrays[0]);
        _height = Integer.parseInt(arrays[1]);
        _width = Integer.parseInt(arrays[2]);
        _map = new char[_height][_width];
        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                _map[i][j] = list.get(i + 1).charAt(j);
            }
        }
        //gan cac phan tu cho mang
    }

    @Override
    public void createEntities() {
        Enemy enemy;
        Agent agent;
        LayeredEntity layeredEntity;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int pos = x + y * getWidth();
                char c = _map[y][x];
                switch (c) {
                    // Thêm grass
                    case ' ':
                        _board.getEntityManager().getTileManager().addTile(pos, new Grass(x, y, Sprite.grass));
                        break;
                    // Thêm Wall
                    case '#':
                        _board.getEntityManager().getTileManager().addTile(pos, new Wall(x, y, Sprite.wall));
                        break;
                    // Thêm Portal
                    case 'x':
                        layeredEntity = new LayeredEntity(
                            x, y,
                            new Grass(x, y, Sprite.grass),
                            new Portal(x, y, _board, Sprite.portal),
                            new Brick(x, y, Sprite.brick)
                        );
                        _board.getEntityManager().getTileManager().addTile(pos, layeredEntity);
                        break;
                    // Thêm brick
                    case '*':
                        layeredEntity = new LayeredEntity(
                            x, y,
                            new Grass(x, y, Sprite.grass),
                            new Brick(x, y, Sprite.brick)
                        );
                        _board.getEntityManager().getTileManager().addTile(x + y * _width, layeredEntity);
                        break;
                    // Thêm Bomber player
                    case 'p':
                        Bomber bomber = new Bomber(
                            Coordinates.tileToPixel(x),
                            Coordinates.tileToPixel(y) + Game.TILES_SIZE,
                            Game.BOMBERSPEED,
                            Game.BOMBRATE,
                            Game.BOMBRADIUS,
                            _board.getEntityManager(),
                            _board.getGameInfoManager(),
                            _board.getEntityManager().getBombManager(),
                            _board
                        );
                        _board.getEntityManager().getCharacterManager().addCharacter(bomber);
                        _board.getEntityManager().getCharacterManager().setPlayer(bomber);
                        Screen.setOffset(0, 0);
                        _board.getEntityManager().getTileManager().addTile(x + y * _width, new Grass(x, y, Sprite.grass));
                        break;

                    // Thêm balloon
                    case '1':
                        enemy = new Balloon(
                            Coordinates.tileToPixel(x),
                            Coordinates.tileToPixel(y) + Game.TILES_SIZE,
                            _board.getEntityManager()
                        );
                        _board.getEntityManager().getCharacterManager().addCharacter(enemy);
                        _board.getEntityManager().getTileManager().addTile(x + y * _width, new Grass(x, y, Sprite.grass));
                        agent = new MovingAgent(enemy, new AILow());
                        _board.addAgent(agent);
                        break;
                    // Thêm oneal
                    case '2':
                        enemy = new Oneal(
                            Coordinates.tileToPixel(x),
                            Coordinates.tileToPixel(y) + Game.TILES_SIZE,
                            _board.getEntityManager()
                        );
                        _board.getEntityManager().getCharacterManager().addCharacter(enemy);
                        _board.getEntityManager().getTileManager().addTile(pos, new Grass(x, y, Sprite.grass));
                        agent = new MovingAgent(enemy, new AILow());
                        _board.addAgent(agent);
                        break;
                    // Thêm doll
                    case '3':
                        enemy = new Doll(
                            Coordinates.tileToPixel(x),
                            Coordinates.tileToPixel(y) + Game.TILES_SIZE,
                            _board.getEntityManager()
                        );
                        _board.getEntityManager().getCharacterManager().addCharacter(enemy);
                        _board.getEntityManager().getTileManager().addTile(x + y * _width, new Grass(x, y, Sprite.grass));
                        agent = new MovingAgent(enemy, new AIMedium(enemy, _board.getEntityManager().getCharacterManager()));
                        _board.addAgent(agent);
                        break;
                    // Thêm oneal
                    // Thêm BomItem            
                    case 'b':
                        layeredEntity = new LayeredEntity(
                            x, y,
                            new Grass(x, y, Sprite.grass),
                            new BombItem(x, y, Sprite.powerup_bombs),
                            new Brick(x, y, Sprite.brick)
                        );
                        _board.getEntityManager().getTileManager().addTile(pos, layeredEntity);
                        break;
                    // Thêm SpeedItem
                    case 's':
                        layeredEntity = new LayeredEntity(
                            x, y,
                            new Grass(x, y, Sprite.grass),
                            new SpeedItem(x, y, Sprite.powerup_speed),
                            new Brick(x, y, Sprite.brick)
                        );
                        _board.getEntityManager().getTileManager().addTile(pos, layeredEntity);
                        break;
                    // Thêm FlameItem
                    case 'f':
                        layeredEntity = new LayeredEntity(
                            x, y,
                            new Grass(x, y, Sprite.grass),
                            new FlameItem(x, y, Sprite.powerup_flames),
                            new Brick(x, y, Sprite.brick)
                        );
                        _board.getEntityManager().getTileManager().addTile(pos, layeredEntity);
                        break;

                    default:
                        _board.getEntityManager().getTileManager().addTile(pos, new Grass(x, y, Sprite.grass));
                        break;

                }
            }
        }
    }
}
