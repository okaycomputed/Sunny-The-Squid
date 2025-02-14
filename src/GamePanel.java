import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePanel extends JFrame {

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

        addCustomComponents();
        addInteractionButtons();
        addStatusBox();
        addSprites();

        // Setting the background of the application
        // ONLY SET THIS AFTER ADDING ALL OTHER COMPONENTS
        JLabel backdrop = new JLabel(loadImage("src/assets/backdrop.PNG"));
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
    public ImageIcon loadImage(String resourcePath) {
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
        JButton exit = new JButton(loadImage("src/assets/exit.PNG"));
        exit.setBounds(291, 7, 32, 22);

        // Programmed so that the exit button will exit the application on click
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exit);

        JButton minimize = new JButton(loadImage("src/assets/minimize.PNG"));
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
        JButton sleepButton = new JButton(loadImage("src/assets/sleep_button.PNG"));
        sleepButton.setBounds(21, 48, 59, 39);
        add(sleepButton);

        // Adding button for 'Eat' action
        JButton eatButton = new JButton(loadImage("src/assets/eat_button.PNG"));
        eatButton.setBounds(97, 48, 59, 39);
        add(eatButton);

        // Adding button for 'Play' action
        JButton playButton = new JButton(loadImage("src/assets/play_button.PNG"));
        playButton.setBounds(172, 48, 59, 39);
        add(playButton);

        // Adding button for 'Bathe' action
        JButton batheButton = new JButton(loadImage("src/assets/bathe_button.PNG"));
        batheButton.setBounds(248, 48, 59, 39);
        add(batheButton);
    }

    public void addStatusBox() {
        // Adding Sunny's name in the status box
        JLabel sunnyText = new JLabel("Sunny");
        sunnyText.setBounds(135, 325, 60, 24);

        // Deriving the font from the 'getFont' method
        Font sunnyFont = getFont().deriveFont(Font.PLAIN, 20);

        // Setting custom font and adding to user-interface
        sunnyText.setFont(sunnyFont);
        add(sunnyText);

        // Adding Sunny's 'Fullness' indicator
        // Adding text
        JLabel fullnessText = new JLabel("Fullness");
        fullnessText.setBounds(27, 347, 63, 17);

        // Deriving font (for all status conditions)
        Font statusFont = getFont().deriveFont(Font.PLAIN,15);

        fullnessText.setFont(statusFont);
        add(fullnessText);

        // Adding icon
        JLabel fullnessIcon = new JLabel(loadImage("src/assets/hunger.PNG"));
        fullnessIcon.setBounds(87, 344, 26, 26);
        add(fullnessIcon);

        // Adding indicator bar
        JLabel fullnessBar = new JLabel(loadImage("src/assets/bar-6.PNG"));
        fullnessBar.setBounds(115, 349, 186, 15);
        add(fullnessBar);

        // Adding Sunny's 'Energy' indicator
        // Adding text
        JLabel energyText = new JLabel("Energy");
        energyText.setFont(statusFont);
        energyText.setBounds(27, 370, 57, 17);
        add(energyText);

        // Adding icon
        JLabel energyIcon = new JLabel(loadImage("src/assets/energy.PNG"));
        energyIcon.setBounds(85, 366, 26, 26);
        add(energyIcon);

        // Adding indicator bar
        JLabel energyBar = new JLabel(loadImage("src/assets/bar-2.PNG"));
        energyBar.setBounds(115, 372, 186, 15);
        add(energyBar);

        // Adding Sunny's 'Mood' indicator
        // Adding text
        JLabel moodText = new JLabel("Mood");
        moodText.setFont(statusFont);
        moodText.setBounds(27, 393, 57, 17);
        add(moodText);

        // Adding icon
        JLabel moodIcon = new JLabel(loadImage("src/assets/mood.PNG"));
        moodIcon.setBounds(85, 389, 26, 26);
        add(moodIcon);

        // Adding indicator bar
        JLabel moodBar = new JLabel(loadImage("src/assets/bar-5.PNG"));
        moodBar.setBounds(115, 395, 186, 15);
        add(moodBar);

        // Adding the status box that displays Sunny's fullness, energy, and mood
        JLabel statusBlock = new JLabel(loadImage("src/assets/statusblock.PNG"));
        statusBlock.setBounds(17, 323, 296, 97);
        add(statusBlock);
    }

    public void addSprites() {
        // Adding Sunny
        Icon sunnyGif = new ImageIcon("src/assets/sunny.GIF");
        JLabel sunnyTheSquid = new JLabel(sunnyGif);
        sunnyTheSquid.setBounds(32,87, 267, 280);

        // Change the cursor when hovering over this component
        Image petting = getToolkit().getImage("src/assets/petting.PNG");
        sunnyTheSquid.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(petting, new Point(0, 0), "Petting"));

        add(sunnyTheSquid);
    }
}
