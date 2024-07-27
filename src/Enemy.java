import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Enemy extends GameObject {
    private int level;
    private float movementSpeed;
    private float zigzagStepSize = 50;
    private boolean movingRight = true;
    private int directionChanges = 0;
    private float bulletProbability;
    private double lastTurnX, lastTurnY; // Variables to store the position at the last turn

    public Enemy(int x, int y, int level) {
        super(x, y);
        this.level = level;
        this.shape = new Rectangle2D.Float(-25, -25, 50, 50);
        applyScaling(2.0f);
        this.material = new Material(); // Initialize with default Material
        setEnemyImage(level);
        setMovementAndShootingParameters(level);
        this.lastTurnX = x;
        this.lastTurnY = y;


    }

    private void setEnemyImage(int level) {
        // Assign the corresponding image based on the level
        switch(level) {
            case 1:
                this.material.setImg((BufferedImage) Resources.Enemy1);
                break;
            case 2:
                this.material.setImg((BufferedImage) Resources.Enemy2);
                break;
            case 3:
                this.material.setImg((BufferedImage) Resources.Enemy3);
                break;
            default:
                // Handle invalid level case
                break;
        }
    }
    private void applyScaling(float scaleFactor) {
        this.transform.scale(scaleFactor, scaleFactor);
    }
    public boolean fireBullet = false;
    private void setMovementAndShootingParameters(int level) {
        // Define movement speed and bullet probability based on level
        // Example values for demonstration, adjust as needed
        switch(level) {
            case 1:
                movementSpeed = 0.1f;
                bulletProbability = 0.2f;
                break;
            case 2:
                movementSpeed = 0.1f;
                bulletProbability = 0.3f;
                break;
            case 3:
                movementSpeed = 0.1f;
                bulletProbability = 0.4f;
                break;
        }
    }

    @Override
    public void Update() {
        // Movement logic
        float moveAmount = movementSpeed;
        if (directionChanges % 4 == 0 || directionChanges % 4 == 2) {
            // Horizontal movement
            if (movingRight) {
                Translate(moveAmount, 0);
            } else {
                Translate(-moveAmount, 0);
            }
        } else {
            // Vertical movement
            Translate(0, moveAmount);
        }

        if (reachedZigzagTurn()) {
            directionChanges++;
            if (directionChanges % 4 == 1 || directionChanges % 4 == 3) {
                movingRight = !movingRight; // Change horizontal direction
            }
            lastTurnX = this.transform.getTranslateX();
            lastTurnY = this.transform.getTranslateY();
            if (this.active && maybeShoot()) {
                this.fireBullet = true; // Indicate that this enemy wants to fire a bullet
            }
        }
    }

    private boolean reachedZigzagTurn() {
        // Get the current position
        double currentX = this.transform.getTranslateX();
        double currentY = this.transform.getTranslateY();

        // Check if the enemy has moved 50 pixels since the last turn
        if (directionChanges % 4 == 0 || directionChanges % 4 == 2) {
            // Horizontal movement
            return Math.abs(currentX - lastTurnX) >= zigzagStepSize;
        } else {
            // Vertical movement
            return Math.abs(currentY - lastTurnY) >= zigzagStepSize;
        }
    }

    public boolean maybeShoot() {
        return Math.random() < bulletProbability;
    }

    public void hitByBullet() {
        // Logic when hit by a bullet
        if (level > 1) {
            level--;
            setEnemyImage(level); // Change image after downgrade
            setMovementAndShootingParameters(level);
        } else {
            active = false; // Delete if level 1
        }
    }
    public double GetY(){
        return lastTurnY;
    }

}
