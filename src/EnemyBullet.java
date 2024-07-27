import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class EnemyBullet extends GameObject {
    private float speed;

    public EnemyBullet(int startX, int startY, float speed) {
        super(startX, startY);
        this.speed = speed;
        this.shape = new Rectangle2D.Float(0, 0, 10, 10); // Small rectangular shape
        this.material.setFill(Color.RED); // Set color to red



    }

    @Override
    public void Update() {
        Translate(0, speed); // Move the bullet downwards

        Point2D position = getPosition();




    }
    public Point2D getPosition() {
        // Create a Point2D object representing the origin (0,0)
        Point2D.Float position = new Point2D.Float(0, 0);

        // Transform the origin to get the current position
        transform.transform(position, position);

        return position;
    }


}