import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;

public class MainMenuScreen implements Screen {
    private ScreenManager screenManager;
    private Rectangle startButtonBounds;
    private boolean isHoveringStartButton = false; // Flag for button hover state

    public MainMenuScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;

        int buttonWidth = 159;
        int buttonHeight = 80;
        int centerX = 400;
        int centerY = 540;
        int topLeftX = centerX - (buttonWidth / 2);
        int topLeftY = centerY - (buttonHeight / 2);

        startButtonBounds = new Rectangle(topLeftX, topLeftY, buttonWidth, buttonHeight);

        GatorEngine.DISPLAY_CONTAINER.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                isHoveringStartButton = startButtonBounds.contains(e.getPoint());
                GatorEngine.DISPLAY_LABEL.repaint(); // Repaint to update the shading
            }
        });
        GatorEngine.DISPLAY_CONTAINER.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (startButtonBounds.contains(e.getPoint())) {
                    screenManager.setScreen(new GameScreen(screenManager));
                    System.out.println("Start Button Clicked");
                }
            }
        });
    }
    @Override
    public void render(Graphics2D g) {
        g.drawImage(Resources.Title, 0, 0, 800, 800, null); // Drawing the title image

        if (isHoveringStartButton) {
            Color semiTransparentBlack = new Color(0, 0, 0, 64); // Semi-transparent black for shading
            g.setColor(semiTransparentBlack);
            g.fillRect(startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height);
        }
    }

    public void dispose(){}
    @Override
    public void update() {
        if (Input.MouseClicked) {
            Point clickPosition = GatorEngine.getMouseClickPosition();
            if (startButtonBounds.contains(clickPosition)) {
                screenManager.setScreen(new GameScreen(screenManager));
            }
            Input.MouseClicked = false; // Reset the flag immediately after handling
        }
    }

    private void mouseClicked(MouseEvent e) {
        if (startButtonBounds.contains(e.getPoint())) {
            screenManager.setScreen(new GameScreen(screenManager));
            System.out.println("clicmed");
        }

    }
}
