import javax.swing.*;

public class Actions {
    Squid squid = new Squid();

    private void updateStatusBar(JLabel statusBar) {

    }

    public void sleep(boolean isButtonON) {
        if(isButtonON) {
            // Changing the user interface to the dark theme
            GamePanel.exit.setIcon(new ImageIcon("src/assets/dark-exit.PNG"));
            GamePanel.minimize.setIcon(new ImageIcon("src/assets/dark-minimize.PNG"));
            GamePanel.sleepButton.setIcon(new ImageIcon("src/assets/dark-sleep.PNG"));
            GamePanel.eatButton.setIcon(new ImageIcon("src/assets/dark-eat.PNG"));
            GamePanel.playButton.setIcon(new ImageIcon("src/assets/dark-play.PNG"));
            GamePanel.batheButton.setIcon(new ImageIcon("src/assets/dark-bathe.PNG"));

            Icon sunnySleeping = new ImageIcon("src/assets/darksunny-sleeping.GIF");
            GamePanel.sunnyTheSquid.setIcon(sunnySleeping);

            GamePanel.statusBlock.setIcon(new ImageIcon("src/assets/dark-statusbox.PNG"));

            GamePanel.backdrop.setIcon(new ImageIcon("src/assets/dark-backdrop.PNG"));

            // Bar placeholders
            GamePanel.fullnessBar.setIcon(new ImageIcon("src/assets/darkbar-5.PNG"));
            GamePanel.energyBar.setIcon(new ImageIcon("src/assets/darkbar-2.PNG"));
            GamePanel.moodBar.setIcon(new ImageIcon("src/assets/darkbar-4.PNG"));

            // Sets the current state of 'Squid' object
            squid.setCurrentState(Squid.SLEEPING);
        }

        else {
            // Changes the UI back to a light theme
            GamePanel.exit.setIcon(new ImageIcon("src/assets/exit.PNG"));
            GamePanel.minimize.setIcon(new ImageIcon("src/assets/minimize.PNG"));
            GamePanel.sleepButton.setIcon(new ImageIcon("src/assets/sleep.PNG"));
            GamePanel.eatButton.setIcon(new ImageIcon("src/assets/eat.PNG"));
            GamePanel.playButton.setIcon(new ImageIcon("src/assets/play.PNG"));
            GamePanel.batheButton.setIcon(new ImageIcon("src/assets/bathe.PNG"));

            Icon sunny = new ImageIcon("src/assets/sunny.GIF");
            GamePanel.sunnyTheSquid.setIcon(sunny);

            GamePanel.statusBlock.setIcon(new ImageIcon("src/assets/statusbox.PNG"));

            GamePanel.backdrop.setIcon(new ImageIcon("src/assets/backdrop.PNG"));

            // Bar placeholders
            GamePanel.fullnessBar.setIcon(new ImageIcon("src/assets/bar-5.PNG"));
            GamePanel.energyBar.setIcon(new ImageIcon("src/assets/bar-2.PNG"));
            GamePanel.moodBar.setIcon(new ImageIcon("src/assets/bar-4.PNG"));

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

    /* This method records the system time when the game is first booted up
     * When time passes in real life, it will reflect on Sunny's stats
     * Per hour that you are away from Sunny, his Fullness, Energy, and Mood decreases by ONE point
     * returns - elapsed time */
    public long recordGameTime(long startTime) {
        return 0;
    }
}
