import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.sound.sampled.*;

public class GameInterface extends JFrame {
    Actions actions = new Actions();
    Squid squid = new Squid();

    // Storing user-preferences
    static Preferences prefs;

    // Keeps track of the squid object's current state
    int currentState;

    // Keeps track of the squid's cleanliness
    boolean isClean;

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

    // Sunny's default idle GIF
    static Icon sunnyIdle = new ImageIcon("src/assets/sunny.GIF");
    static JLabel sunnyTheSquid = new JLabel(sunnyIdle);

    // Storing all of Sunny's eating sprites inside an array
    static Icon[] sunnyEat = new Icon[9];

    // Storing all bathing sprites inside an array
    static Icon[] sunnyBathe = new Icon[5];

    /* When food is hovered over the squid, it will change the sprite to a BufferedImage in order to retrieve the
       color values of the image when hovering over it - this is to prevent activating the 'eat' method when hovering
       over a transparent pixel */
    static BufferedImage sunnyEatImg = loadBufferedImage("src/assets/eat-0.PNG");

    // Sunny's dirty GIF
    static Icon sunnyDirty = new ImageIcon("src/assets/sunny-dirty.GIF");

    // Sunny dirty IMG (for mouse hover)
    static Icon sunnyDirtyHover = new ImageIcon("src/assets/dirty-0.PNG");

    // To check pixels
    static BufferedImage sunnyDirtyImg = loadBufferedImage("src/assets/dirty-0.PNG");

    static JLabel squidFood = new JLabel(loadImage("src/assets/food-1.PNG"));
    static JLabel soap = new JLabel(loadImage("src/assets/soap.PNG"));

    static JLabel backdrop = new JLabel(loadImage("src/assets/backdrop.PNG"));

    Image petting = getToolkit().getImage("src/assets/petting.PNG");

    // Preference keys
    // To save the state of the sleep button
    static final String IS_BUTTON_ON = "IsButtonOn";

    // To save the current state of the squid object
    static final String CURRENT_STATE = "SquidState";

    // To save the points in each status bar
    static final String FULLNESS_BAR = "Fullness";
    static final String ENERGY_BAR = "Energy";
    static final String MOOD_BAR = "Mood";

    // Saves the last time the bar was updated
    static final String LAST_UPDATE_TIME = "LastUpdateTime";

    // Keeps track of the squid's cleanliness
    static final String IS_CLEAN = "IsClean";

    // Keeps track of the last time the squid was clean
    static final String LAST_CLEAN_TIME = "LastClean";

    // Adding indicator bar JLabels
    static StatusBar fullness = new StatusBar(FULLNESS_BAR);
    static StatusBar energy = new StatusBar(ENERGY_BAR);
    static StatusBar mood = new StatusBar(MOOD_BAR);

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

    // Runnable to increase and decrease stats by 1 point
    Runnable decreaseFullness = () -> actions.updateStatusBar(fullness, -1, prefs.getInt(CURRENT_STATE, Squid.IDLE));
    Runnable decreaseEnergy = () -> actions.updateStatusBar(energy, -1, prefs.getInt(CURRENT_STATE, Squid.IDLE));
    Runnable decreaseMood = () -> actions.updateStatusBar(mood, -1, prefs.getInt(CURRENT_STATE, Squid.IDLE));
    Runnable increaseEnergy = () -> actions.updateStatusBar(energy, 1, prefs.getInt(CURRENT_STATE, Squid.IDLE));

    static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final int INTERVAL_MINUTES = 60;

    Timer cleanlinessTimer;
    static final long ONE_DAY_MS = 24 * 60 * 60 * 1000L; // 1 day in milliseconds
    long lastCleanTime;
    long remainingTimeUntilDirty;

    // Variables to store mouse coordinates
    private volatile int screenX;
    private volatile int screenY;
    private volatile int myX;
    private volatile int myY;

