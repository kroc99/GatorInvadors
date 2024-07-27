import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Ship extends GameObject {
    private int x, y; // The current position of the ship
    private final int speed = 3; // Speed of the ship's movement
    private final int shipWidth = 50; // Width of the ship image
    private final int shipHeight = 50; // Height of the ship image
    private final int hitboxSize = 70; // Size of the hitbox (larger than the ship)
    private final int dashSpeed = 7; // Dash speed
    private boolean isDashing = false; // Is the ship currently dashing
    private long lastDashTime = 0; // Last time the ship dashed
    private final long dashCooldown = 10000; // Dash cooldown in milliseconds
    private final long dashDuration = 300; // Dash duration in milliseconds
    boolean isImmune = false; // Track immunity state
    private long lastHitTime = 0; // Time when the ship was last hit
    public Ship(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        updateTransform();
        int hitboxOffset = (hitboxSize - shipWidth) / 2;
        this.shape = new Rectangle2D.Float(-hitboxOffset, -hitboxOffset, hitboxSize, hitboxSize);
        this.lastHitTime = System.currentTimeMillis(); // Initialize to current time
    }
    public boolean isImmune() {
        return isImmune;
    }
    public void hit() {
        lastHitTime = System.currentTimeMillis();
        isImmune = true;
        // Handle other logic for when the ship is hit (like reducing lives)
    }
    public void dash() {
        if (System.currentTimeMillis() - lastDashTime >= dashCooldown) {
            isDashing = true;
            lastDashTime = System.currentTimeMillis();
        }
    }
    public void update() {
        if (isDashing && System.currentTimeMillis() - lastDashTime > dashDuration) {
            isDashing = false;
        }
        if (isImmune && System.currentTimeMillis() - lastHitTime > 1000) {
            // Disable immunity after 1 second
            isImmune = false;
        }
        updateTransform();
    }



    public void draw(Graphics2D g) {
        if (isDashing) {
            g.drawImage(Resources.ShipUpgrade, x, y, shipWidth, shipHeight, null);
        } else if (isImmune) {
            long timeSinceHit = System.currentTimeMillis() - lastHitTime;
            if (timeSinceHit % 400 < 200) { // Flicker every 200 milliseconds
                g.drawImage(Resources.ShipHit, x, y, shipWidth, shipHeight, null);
            } else {
                g.drawImage(Resources.Ship, x, y, shipWidth, shipHeight, null);
            }
        } else {
            g.drawImage(Resources.Ship, x, y, shipWidth, shipHeight, null);
        }
    }



    public int getX() {
        return x;
    }

    // Getter for y coordinate
    public int getY() {
        return y;
    }
    public void moveLeft () {
        x -= speed;
        if (x < 0) x = 0;
        updateTransform();
        x -= isDashing ? dashSpeed : speed;
    }

    public void moveRight () {
        x += speed;
        updateTransform();
        x += isDashing ? dashSpeed : speed;
    }

    private void updateTransform () {
        this.transform.setToTranslation(x, y);
    }
}


