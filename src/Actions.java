import javax.swing.*;
import java.time.LocalDateTime;

public class Actions {
    Squid squid = new Squid();

    // The frames per second for the animation
    int fps = 3;

    // The amount of time in between each frame - in this case it's 333 ms per frame
    int delay = 1000 / fps;

    // The sprite starts at 0, and the frames will begin to switch from there
    int currFrame = 0;

    final int TOTAL_FRAMES_EAT = 9;
    final int TOTAL_FRAMES_BATHE = 5;

    /* When the 'Sleep' JButton is clicked, it will change the GUI to a dark version and change the sprite of the squid.
     * It will then set the current state of the Squid object accordingly
     * @param isButtonON  - Checks if the 'Sleep' JButton is switched on
     * @param fullness    - 'Fullness' Status bar in order to update the StatusBar object
     * @param energy      - 'Energy' Status bar in order to update the StatusBar object
     * @param mood        - 'Mood' Status bar in order to update the StatusBar object */
    public void sleep(boolean isButtonON, StatusBar fullness, StatusBar energy, StatusBar mood) {
        if(isButtonON) {
            // Changing the user interface to the dark theme
            GameInterface.exit.setIcon(new ImageIcon("src/assets/dark-exit.PNG"));
            GameInterface.minimize.setIcon(new ImageIcon("src/assets/dark-minimize.PNG"));
            GameInterface.sleepButton.setIcon(new ImageIcon("src/assets/dark-sleep.PNG"));
            GameInterface.eatButton.setIcon(new ImageIcon("src/assets/dark-eat.PNG"));
            GameInterface.playButton.setIcon(new ImageIcon("src/assets/dark-play.PNG"));
            GameInterface.batheButton.setIcon(new ImageIcon("src/assets/dark-bathe.PNG"));

            Icon sunnySleeping = new ImageIcon("src/assets/darksunny-sleeping.GIF");
            GameInterface.sunnyTheSquid.setIcon(sunnySleeping);

            GameInterface.statusBlock.setIcon(new ImageIcon("src/assets/dark-statusbox.PNG"));

            GameInterface.backdrop.setIcon(new ImageIcon("src/assets/dark-backdrop.PNG"));

            fullness.setIcon(new ImageIcon("src/assets/darkbar-" + fullness.getStatValue() + ".PNG"));
            energy.setIcon(new ImageIcon("src/assets/darkbar-" + energy.getStatValue() + ".PNG"));
            mood.setIcon(new ImageIcon("src/assets/darkbar-" + mood.getStatValue() + ".PNG"));

            squid.setCurrentState(Squid.SLEEPING);

        }

        else {
            // Changes the UI back to a light theme
            GameInterface.exit.setIcon(new ImageIcon("src/assets/exit.PNG"));
            GameInterface.minimize.setIcon(new ImageIcon("src/assets/minimize.PNG"));
            GameInterface.sleepButton.setIcon(new ImageIcon("src/assets/sleep.PNG"));
            GameInterface.eatButton.setIcon(new ImageIcon("src/assets/eat.PNG"));
            GameInterface.playButton.setIcon(new ImageIcon("src/assets/play.PNG"));
            GameInterface.batheButton.setIcon(new ImageIcon("src/assets/bathe.PNG"));

            Icon sunny = new ImageIcon("src/assets/sunny.GIF");
            GameInterface.sunnyTheSquid.setIcon(sunny);

            GameInterface.statusBlock.setIcon(new ImageIcon("src/assets/statusbox.PNG"));

            GameInterface.backdrop.setIcon(new ImageIcon("src/assets/backdrop.PNG"));

            fullness.setIcon(new ImageIcon("src/assets/bar-" + fullness.getStatValue() + ".PNG"));
            energy.setIcon(new ImageIcon("src/assets/bar-" + energy.getStatValue() + ".PNG"));
            mood.setIcon(new ImageIcon("src/assets/bar-" + mood.getStatValue() + ".PNG"));

            squid.setCurrentState(Squid.IDLE);
        }

    }

