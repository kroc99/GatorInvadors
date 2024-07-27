import java.util.ArrayList;

public class Input {
    static public ArrayList<Character> pressed = new ArrayList<>();
    static public ArrayList<Character> held = new ArrayList<>();
    static public ArrayList<Character> released = new ArrayList<>();

    public static int MouseX;
    public static int MouseY;
    public static boolean MousePressed;
    public static boolean MouseClicked;

    static void UpdateInputs() {
        // Clear pressed and released lists at the end of each frame
        pressed.clear();
        released.clear();
    }

    static boolean GetKeyPressed(char c) {
        return pressed.contains(c);
    }

    static boolean GetKeyDown(char c) {
        return held.contains(c);
    }

    static boolean GetKeyUp(char c) {
        return released.contains(c);
    }
    public static void resetMouseClickState() {
        MouseClicked = false;
    }
}