import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class GamePanel extends JFrame {
    Actions actions = new Actions();

    // Setting default button states
    boolean isSleepButtonOn;

    // Storing user-preferences
    Preferences prefs;

    // Initializing GUI components as instance variables (so they can be called in other classes)
    static JButton exit = new JButton(loadImage("src/assets/exit.PNG"));
    static JButton minimize = new JButton(loadImage("src/assets/minimize.PNG"));

    static JButton sleepButton = new JButton(loadImage("src/assets/sleep.PNG"));
    static JButton eatButton = new JButton(loadImage("src/assets/eat.PNG"));
    static JButton playButton = new JButton(loadImage("src/assets/play.PNG"));
    static JButton batheButton = new JButton(loadImage("src/assets/bathe.PNG"));

    static JLabel sunnyText = new JLabel("Sunny");

    static JLabel fullnessText = new JLabel("Fullness");
    static JLabel fullnessIcon = new JLabel(loadImage("src/assets/hunger.PNG"));
    static JLabel fullnessBar = new JLabel(loadImage("src/assets/bar-6.PNG"));

    static JLabel energyText = new JLabel("Energy");
    static JLabel energyIcon = new JLabel(loadImage("src/assets/energy.PNG"));
    static JLabel energyBar = new JLabel(loadImage("src/assets/bar-2.PNG"));

    static JLabel moodText = new JLabel("Mood");
    static JLabel moodIcon = new JLabel(loadImage("src/assets/mood.PNG"));
    static JLabel moodBar = new JLabel(loadImage("src/assets/bar-5.PNG"));

    static JLabel statusBlock = new JLabel(loadImage("src/assets/statusbox.PNG"));

    static Icon sunnyGif = new ImageIcon("src/assets/sunny.GIF");
    static JLabel sunnyTheSquid = new JLabel(sunnyGif);

    static JLabel backdrop = new JLabel(loadImage("src/assets/backdrop.PNG"));

    // Setting the value of the preference -- if the 'Sleep' button is clicked or not
    private static final String IS_BUTTON_ON = "IsButtonOn";

    public GamePanel() {
        // Setting up GUI and adding a title
        super("Sunny The Squid");

        // Configures GUI to end after the program has been closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Sets the size of the GUI (in pixels)
        setSize(330, 430);

        // Loads GUI at the center of the screen
        setLocationRelativeTo(null);

        // Prevents resizing
        setResizable(false);

        // Removes the title bar
        setUndecorated(true);

        // Setting the icon image of the application
        setIconImage(Objects.requireNonNull(loadImage("src/assets/app-icon.PNG")).getImage());

        // Set initial state for Sleep JButton
        prefs = Preferences.userNodeForPackage(GamePanel.class);

        // Retrieve stored state, default to false if not found
        isSleepButtonOn = prefs.getBoolean(IS_BUTTON_ON, false);

        // Call the method
        actions.sleep(isSleepButtonOn);

        addCustomComponents();
        addInteractionButtons();
        addStatusBox();
        addSprites();

        // Setting the background of the application
        // ONLY SET THIS AFTER ADDING ALL OTHER COMPONENTS
        add(backdrop);

    }

    // Creates the 'Font' object from a ttf file in order to create a custom font
    public Font getFont() {
        try {
            File fileName = new File("src/assets/PixelifySans-Regular.ttf");
            return Font.createFont(Font.TRUETYPE_FONT, fileName);

        } catch (FontFormatException | IOException exception) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, exception);
            return super.getFont();
        }
    }

    // Method to customize a cursor for the application
    public void customizeApplicationCursor() {
        Image cursor = getToolkit().getImage("src/assets/cursor.PNG");
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "Default"));
    }

    // Method to retrieve image from the asset folder
    public static ImageIcon loadImage(String resourcePath) {
        try {
            // Reads the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // Returns an image icon so the component can render it
            return new ImageIcon(image);

        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }

    public void addCustomComponents() {
        customizeApplicationCursor();

        // Creating the exit button
        exit.setBounds(291, 7, 32, 22);

        // Programmed so that the exit button will exit the application on click
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exit);

        minimize.setBounds(252, 7,32, 22);
        minimize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(JFrame.ICONIFIED);
            }
        });
        add(minimize);

    }

    public void addInteractionButtons () {
        // Adding button for 'Sleep' action
        sleepButton.setBounds(21, 48, 59, 39);
        sleepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the button is NOT on, on click it will be switched to ON
                if(!isSleepButtonOn) {
                    isSleepButtonOn = true;
                    actions.sleep(true);
                    prefs.putBoolean(IS_BUTTON_ON, isSleepButtonOn);
                }

                else {
                    isSleepButtonOn = false;
                    actions.sleep(false);
                    prefs.putBoolean(IS_BUTTON_ON, isSleepButtonOn);
                }
            }
        });
        add(sleepButton);

        // Adding button for 'Eat' action
        eatButton.setBounds(97, 48, 59, 39);
        add(eatButton);

        // Adding button for 'Play' action
        playButton.setBounds(172, 48, 59, 39);
        add(playButton);

        // Adding button for 'Bathe' action
        batheButton.setBounds(248, 48, 59, 39);
        add(batheButton);
    }

    public void addStatusBox() {
        // Adding Sunny's name in the status box
        sunnyText.setBounds(135, 325, 60, 24);

        // Deriving the font from the 'getFont' method
        Font sunnyFont = getFont().deriveFont(Font.PLAIN, 20);

        // Setting custom font and adding to user-interface
        sunnyText.setFont(sunnyFont);
        add(sunnyText);

        // Adding Sunny's 'Fullness' indicator
        // Adding text
        fullnessText.setBounds(27, 347, 63, 17);

        // Deriving font (for all status conditions)
        Font statusFont = getFont().deriveFont(Font.PLAIN,15);

        fullnessText.setFont(statusFont);
        add(fullnessText);

        // Adding icon
        fullnessIcon.setBounds(87, 344, 26, 26);
        add(fullnessIcon);

        // Adding indicator bar
        fullnessBar.setBounds(115, 349, 186, 15);
        add(fullnessBar);

        // Adding Sunny's 'Energy' indicator
        // Adding text
        energyText.setFont(statusFont);
        energyText.setBounds(27, 370, 57, 17);
        add(energyText);

        // Adding icon
        energyIcon.setBounds(85, 366, 26, 26);
        add(energyIcon);

        // Adding indicator bar
        energyBar.setBounds(115, 372, 186, 15);
        add(energyBar);

        // Adding Sunny's 'Mood' indicator
        // Adding text
        moodText.setFont(statusFont);
        moodText.setBounds(27, 393, 57, 17);
        add(moodText);

        // Adding icon
        moodIcon.setBounds(85, 389, 26, 26);
        add(moodIcon);

        // Adding indicator bar
        moodBar.setBounds(115, 395, 186, 15);
        add(moodBar);

        // Adding the status box that displays Sunny's fullness, energy, and mood
        statusBlock.setBounds(17, 323, 296, 97);
        add(statusBlock);
    }

    public void addSprites() {
        // Adding Sunny
        sunnyTheSquid.setBounds(32,87, 267, 280);

        // Change the cursor when hovering over this component
        Image petting = getToolkit().getImage("src/assets/petting.PNG");
        sunnyTheSquid.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(petting, new Point(0, 0), "Petting"));

        add(sunnyTheSquid);
    }
}
