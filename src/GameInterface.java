import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameInterface extends JFrame {
    Actions actions = new Actions();
    Squid squid = new Squid();

    // Adding indicator bar JLabels
    StatusBar fullness = new StatusBar(FULLNESS_BAR);
    StatusBar energy = new StatusBar(ENERGY_BAR);
    StatusBar mood = new StatusBar(MOOD_BAR);

    // Storing user-preferences
    Preferences prefs;

    // Keeps track of the squid object's current state
    int currentState;

    // Setting default button states
    boolean isSleepButtonOn;

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

    static JLabel energyText = new JLabel("Energy");
    static JLabel energyIcon = new JLabel(loadImage("src/assets/energy.PNG"));

    static JLabel moodText = new JLabel("Mood");
    static JLabel moodIcon = new JLabel(loadImage("src/assets/mood.PNG"));

    static JLabel statusBlock = new JLabel(loadImage("src/assets/statusbox.PNG"));

    static Icon sunnyGif = new ImageIcon("src/assets/sunny.GIF");
    static JLabel sunnyTheSquid = new JLabel(sunnyGif);

    static JLabel backdrop = new JLabel(loadImage("src/assets/backdrop.PNG"));

    // Preference keys
    // To save the state of the sleep button
    private static final String IS_BUTTON_ON = "IsButtonOn";

    // To save the current state of the squid object
    private static final String CURRENT_STATE = "SquidState";

    // To save the points in each status bar
    private static final String FULLNESS_BAR = "Fullness";
    private static final String ENERGY_BAR = "Energy";
    private static final String MOOD_BAR = "Mood";

    // Initializing executor to execute certain tasks after a specified time interval
    // In this case, after every hour that the squid is idle, all stats will decrease by one point
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    // Using Atomic reference to update tasks dynamically without needing to restart the application
    AtomicReference<Runnable> updateFullness = new AtomicReference<>();
    AtomicReference<Runnable> updateEnergy = new AtomicReference<>();
    AtomicReference<Runnable> updateMood = new AtomicReference<>();

    // Creating scheduled tasks
    volatile ScheduledFuture<?> scheduledFullness;
    volatile ScheduledFuture<?> scheduledEnergy;
    volatile ScheduledFuture<?> scheduledMood;

    Runnable decreaseFullness = () -> actions.updateStatusBar(fullness, -1, prefs.getInt(CURRENT_STATE, Squid.IDLE));
    Runnable decreaseEnergy = () -> actions.updateStatusBar(energy, -1, prefs.getInt(CURRENT_STATE, Squid.IDLE));
    Runnable decreaseMood = () -> actions.updateStatusBar(mood, -1, prefs.getInt(CURRENT_STATE, Squid.IDLE));

    Runnable increaseEnergy = () -> actions.updateStatusBar(energy, 1, prefs.getInt(CURRENT_STATE, Squid.IDLE));

    public GameInterface() {
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
        prefs = Preferences.userNodeForPackage(GameInterface.class);

        // Retrieve stored state, default to false if not found
        isSleepButtonOn = prefs.getBoolean(IS_BUTTON_ON, false);

        // Retrieve stored state for the squid's current status
        currentState = prefs.getInt(CURRENT_STATE, squid.getCurrentState());

        // Call the method to refresh display
        actions.sleep(isSleepButtonOn, fullness, energy, mood);
        addCustomComponents();
        addInteractionButtons();
        addStatusBox();
        addSprites();

        // Setting the background of the application
        // ONLY SET THIS AFTER ADDING ALL OTHER COMPONENTS
        add(backdrop);

        setCurrentTasks();
        startScheduledTasks();

    }

    // Creates the 'Font' object from a ttf file in order to create a custom font
    public Font getFont() {
        try {
            File fileName = new File("src/assets/PixelifySans-Regular.ttf");
            return Font.createFont(Font.TRUETYPE_FONT, fileName);

        } catch (FontFormatException | IOException exception) {
            Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, exception);
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

    // Creates a custom exit and minimize button
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

    // Adds 'Sleep', 'Eat', 'Play', and 'Bathe' button to interact with the squid sprite
    public void addInteractionButtons () {
        // Adding button for 'Sleep' action
        sleepButton.setBounds(21, 48, 59, 39);
        sleepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the button is NOT on, on click it will be switched to ON
                if(!isSleepButtonOn) {
                    isSleepButtonOn = true;

                    // Call 'sleep' method in actions to change the UI
                    actions.sleep(true, fullness, energy, mood);

                    // Keep button state inside preferences
                    prefs.putBoolean(IS_BUTTON_ON, isSleepButtonOn);

                    // Save the current state of the squid inside preferences
                    prefs.putInt(CURRENT_STATE, Squid.SLEEPING);

                    // Set current tasks
                }

                else {
                    isSleepButtonOn = false;
                    actions.sleep(false, fullness, energy, mood);

                    prefs.putBoolean(IS_BUTTON_ON, isSleepButtonOn);
                    prefs.putInt(CURRENT_STATE, Squid.IDLE);

                }

                // Sets the current tasks to be carried out
                setCurrentTasks();

                // Cancels old tasks in order to execute new tasks
                restartTasks();
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

    // Creates the section in which the Squid's stats can be viewed in a graphic format
    public void addStatusBox() {
        // Adding Sunny's name in the status box
        sunnyText.setBounds(135, 325, 60, 24);

        // Deriving the font from the 'getFont' method
        Font sunnyFont = getFont().deriveFont(Font.PLAIN, 20);

        // Setting custom font and adding to user-interface
        sunnyText.setFont(sunnyFont);
        sunnyText.setForeground(Color.BLACK);
        add(sunnyText);

        // Adding Sunny's 'Fullness' indicator
        // Adding text
        fullnessText.setBounds(27, 347, 63, 17);

        // Deriving font (for all status conditions)
        Font statusFont = getFont().deriveFont(Font.PLAIN,15);

        fullnessText.setFont(statusFont);
        fullnessText.setForeground(Color.BLACK);
        add(fullnessText);

        // Adding icon
        fullnessIcon.setBounds(87, 344, 26, 26);
        add(fullnessIcon);

        // Adding indicator bar
        fullness.setBounds(115, 349, 186, 15);
        add(fullness);

        // Adding Sunny's 'Energy' indicator
        // Adding text
        energyText.setFont(statusFont);
        energyText.setBounds(27, 370, 57, 17);
        energyText.setForeground(Color.BLACK);
        add(energyText);

        // Adding icon
        energyIcon.setBounds(85, 366, 26, 26);
        add(energyIcon);

        // Adding indicator bar
        energy.setBounds(115, 372, 186, 15);
        add(energy);

        // Adding Sunny's 'Mood' indicator
        // Adding text
        moodText.setFont(statusFont);
        moodText.setBounds(27, 393, 57, 17);
        moodText.setForeground(Color.BLACK);
        add(moodText);

        // Adding icon
        moodIcon.setBounds(85, 389, 26, 26);
        add(moodIcon);

        // Adding indicator bar
        mood.setBounds(115, 395, 186, 15);
        add(mood);

        // Adding the status box that displays Sunny's fullness, energy, and mood
        statusBlock.setBounds(17, 323, 296, 97);
        add(statusBlock);
    }

    // Adds the Squid's sprite
    public void addSprites() {
        // Adding Sunny
        sunnyTheSquid.setBounds(32,87, 267, 280);

        // Change the cursor when hovering over this component
        Image petting = getToolkit().getImage("src/assets/petting.PNG");
        sunnyTheSquid.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(petting, new Point(0, 0), "Petting"));

        add(sunnyTheSquid);
    }

    // Sets current tasks according to the current state of the squid
    // This executes the tasks at a certain time period (every hour)
    private void setCurrentTasks() {
        // Schedule the task to run every 5 seconds with a delay of 0 seconds
        // Change the series of tasks depending on whether the SLEEP BUTTON is on or not

        // If it is not on, decrease all stats accordingly by one point
        if(!prefs.getBoolean(IS_BUTTON_ON, false)) {
            updateFullness.set(decreaseFullness);
            updateEnergy.set(decreaseEnergy);
            updateMood.set(decreaseMood);
        }

        // If it is, decrease all stats but increase energy by 1 point every hour
        else {
            updateFullness.set(decreaseFullness);
            updateEnergy.set(increaseEnergy);
            updateMood.set(decreaseMood);
        }
    }

    // Starts the tasks
    private void startScheduledTasks() {
        scheduledFullness = executor.scheduleAtFixedRate(() -> updateFullness.get().run(), 5, 5, TimeUnit.SECONDS);
        scheduledEnergy = executor.scheduleAtFixedRate(() -> updateEnergy.get().run(), 5, 5, TimeUnit.SECONDS);
        scheduledMood = executor.scheduleAtFixedRate(() -> updateMood.get().run(), 5, 5, TimeUnit.SECONDS);
    }

    // Cancels previous tasks in order to run new ones
    private void restartTasks() {
        // If tasks are still running (not null) then cancel them in order to start new tasks
        if(scheduledFullness != null && scheduledEnergy != null && scheduledMood != null) {
            // Cancel all running tasks
            scheduledFullness.cancel(false);
            scheduledEnergy.cancel(false);
            scheduledMood.cancel(false);
        }

        // Start new tasks
        startScheduledTasks();
    }

}
