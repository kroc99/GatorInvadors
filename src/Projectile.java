import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Projectile extends GameObject {
    private final int speed = 10; // Speed of the projectile's movement

    public Projectile(int startX, int startY) {
        super(startX, startY);
        // Set the shape to represent the projectile's physical form
        this.shape = new Rectangle2D.Float(0, 0, 5, 10);
        this.material.setFill(Color.WHITE);
    }

    @Override
    public void Update() {
        Translate(0, -speed); // Move the projectile upwards

    }

}
