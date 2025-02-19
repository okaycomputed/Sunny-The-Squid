import javax.swing.*;

public class Actions {
    Squid squid = new Squid();

    public void sleep(boolean isButtonON) {
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

            // Bar placeholders
            GameInterface.fullnessBar.setIcon(new ImageIcon("src/assets/darkbar-5.PNG"));
            GameInterface.energyBar.setIcon(new ImageIcon("src/assets/darkbar-2.PNG"));
            GameInterface.moodBar.setIcon(new ImageIcon("src/assets/darkbar-4.PNG"));

            // Sets the current state of 'Squid' object
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

            // Bar placeholders
            GameInterface.fullnessBar.setIcon(new ImageIcon("src/assets/bar-5.PNG"));
            GameInterface.energyBar.setIcon(new ImageIcon("src/assets/bar-2.PNG"));
            GameInterface.moodBar.setIcon(new ImageIcon("src/assets/bar-4.PNG"));

            // Sets current state of 'Squid' object
            squid.setCurrentState(Squid.IDLE);
        }

        // System.out.println(squid.getCurrentState());

    }

    public void eat() {

    }

    public void play() {

    }

    public void bathe() {

    }

    /* This method updates the status bar according to the time elapsed in real-time
     * When time passes in real life, it will reflect on Sunny's stats
     * Per hour that you are away from Sunny, his Fullness, Energy, and Mood decreases by ONE point
     * returns - elapsed time */
    public void updateStatusBar(long elapsedTime) {

    }
}