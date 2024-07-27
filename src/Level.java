import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<Enemy> enemies;
    int ROWS_PER_LEVEL = 3;
    private static final int COLUMNS = 10;
    private static final int ENEMY_SPACING = 75; // pixels

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        if (this.levelNumber == 1){
            ROWS_PER_LEVEL = 1;
        }
        if (this.levelNumber == 2){
            ROWS_PER_LEVEL = 2;
        }
        if (this.levelNumber == 3){
            ROWS_PER_LEVEL = 3;
        }
        this.enemies = new ArrayList<>();
        spawnEnemies();
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }

    private void spawnEnemies() {
        for (int row = 0; row < ROWS_PER_LEVEL; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int x = col * ENEMY_SPACING;
                int y = row * ENEMY_SPACING;
                int enemyLevel = Math.min(row + 1, levelNumber);
                enemies.add(new Enemy(x, y, enemyLevel));
            }
        }
    }
    public void update() {
        // Update logic for all enemies in this level
        for (Enemy enemy : enemies) {
            enemy.Update();
            // Add any additional update logic specific to the level
        }}

    public boolean isLevelComplete() {
        // Check if all enemies are inactive
        for (Enemy enemy : enemies) {
            if (enemy.active) {
                return false; // If any enemy is active, the level is not complete
            }
        }
        return true; // All enemies are inactive, level is complete
    }


    //

    // ... Additional methods for level management
}