import javax.naming.InitialContext;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {
    private ScreenManager screenManager;
    private Ship ship;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();


    private long lastFireTime = 0;
    private final long fireDelay = 500;
    private Random random = new Random();


    private Level currentLevel;
    private int currentLevelNumber = 1;
    private int shiplives = 3;

    private long immunityTime = 3000;
    private long lastHitTime;


    private static final int LIFE_ICON_WIDTH = 40; // Width of the life icon
    private static final int LIFE_ICON_HEIGHT = 40; // Height of the life icon
    private static final int LIFE_ICON_SPACING = 10; // Spacing between life icons


    public GameScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.ship = new Ship(400, 600);
        loadLevel(currentLevelNumber);

    }

    private void loadLevel(int levelNumber) {
        currentLevel = new Level(levelNumber);
        enemies.clear();
        projectiles.clear();
        enemyBullets.clear();
        enemies.addAll(currentLevel.getEnemies()); // Add newly spawned enemies for the new level
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(Resources.background, 0, 0, 800, 800, null);
        ship.draw(g);
        renderHealthUI(g);
        for (Projectile projectile : projectiles) {
            projectile.Draw(g);
        }

        for (EnemyBullet bullet : enemyBullets) {
            if(bullet.active){bullet.Draw(g);}

        }

        for (Enemy enemy : enemies) {
            if (enemy.active) {
                // Save the current transform




                // Draw the enemy with the scaled transform
                enemy.Draw(g);


            }
        }
    }

    private void renderHealthUI(Graphics2D g) {
        for (int i = 0; i < shiplives; i++) {
            int x = 10 + (i * (LIFE_ICON_WIDTH + LIFE_ICON_SPACING)); // Calculate the x position
            int y = 750; // Y position set to bottom left, adjust as needed
            g.drawImage(Resources.Ship, x, y, LIFE_ICON_WIDTH, LIFE_ICON_HEIGHT, null);
        }
    }
    @Override
    public void update() {
        shipControl();
        ship.update();
        // Update and manage player projectiles
        updateProjectiles();

        // Update and manage enemy bullets
        updateEnemyBullets();

        // Update and manage enemies
        updateEnemies();

        checkLevelCompletion();
        currentLevel.update();
    }
    private void updateProjectiles() {
        Iterator<Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            projectile.Update();
            if (!projectile.active) {
                projectileIterator.remove();
                continue;
            }

            for (Enemy enemy : enemies) {
                if (projectile.CollidesWith(enemy)&&enemy.active) {
                    enemy.hitByBullet();
                    projectile.active = false;
                    projectileIterator.remove();
                    break;
                }
            }
        }
    }
    private void updateEnemyBullets() {
        Iterator<EnemyBullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            EnemyBullet bullet = bulletIterator.next();
            bullet.Update();
            if (bullet.CollidesWith(ship) && !ship.isImmune) {
                ship.hit(); // Call hit method to trigger immunity
                shiplives -= 1;
                lastHitTime = System.currentTimeMillis();
                bullet.active = false;
                bulletIterator.remove();
            }

            if (!bullet.active) {
                bulletIterator.remove();
            }
        }
    }
    private void updateEnemies() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.Update();

            if (enemy.fireBullet) {
                enemyFire(enemy);
                enemy.fireBullet = false;
            }

            if ((enemy.GetY() > ship.getY())) {
                shiplives = 0;
            }
        }
    }


    private void checkLevelCompletion() {
        if (currentLevel.isLevelComplete()) {
            currentLevelNumber++;
            shiplives = 3;
            if(currentLevelNumber == 4){currentLevelNumber=1;}

            loadLevel(currentLevelNumber); // Load the next level
        } else if (shiplives == 0) {
            Input.resetMouseClickState();
            goToDeathScreen(); // Go to death screen if the ship has no lives left
        }
    }

    private void goToDeathScreen() {
        screenManager.setScreen(new DeathScreen(screenManager));
    }

    private void shipControl() {
        if (Input.GetKeyDown('a')) {
            ship.moveLeft();
        }
        if (Input.GetKeyDown('d')) {
            ship.moveRight();
        }
        if (Input.GetKeyDown(' ') && System.currentTimeMillis() - lastFireTime > fireDelay) {
            int projectileStartX = ship.getX() + 25; // Center of the ship's width
            int projectileStartY = ship.getY() + 25; // Center of the ship's height
            projectiles.add(new Projectile(projectileStartX, projectileStartY));
            lastFireTime = System.currentTimeMillis();
        }
        if (Input.GetKeyDown('s')) {
            ship.dash();
        }
    }


    private void enemyFire(Enemy enemy) {
        int bulletStartX = (int) enemy.transform.getTranslateX();
        int bulletStartY = (int) enemy.transform.getTranslateY();
        EnemyBullet bullet = new EnemyBullet(bulletStartX, bulletStartY, 4.0f); // Example bullet speed
        enemyBullets.add(bullet); // Add the bullet to the enemyBullets list
    }


    @Override
    public void dispose() {
        // Dispose resources
    }

    public void mouseClicked(MouseEvent e) {
        // Mouse click logic
    }

}
