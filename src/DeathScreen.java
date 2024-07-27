import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;

public class DeathScreen implements Screen {
    private ScreenManager screenManager;
    private Rectangle2D.Float backButtonBounds;
    private boolean isHoveringBackButton = false; // Flag for back button hover state

    public DeathScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        backButtonBounds = new Rectangle2D.Float(232, 610, 359, 80); // Example button position and size

        // Add mouse motion listener
        GatorEngine.DISPLAY_CONTAINER.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                isHoveringBackButton = backButtonBounds.contains(e.getX(), e.getY());
                GatorEngine.DISPLAY_LABEL.repaint(); // Repaint to update the shading
            }
        });
        // Add mouse listener for click detection
        GatorEngine.DISPLAY_CONTAINER.addMouseListener(new MouseAdapter() {
            private boolean ignoreInitialClick = true;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (ignoreInitialClick) {
                    ignoreInitialClick = false;
                    return;
                }

                if (backButtonBounds.contains(e.getX(), e.getY())) {
                    screenManager.setScreen(new MainMenuScreen(screenManager));
                    System.out.println("Back Button Clicked");
                }
            }
        });
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(Resources.DeathScreen, 0, 0, 800, 800, null); // Render the death screen background

        // Change color based on hover state
        if (isHoveringBackButton) {
            Color semiTransparentBlack = new Color(0, 0, 0, 64); // Semi-transparent black for shading
            g.setColor(semiTransparentBlack);
            g.fill(new Rectangle2D.Float(backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height));
        }
    }
    @Override
    public void update() {
        if (Input.MouseClicked) {
            Point clickPosition = GatorEngine.getMouseClickPosition();
            if (backButtonBounds.contains(clickPosition.x, clickPosition.y)) {
                screenManager.setScreen(new MainMenuScreen(screenManager));
            }
            Input.MouseClicked = false; // Reset the flag immediately after handling
        }
    }

    @Override
    public void dispose() {
        // Add any necessary cleanup code here
    }

    // Implement other methods as needed
}