    // Variables to handle the audio being played
    private Clip backgroundClip;
    public static final String IDLE_TRACK = "src/assets/sfx/idle-track.wav";
    public static final String SLEEPING_TRACK = "src/assets/sfx/sleeping-track.wav";
    public static final String SOAP_BUBBLES = "src/assets/sfx/bathe-soap-bubbles.wav";
    public static final String BUTTON_CLICKED = "src/assets/sfx/button-clicked.wav";
    public static final String ERROR = "src/assets/sfx/error.wav";
    public static final String GLISTENING = "src/assets/sfx/glistening.wav";
    public static final String BURP = "src/assets/sfx/burp.wav";
    public static final String GAME_OVER = "src/assets/sfx/game-over.wav";
    public static final String BOUNCE = "src/assets/sfx/ball-bounce.wav";

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

        // Initializing Sunny's eating sprites
        for(int i = 0; i < 9; i++) {
            sunnyEat[i] = new ImageIcon("src/assets/eat-" + i + ".PNG");
        }

        // Initializing Sunny's bathing sprites
        for(int i = 0; i < sunnyBathe.length; i++) {
            sunnyBathe[i] = new ImageIcon("src/assets/bathe-" + i + ".PNG");
        }

        // Set initial state for Sleep JButton
        prefs = Preferences.userNodeForPackage(GameInterface.class);

        // Retrieve stored state, default to false if not found
        isSleepButtonOn = prefs.getBoolean(IS_BUTTON_ON, false);

        // Retrieve stored state for the squid's current status
        currentState = prefs.getInt(CURRENT_STATE, squid.getCurrentState());

        // Retrieve squid's cleanliness state
        isClean = prefs.getBoolean(IS_CLEAN, squid.isClean());

        // Retrieve the time that the squid was last cleaned
        lastCleanTime = prefs.getLong(LAST_CLEAN_TIME, System.currentTimeMillis());

        // Retrieve the time elapsed since last cleaned
        long timeElapsedSinceClean = System.currentTimeMillis() - lastCleanTime;

        if(timeElapsedSinceClean > ONE_DAY_MS && currentState == Squid.IDLE) {
            // Squid gets dirty
            prefs.putBoolean(IS_CLEAN, false);
            actions.updateCleanlinessState(prefs.getBoolean(IS_CLEAN, true), currentState);
            resetCleanlinessTimer();
        }

        // There is time left, continue running the timer with a new interval
        else if(timeElapsedSinceClean < ONE_DAY_MS) {
            long remainingTime = ONE_DAY_MS - timeElapsedSinceClean;
            startCleanlinessTimer(remainingTime);
        }

        // Call the method to refresh display
        actions.sleep(isSleepButtonOn, fullness, energy, mood);

        // Refresh squid sprite (only when it is IDLE)
        actions.updateCleanlinessState(prefs.getBoolean(IS_CLEAN, true), currentState);

        addSquidFood();
        addSoap();
        addCustomComponents();
        addInteractionButtons();
        addStatusBox();
        addSprites();

        // Setting the background of the application
        // ONLY SET THIS AFTER ADDING ALL OTHER COMPONENTS
        add(backdrop);

        // Checks all the tasks that were missed while the application was closed and returns the amount of time before the next task
        long remainingTime = checkedMissedExecutions();

        // Set current tasks according to the button state
        setCurrentTasks();

        // Start scheduled tasks, considering the previous execution time
        startScheduledTasks(remainingTime);

        // Play background music according to squid's current state
        if(isSleepButtonOn) {
            // If squid is sleeping, play sleep track
            playBackgroundMusic(SLEEPING_TRACK);
        }

