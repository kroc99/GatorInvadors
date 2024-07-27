import java.awt.*;

public class ScreenManager {
    private Screen currentScreen;

    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.dispose(); // Dispose the current screen if it exists
        }
        currentScreen = screen; // Set the new screen
        GatorEngine.currentScreen = this.currentScreen; // Update the currentScreen reference in GatorEngine
    }

    public void update() {
        if (currentScreen != null) {
            currentScreen.update(); // Update the current screen if it exists
        }
    }

    public void render(Graphics2D g) {
        if (currentScreen != null) {
            currentScreen.render(g); // Render the current screen if it exists
        }
    }

}
