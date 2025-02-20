import javax.swing.*;

public class Actions {
    Squid squid = new Squid();

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
        }

    }

    public void eat() {

    }

    public void play() {

    }

    public void bathe() {

    }

    public String getResourcePath(int statValue, int currentState) {
        if(statValue == 8 && currentState == Squid.IDLE) {
            return "src/assets/bar-0.PNG";
        }

        else if(statValue == 8 && currentState == Squid.SLEEPING) {
            return "src/assets/darkbar-0.PNG";
        }

        else if(currentState == Squid.IDLE) {
            return "src/assets/bar-" + statValue +".PNG";
        }

        else {
            return "src/assets/darkbar-" + statValue + ".PNG";
        }

    }

    /* This method updates the status bar according to the time elapsed in real-time
     * When time passes in real life, it will reflect on Sunny's stats
     * Per hour that you are away from Sunny, his Fullness, Energy, and Mood decreases by ONE point
     * However, actions will increase these stats */
    public void updateStatusBar(StatusBar statusBar, int changeValue, int currentState) {
        // Calculating new stat value
        int newValue = statusBar.getStatValue() + changeValue;

        // Updating UI accordingly
        statusBar.setIcon(new ImageIcon(getResourcePath(newValue, currentState)));

        // Update stat value in corresponding StatusBar object
        statusBar.setStatValue(newValue);
    }
}