        else {
            // Play default idle-track
            playBackgroundMusic(IDLE_TRACK);
        }
    }

    // Method to play a looping background track
    public void playBackgroundMusic(String filePath) {
        try {
            // Retrieves the audio file from path
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));

            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);

            // Loops background music
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);

            backgroundClip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Method to play short sfx (non-loop)
    public static void playSfx(String filePath) {
        try {
            // Retrieves the audio file from path
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));

            // Creates a new instance of 'clip' so the variable does not need to be declared again
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Stops the current track
    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
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

    // Retrieves an image from asset folder as a BufferedImage
    public static BufferedImage loadBufferedImage(String resourcePath) {
        try {
            // Reads the image file from the path given
            // Returns an image icon so the component can render it
            return ImageIO.read(new File(resourcePath));

        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }

    // Method to retrieve image from the asset folder as an ImageIcon
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
                playSfx(BUTTON_CLICKED);
                System.exit(0);
            }
        });
        add(exit);

        minimize.setBounds(252, 7,32, 22);

        // Minimize button will minimize application to system tray
        minimize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSfx(BUTTON_CLICKED);
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
                // Ensure that this action cannot be performed unless the squid is IDLE or already SLEEPING
                if((prefs.getInt(CURRENT_STATE, Squid.IDLE) == Squid.IDLE || prefs.getInt(CURRENT_STATE, Squid.IDLE) == Squid.SLEEPING) && prefs.getBoolean(IS_CLEAN, true)) {
                    playSfx(BUTTON_CLICKED);
                    // If the button is NOT on, on click it will be switched to ON
                    if (!isSleepButtonOn) {
                        isSleepButtonOn = true;

                        // Call 'sleep' method in actions to change the UI
                        actions.sleep(true, fullness, energy, mood);

                        // Keep button state inside preferences
                        prefs.putBoolean(IS_BUTTON_ON, isSleepButtonOn);

                        // Save the current state of the squid inside preferences
                        prefs.putInt(CURRENT_STATE, Squid.SLEEPING);

                        // Stops current background music
                        stopBackgroundMusic();

                        // Plays new music
                        playBackgroundMusic(SLEEPING_TRACK);

                        System.out.println("Sunny began sleeping at " + LocalDateTime.now().format(timeFormatter));
                    }

                    else {
                        isSleepButtonOn = false;
                        actions.sleep(false, fullness, energy, mood);

                        prefs.putBoolean(IS_BUTTON_ON, isSleepButtonOn);
                        prefs.putInt(CURRENT_STATE, Squid.IDLE);

                        stopBackgroundMusic();
                        playBackgroundMusic(IDLE_TRACK);

                        System.out.println("Sunny is idle at " + LocalDateTime.now().format(timeFormatter));
                    }

                    // Sets the current tasks to be carried out
                    setCurrentTasks();

                    // Cancels old tasks in order to execute new tasks
                    restartTasks();
                }
            }

        });
        add(sleepButton);

        // Adding button for 'Eat' action
        eatButton.setBounds(97, 48, 59, 39);
        eatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ensure that this action cannot be performed unless the squid is IDLE
                if(prefs.getInt(CURRENT_STATE, Squid.IDLE) == Squid.IDLE && prefs.getBoolean(IS_CLEAN, true)) {
                    playSfx(BUTTON_CLICKED);

                    // Set current state to 'Eating'
                    prefs.putInt(CURRENT_STATE, Squid.EATING);

                    // Spawn the draggable fish
                    squidFood.setBounds(226, 128, 92, 45);
                    squidFood.setVisible(true);
                }

                else {
                    // Play an error sound
                    playSfx(ERROR);
                }
            }
        });
        add(eatButton);

        // Adding button for 'Play' action
        playButton.setBounds(172, 48, 59, 39);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ensure that this action cannot be performed unless the squid is IDLE
                if(prefs.getInt(CURRENT_STATE, Squid.IDLE) == Squid.IDLE && prefs.getBoolean(IS_CLEAN, true)) {
                    playSfx(BUTTON_CLICKED);

                    // Set current state to 'Playing'
                    prefs.putInt(CURRENT_STATE, Squid.PLAYING);

                    // Dispose of current GUI components
                    dispose();
                    // Instantiating pong interface JFrame
                    PongInterface pong = new PongInterface(GameInterface.this);

                    // Show the pong interface
                    pong.setVisible(true);
                    pong.startGame();

                    // Set state back to IDLE
                    prefs.putInt(CURRENT_STATE, Squid.IDLE);

                    // Reset scheduled tasks (both mood and energy)
                    // Cancels tasks
                    scheduledEnergy.cancel(false);
                    scheduledMood.cancel(false);

                    // Reset tasks at default interval
                    scheduledEnergy = executor.scheduleAtFixedRate(() -> updateFullness.get().run(), INTERVAL_MINUTES, INTERVAL_MINUTES, TimeUnit.MINUTES);
                    scheduledMood = executor.scheduleAtFixedRate(() -> updateFullness.get().run(), INTERVAL_MINUTES, INTERVAL_MINUTES, TimeUnit.MINUTES);
                }

                else {
                    playSfx(ERROR);
                }
            }
        });
        add(playButton);

        // Adding button for 'Bathe' action
        batheButton.setBounds(248, 48, 59, 39);
        batheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ensure that this button can only be pressed when the squid is NOT CLEAN
                if(!prefs.getBoolean(IS_CLEAN, true)) {
                    playSfx(BUTTON_CLICKED);

                    // Set current state to 'bathing'
                    prefs.putInt(CURRENT_STATE, Squid.BATHING);

                    // Spawn soap bar
                    soap.setBounds(226,128, 60, 40);
                    soap.setVisible(true);
                }

                else {
                    playSfx(ERROR);
                }
            }
        });
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
        sunnyTheSquid.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(petting, new Point(0, 0), "Petting"));
        add(sunnyTheSquid);
    }

    public void addSquidFood() {
        squidFood.setBounds(226, 128, 92, 45);

        // Setting the component's visibility to 'false' as it will only show when the 'Eat' button is clicked
        squidFood.setVisible(false);

        // However, if you exit the program without consuming the fish, it will spawn in its default location again
        if(prefs.getInt(CURRENT_STATE, Squid.IDLE) == Squid.EATING) {
            squidFood.setVisible(true);
        }

        // Change the cursor when hovering over this component
        squidFood.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(petting, new Point(0, 0), "Petting"));

        // Adding mouse listener to the 'Squid Food' component in order to make it a draggable component
        squidFood.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Returns ABSOLUTE coordinate of the user's screen
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                // Get coordinate of the COMPONENT ORIGIN
                myX = squidFood.getX();
                myY = squidFood.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Get mouse position relative to the squid sprite
                int finalX = e.getXOnScreen() - sunnyTheSquid.getLocationOnScreen().x;
                int finalY = e.getYOnScreen() - sunnyTheSquid.getLocationOnScreen().y;

                // Check that the mouse is over an opaque pixel before allowing the component to be dropped
                if(sunnyTheSquid.getBounds().contains(squidFood.getBounds()) && isOverOpaquePixel(finalX, finalY, sunnyEatImg)) {
                    squidFood.setVisible(false); // Hide the squid food
                    actions.eat(fullness, sunnyEat); // Calls 'eat' method to change the sprite

                    // Reset the scheduled tasks (only for the fullness bar)
                    scheduledFullness.cancel(false);

                    // Restart scheduled task and run at default interval
                    scheduledFullness = executor.scheduleAtFixedRate(() -> updateFullness.get().run(), INTERVAL_MINUTES, INTERVAL_MINUTES, TimeUnit.MINUTES);

                    // Change current state back to 'Idle'
                    prefs.putInt(CURRENT_STATE, Squid.IDLE);
                }
            }
        });

        // Add mouse motion to set the new location of the component when it is DRAGGED
        squidFood.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Calculates the DIFFERENCE between the first time the component was clicked to the area it was dragged to
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                // Add delta coordinates to current coordinates and change the location of the component to the new location
                squidFood.setLocation(myX + deltaX, myY + deltaY);

                // If hovering over Sunny's sprite, change the GIF to a buffered image
                if(sunnyTheSquid.getBounds().intersects(squidFood.getBounds())) {
                    // Change GIF to static image
                    sunnyTheSquid.setIcon(sunnyEat[0]);
                }

                else {
                    // Restore GIF when not hovering
                    sunnyTheSquid.setIcon(sunnyIdle);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        add(squidFood);
    }

    public void addSoap() {
        soap.setBounds(226, 128, 60, 40);
        soap.setVisible(false);

        // However, if you exit the program without using the soap, it will spawn in its default location again
        if(prefs.getInt(CURRENT_STATE, Squid.IDLE) == Squid.BATHING) {
            soap.setVisible(true);
        }

        // Change the cursor when hovering over this component
        soap.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(petting, new Point(0, 0), "Petting"));

        soap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Returns ABSOLUTE coordinate of the user's screen
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                // Get coordinate of the COMPONENT ORIGIN
                myX = soap.getX();
                myY = soap.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Get mouse position relative to the squid sprite
                int finalX = e.getXOnScreen() - sunnyTheSquid.getLocationOnScreen().x;
                int finalY = e.getYOnScreen() - sunnyTheSquid.getLocationOnScreen().y;

                // Check that the mouse is over an opaque pixel before allowing the component to be dropped
                if(sunnyTheSquid.getBounds().contains(soap.getBounds()) && isOverOpaquePixel(finalX, finalY, sunnyDirtyImg)) {
                    // Hide the soap
                    soap.setVisible(false);

                    // Play short cleaning animation
                    actions.bathe(prefs.getInt(CURRENT_STATE, Squid.IDLE), prefs.getBoolean(IS_CLEAN, true), sunnyBathe);

                    // Change cleanliness state
                    prefs.putBoolean(IS_CLEAN, true);

                    // Keep track of the last clean time
                    prefs.putLong(LAST_CLEAN_TIME, System.currentTimeMillis());

                    // Change current state back to 'Idle'
                    prefs.putInt(CURRENT_STATE, Squid.IDLE);

                    // Reset the one-day timer
                    resetCleanlinessTimer();
                }
            }
        });

        soap.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Calculates the DIFFERENCE between the first time the component was clicked to the area it was dragged to
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                // Add delta coordinates to current coordinates and change the location of the component to the new location
                soap.setLocation(myX + deltaX, myY + deltaY);

                // If hovering over Sunny's sprite, change the GIF to a buffered image
                if(sunnyTheSquid.getBounds().intersects(soap.getBounds())) {
                    // Change GIF to static image
                    sunnyTheSquid.setIcon(sunnyDirtyHover);
                }

                else {
                    // Restore GIF when not hovering
                    sunnyTheSquid.setIcon(sunnyDirty);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        add(soap);
    }

    private boolean isOverOpaquePixel(int imgX, int imgY, BufferedImage sunnyImg) {
        // Prevent out of bound errors
        if(imgX < 0 || imgY < 0 || imgX >= sunnyTheSquid.getWidth() || imgY >= sunnyTheSquid.getHeight()) {
            return false;
        }

        // Get the pixel color of the image
        // The buffered image here is the same image as the static image
        int pixel = sunnyImg.getRGB(imgX, imgY);

        // Extract alpha channel (transparency)
        int alpha = (pixel >> 24) & 0xff;
        return alpha > 0;     // Only allow drop if alpha > 0 (not transparent)
    }

    // Saves the last time the status bar update method ran
    public static void saveExecutionTime() {
        prefs.put(LAST_UPDATE_TIME, LocalDateTime.now().format(timeFormatter));
    }

    // Sets current tasks according to the current state of the squid
    // This executes the tasks at a certain time period (every hour)
    private void setCurrentTasks() {
        // Schedule the task to run every 60 minutes
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
    private void startScheduledTasks(long remainingTime) {
        // Start the next execution after a delay period (the remaining time left until the next execution)
        scheduledFullness = executor.scheduleAtFixedRate(() -> updateFullness.get().run(), remainingTime, INTERVAL_MINUTES, TimeUnit.MINUTES);
        scheduledEnergy = executor.scheduleAtFixedRate(() -> updateEnergy.get().run(), remainingTime, INTERVAL_MINUTES, TimeUnit.MINUTES);
        scheduledMood = executor.scheduleAtFixedRate(() -> updateMood.get().run(), remainingTime, INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private long checkedMissedExecutions() {
        // Retrieve last update time from preferences
        String lastUpdateTime = prefs.get(LAST_UPDATE_TIME, null);
        System.out.println("Status bar was last updated at " + lastUpdateTime);

        // If there exists a 'Last Update Time', then execute the missed executions on application boot
        if(lastUpdateTime != null) {
            // Parse the 'Last Update Time' (of string) into 'LocalDateTime'
            LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateTime, timeFormatter);
            LocalDateTime now = LocalDateTime.now();

            // Calculate the missed intervals
            // E.g. if 120 minutes have passed, 120/60 means that there were 2 missed intervals
            long missedIntervals = ChronoUnit.MINUTES.between(lastUpdate, now) / 60;

            // Calculates the additional time that has passed between the last time and now
            // MOD the time interval to get the REMAINDER
            long extraTime = ChronoUnit.MINUTES.between(lastUpdate, now) % INTERVAL_MINUTES;

            if(missedIntervals > 0) {
                System.out.println("Missed " + missedIntervals + " executions, running them now...");
                for(int i = 0; i < missedIntervals; i++) {
                    runBarUpdate();
                }
            }

            // The next execution should begin at the next expected time
            System.out.println("Scheduling next task in " + (INTERVAL_MINUTES - extraTime) + " minutes");
            return INTERVAL_MINUTES - extraTime;
        }

        // If no previous executions, start immediately
        return 0;
    }

    private void runBarUpdate() {
        // These methods run when the squid is sleeping
        if(isSleepButtonOn) {
            actions.updateStatusBar(fullness, -1, Squid.SLEEPING);
            actions.updateStatusBar(energy, 1, Squid.SLEEPING);
            actions.updateStatusBar(mood, -1, Squid.SLEEPING);
        }

        else {
            actions.updateStatusBar(fullness, -1, Squid.IDLE);
            actions.updateStatusBar(energy, -1, Squid.IDLE);
            actions.updateStatusBar(mood, -1, Squid.IDLE);
        }
    }

    // Cancels ALL previous tasks in order to run new ones
    private void restartTasks() {
        // If tasks are still running (not null) then cancel them in order to start new tasks
        if(scheduledFullness != null && scheduledEnergy != null && scheduledMood != null) {
            // Cancel all running tasks
            scheduledFullness.cancel(false);
            scheduledEnergy.cancel(false);
            scheduledMood.cancel(false);
        }

        // Start new tasks
        startScheduledTasks(INTERVAL_MINUTES); // Restart with default interval
    }

    private void startCleanlinessTimer(long remainingTime) {
        cleanlinessTimer = new Timer();
        remainingTimeUntilDirty = remainingTime;

        cleanlinessTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // If the squid is clean AND idle, continue running the task
                if (prefs.getBoolean(IS_CLEAN, true) && prefs.getInt(CURRENT_STATE, Squid.IDLE) == Squid.IDLE) {
                    System.out.println("Squid will be dirty in " + remainingTimeUntilDirty + " seconds");
                    // Deduct the remaining time by a second
                    remainingTimeUntilDirty -= 1000;
                }

                if(remainingTimeUntilDirty <= 0) {
                    System.out.println("Squid is Dirty");

                    // Save value into preferences
                    // After the timer, the squid becomes dirty
                    prefs.putBoolean(IS_CLEAN, false);

                    // Update squid sprite
                    // Change the squid sprite to the dirty sprite
                    actions.updateCleanlinessState(prefs.getBoolean(IS_CLEAN, true), prefs.getInt(CURRENT_STATE, Squid.IDLE));

                    // Cancel the timer
                    cleanlinessTimer.cancel();
                }
            }
            // Checks at every 1 second interval
        }, 0, 1000);
    }

    private void resetCleanlinessTimer() {
        if(cleanlinessTimer != null) {
            cleanlinessTimer.cancel();
        }

        // Restart scheduled task and run at default interval
        startCleanlinessTimer(ONE_DAY_MS);
    }
}