    public void eat(StatusBar fullness, Icon[] sunnyEat) {
        System.out.println("Eat");

        // Plays brief eating animation for Squid
        // Uses a thread to animate the sprite
        new Thread(() -> {
            // Will operate as long as the currFrame is less than the index of the Icon array
            while(currFrame < TOTAL_FRAMES_EAT) {
                // Switch the label
                GameInterface.sunnyTheSquid.setIcon(sunnyEat[currFrame]);

                // Increment currFrame
                currFrame++;

                try {
                    Thread.sleep(delay); // Pause the thread for frame rate
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Play sound after animation completes
            GameInterface.playSfx(GameInterface.BURP);

            // Switch back to the original GIF
            GameInterface.sunnyTheSquid.setIcon(GameInterface.sunnyIdle);

            // Reset the frames for the next animation
            currFrame = 0;

            // Updates status bar for the squid
            updateStatusBar(fullness, 1, Squid.EATING);

        }).start();
    }

    // Playing will increase mood by 2 points and decrease energy by 1 point
    public void play(StatusBar mood, StatusBar energy) {
        System.out.println("Play");

        // Update the mood status bar
        updateStatusBar(mood, 2, Squid.PLAYING);

        // Update the energy status bar
        updateStatusBar(energy, -1, Squid.PLAYING);

    }

    // If the squid is not clean, change the sprite and the status of the squid
    public void updateCleanlinessState(boolean isClean, int currentState) {
        // If the squid is not clean, display the dirty sprite
        if(!isClean && currentState == Squid.IDLE) {
            GameInterface.sunnyTheSquid.setIcon(GameInterface.sunnyDirty);
        }

        // Else, display default idle sprite
        else if (isClean && currentState == Squid.IDLE) {
            GameInterface.sunnyTheSquid.setIcon(GameInterface.sunnyIdle);
        }
    }

    public void bathe(int currentState, boolean isClean, Icon[] sunnyBathe) {
        if(currentState == Squid.BATHING && !isClean) {
            System.out.println("Bathe");

            // Plays brief eating animation for Squid
            // Uses a thread to animate the sprite
            new Thread(() -> {
                // Will operate as long as the currFrame is less than the index of the Icon array
                while(currFrame < TOTAL_FRAMES_BATHE) {
                    // Switch the label
                    GameInterface.sunnyTheSquid.setIcon(sunnyBathe[currFrame]);

                    // Play bathing sound
                    GameInterface.playSfx(GameInterface.SOAP_BUBBLES);

                    // Increment currFrame
                    currFrame++;

                    try {
                        Thread.sleep(delay); // Pause the thread for frame rate
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Play sound after animation completes
                GameInterface.playSfx(GameInterface.GLISTENING);

                // Switch back to the original GIF
                GameInterface.sunnyTheSquid.setIcon(GameInterface.sunnyIdle);

                // Reset the frames for the next animation
                currFrame = 0;

            }).start();
        }
    }

    /* Retrieves the resource file of the status bar according to the number of points
     * @param newValue        - New stat value
     * @param currentState    - Whether the GUI is in dark or light mode
     * @return                - The path of the image file as a String */
    public String getResourcePath(int newValue, int currentState) {
        if(currentState == Squid.SLEEPING) {
            return "src/assets/darkbar-" + newValue +".PNG";
        }

        else {
            return "src/assets/bar-" + newValue + ".PNG";
        }

    }

    /* This method updates the status bar according to the time elapsed in real-time
     * When time passes in real life, it will reflect on Sunny's stats
     * Per hour that you are away from Sunny, his Fullness, Energy, and Mood decreases by ONE point
     * However, actions will increase these stats
     * @param statusBar       - StatusBar object to be updated
     * @param changeValue     - Amount to be changed
     * @param currentState    - State of the Squid object (in order to know which mode the GUI is in) */
    public void updateStatusBar(StatusBar statusBar, int changeValue, int currentState) {
        // Updating UI accordingly (only if possible)
        // Calculating new stat value
        int newValue = statusBar.getStatValue() + changeValue;

        // If the new value is 0, 1, 2, 3, 4, 5, 6, or 7, update accordingly
        // An 'overflow' of stat points will only result in the max value possible (7)
        if(newValue >= 0 && newValue <= 7) {

            // Update stat value in corresponding StatusBar object
            statusBar.setStatValue(newValue);

            // Updating stat value
            statusBar.setIcon(new ImageIcon(getResourcePath(statusBar.getStatValue(), currentState)));

            if(currentState == Squid.SLEEPING) {
                System.out.println("Executing Sleep Task at " + LocalDateTime.now().format(GameInterface.timeFormatter));
                // Whenever this method is called, keep track of the execution time (only for idle/sleep tasks)
                GameInterface.saveExecutionTime();
            }

            else if(currentState == Squid.IDLE){
                System.out.println("Executing Idle Task at " + LocalDateTime.now().format(GameInterface.timeFormatter));
                // Whenever this method is called, keep track of the execution time
                GameInterface.saveExecutionTime();
            }

            else if (currentState == Squid.EATING){
                System.out.println("Executing Eating Task at " + LocalDateTime.now().format(GameInterface.timeFormatter));
                // Not applicable for eating tasks as they are meant to be completed without closing the app
            }
        }

        else if(newValue > 7) {
            statusBar.setStatValue(7);
            statusBar.setIcon(new ImageIcon(getResourcePath(statusBar.getStatValue(), currentState)));
        }
    }
}