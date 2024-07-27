import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class GameObject {
    public AffineTransform transform;
    public Shape shape;
    public Material material;
    public ArrayList<ScriptableBehavior> scripts = new ArrayList<>();
    public boolean active = true;

    public GameObject() {
        this.transform = new AffineTransform();
        this.shape = new Rectangle2D.Float(0, 0, 10, 10);
        this.material = new Material(); // Assuming Material has a default constructor
    }

    public GameObject(int x, int y) {
        this();
        this.transform.translate(x, y);
    }

    public void Draw(Graphics2D pen) {
        // Save the current transform of the pen to restore it later
        AffineTransform oldTransform = pen.getTransform();

        // Apply the GameObject's transform to the pen
        pen.transform(this.transform);

        if (this.material.hasImage()) {
            // Draw the image
            BufferedImage img = this.material.getImage();
            pen.drawImage(img, 0, 0, null);
        } else {
            // Set fill color
            pen.setColor(this.material.getFill());
            pen.fill(this.shape); // Fill the shape

            // Draw border if it has one
            if (this.material.hasBorder()) {
                pen.setColor(this.material.getBorder());
                pen.setStroke(new BasicStroke(this.material.getBorderWidth()));
                pen.draw(this.shape); // Draw the border
            }
        }

        // Restore the original transform of the pen
        pen.setTransform(oldTransform);
    }


    public void Start() {
        for (ScriptableBehavior script : scripts) {
            script.Start();
        }
    }

    public void Update() {
        for (ScriptableBehavior script : scripts) {
            script.Update();
        }
    }

    public void Translate(float dX, float dY) {
        this.transform.translate(dX, dY);
    }

    public void Scale(float sX, float sY) {
        Rectangle2D bounds = shape.getBounds2D();
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();
        this.transform.translate(centerX, centerY);
        this.transform.scale(sX, sY);
        this.transform.translate(-centerX, -centerY);
    }

    public boolean CollidesWith(GameObject other) {
        // Create a new shape that applies the transformation to the original shape
        Shape transformedShapeThis = this.transform.createTransformedShape(this.shape);
        Shape transformedShapeOther = other.transform.createTransformedShape(other.shape);

        // Create Area objects from the transformed shapes
        Area areaThis = new Area(transformedShapeThis);
        Area areaOther = new Area(transformedShapeOther);


        // Check for intersection
        areaThis.intersect(areaOther);

        // Return true if the intersection is not empty (indicating a collision)
        return !areaThis.isEmpty();
    }

    public boolean Contains(Point2D point) {
        return this.shape.contains(point);
    }
}