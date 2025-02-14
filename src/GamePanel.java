import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

        addGuiComponents();
    }

    public void addGuiComponents() {
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

        // Adding the status box below to display Sunny's hunger, energy, and mood
        JLabel statusBlock = new JLabel(loadImage("src/assets/statusblock.PNG"));
        statusBlock.setBounds(17, 323, 296, 97);
        add(statusBlock);

        // Adding Sunny
        Icon sunnyGif = new ImageIcon("src/assets/sunny.GIF");
        JLabel sunnyTheSquid = new JLabel(sunnyGif);
        sunnyTheSquid.setBounds(32,87, 267, 280);

        // Change the cursor when hovering over this component
        Image petting = getToolkit().getImage("src/assets/petting.PNG");
        sunnyTheSquid.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(petting, new Point(0, 0), "Petting"));

        add(sunnyTheSquid);

        // Setting the background of the application
        // ONLY SET THIS AFTER ADDING ALL OTHER COMPONENTS
        JLabel backdrop = new JLabel(loadImage("src/assets/backdrop.PNG"));
        add(backdrop);

    }

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
}
