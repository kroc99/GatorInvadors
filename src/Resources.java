import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Resources {
    public static Image Title, background, ShipUpgrade, ShipHit, Ship, Enemy1, Enemy2, Enemy3, DeathScreen;

    static {
        Title = loadImage("src/resources/Title.png");
        background = loadImage("src/resources/Background.png");
        ShipUpgrade = loadImage("src/resources/ShipUpgrade.png");
        ShipHit = loadImage("src/resources/ShipHit.png");
        Ship = loadImage("src/resources/Ship.png");
        Enemy1 = loadImage("src/resources/Enemy1.png");
        Enemy2 = loadImage("src/resources/Enemy2.png");
        Enemy3 = loadImage("src/resources/Enemy3.png");
        DeathScreen = loadImage("src/resources/DeathScreen.png");
    }

    public static Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Other shared resources can be added here
}
