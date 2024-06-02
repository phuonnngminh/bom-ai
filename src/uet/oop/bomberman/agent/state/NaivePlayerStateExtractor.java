package uet.oop.bomberman.agent.state;

import java.util.concurrent.atomic.AtomicInteger;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.agent.state.base.PlayerStateExtractor;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Character;

public class NaivePlayerStateExtractor extends PlayerStateExtractor {

    private static final int FIELD_OF_VISION = 9;

    public NaivePlayerStateExtractor(Character player) {
        super(player);
    }

    @Override
    public int getDimension() {
        return 3 * FIELD_OF_VISION * FIELD_OF_VISION;
    }

    private interface BoardTilePredicate {
        float test(Board board, int x, int y);
    }

    @Override
    public float[] getEmbedding(Board board) {
        float[] embedding = new float[getDimension()];

        AtomicInteger currentIndex = new AtomicInteger(0);

        addSurroundingTileMask(board, embedding, currentIndex, this::isPassable);
        addSurroundingTileMask(board, embedding, currentIndex, this::isItem);
        addSurroundingTileMask(board, embedding, currentIndex, this::isEnemy);

        return embedding;
    }

    private void addSurroundingTileMask(Board board, float[] embedding, AtomicInteger currentIndex, BoardTilePredicate predicate) {
        for (int dy = -FIELD_OF_VISION / 2; dy <= FIELD_OF_VISION / 2; dy++) {
            for (int dx = -FIELD_OF_VISION / 2; dx <= FIELD_OF_VISION / 2; dx++) {
                int x = player.getXTile() + dx;
                int y = player.getYTile() + dy;
                float value = predicate.test(board, x + dx, y + dy);
                embedding[currentIndex.getAndIncrement()] = value;
            }
        }
    }

    private float isEnemy(Board board, int x, int y) {
        Character character = board.getEntityManager().getCharacterManager().getCharacterAtExcluding(x, y, player);
        if (character == null) return 0;
        return character.isPlayer() ? 0 : 1;
    }

    private float isItem(Board board, int x, int y) {
        Entity entity = board.getEntityManager().getEntityAt(x, y);
        if (entity == null) return 0;
        if (entity instanceof LayeredEntity) {
            LayeredEntity layeredEntity = (LayeredEntity) entity;
            return layeredEntity.hasItem() ? 1 : 0;
        }
        return 0;
    }

    private float isPassable(Board board, int x, int y) {
        int width = board.getLevelManager().getBoardWidth();
        int height = board.getLevelManager().getBoardHeight();
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return 0;
        }
        Entity entity = board.getEntityManager().getEntityAt(x, y);
        if (entity == null) return 0;
        return entity.canBePassedThroughBy(player) ? 1 : 0;
    }

    @Override
    public float getValue(Board board) {
        float value = 0;

        // Penalize based on number of enemies still alive
        float enemyPoints = 0;
        for (Character character: board.getEntityManager().getCharacterManager().getCharacters()) {
            if (character == player) {
                continue;
            }
            if (character.isPlayer()) {
                continue;
            }
            enemyPoints += character.getPoints();
        }
        value -= enemyPoints;

        return value;
    }

}
