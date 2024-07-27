import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GatorEngine {
    //UI Components (things that are "more" related to the UI)
    static Screen currentScreen;
    private static ScreenManager screenManager = new ScreenManager();
    static JFrame WINDOW;
    static JPanel DISPLAY_CONTAINER;
    static JLabel DISPLAY_LABEL;
    static BufferedImage DISPLAY;
    static int WIDTH=800, HEIGHT=800;


    //Engine Caomponents (things that are "more" related to the engine structures)
    static Graphics2D RENDERER;
    static ArrayList<GameObject> OBJECTLIST = new ArrayList<>(); //list of GameObjects in the scene
    static ArrayList<GameObject> CREATELIST = new ArrayList<>(); //list of GameObjects to add to OBJECTLIST at the end of the frame
    static ArrayList<GameObject> DELETELIST = new ArrayList<>(); //list of GameObjects to remove from OBJECTLIST at the end fo the frame
    static float FRAMERATE = 60; //target frames per second;
    static float FRAMEDELAY = 1000/FRAMERATE; //target delay between frames
    static Timer FRAMETIMER; //Timer controlling the update loop
    static Thread FRAMETHREAD; //the Thread implementing the update loop
    static Thread ACTIVE_FRAMETHREAD; //a copy of FRAMETHREAD that actually runs.


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateEngineWindow();

                screenManager.setScreen(new MainMenuScreen(screenManager));

            }
        });

    }


    static void CreateEngineWindow() {
        //Sets up the GUI
        WINDOW = new JFrame("GATOR INVADERS");
        WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WINDOW.setVisible(true);
        DISPLAY = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        RENDERER = (Graphics2D) DISPLAY.getGraphics();
        DISPLAY_CONTAINER = new JPanel();
        DISPLAY_CONTAINER.setFocusable(true);
        DISPLAY_LABEL = new JLabel(new ImageIcon(DISPLAY));
        DISPLAY_CONTAINER.add(DISPLAY_LABEL);
        WINDOW.add(DISPLAY_CONTAINER);
        WINDOW.pack();

        FRAMETHREAD = new Thread(new Runnable() {
            @Override
            public void run() {
                Update();
                UpdateObjectList();
                Input.UpdateInputs();

                SwingUtilities.invokeLater(() -> DISPLAY_LABEL.repaint());
            }
        });

        //This copies the template thread made above
        ACTIVE_FRAMETHREAD = new Thread(FRAMETHREAD);


        FRAMETIMER = new Timer((int) FRAMEDELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ACTIVE_FRAMETHREAD.isAlive()) {
                    ACTIVE_FRAMETHREAD = new Thread(FRAMETHREAD);
                    ACTIVE_FRAMETHREAD.start();
                }
            }
        });
        FRAMETIMER.start();

        Start();
        DISPLAY_CONTAINER.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();

                if (!Input.held.contains(keyChar)) {
                    Input.pressed.add(keyChar);
                    Input.held.add(keyChar);
                }
            }

            public void keyTyped(KeyEvent e) {
                // No action needed for keyTyped in this context
            }

            @Override
            public void keyReleased(KeyEvent e) {
                char keyChar = e.getKeyChar();
                Input.held.remove((Character) keyChar);
                if (!Input.released.contains(keyChar)) {
                    Input.released.add(keyChar);
                }
            }

        });
        DISPLAY_CONTAINER.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Input.MouseClicked = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Input.MousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Input.MousePressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Implement any specific logic when the mouse enters the window
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Implement any specific logic when the mouse exits the window
            }
        });
        DISPLAY_CONTAINER.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Input.MouseX = e.getX();
                Input.MouseY = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Input.MouseX = e.getX();
                Input.MouseY = e.getY();
            }
        });
    }
    static void Create(GameObject g) {
        CREATELIST.add(g);
    }
    static void Delete(GameObject g) {
        DELETELIST.add(g);
    }
    static void UpdateObjectList() {
        OBJECTLIST.removeAll(DELETELIST);
        OBJECTLIST.addAll(CREATELIST);
        OBJECTLIST.removeIf(gameObject -> !gameObject.active);
        DELETELIST.clear();
        CREATELIST.clear();

    }
    static void Start() {

        // Start all objects in OBJECTLIST
        for (GameObject gameObject : OBJECTLIST) {
            gameObject.Start();
        }
    }

    static void Update(){
        screenManager.update();
        screenManager.render(RENDERER);

        for (GameObject gameObject : OBJECTLIST) {
            gameObject.Draw(RENDERER);
            gameObject.Update();
        }



        Input.UpdateInputs();
        UpdateObjectList();
        // Ensure this is called to update the object list
    }
    public static Point getMouseClickPosition() {
        if (Input.MouseClicked) {
            return new Point(Input.MouseX, Input.MouseY);
        }
        return null;
    }


}
