import javax.swing.*;

// Sunny's fullness increases when he is EATING
// It decreases when he is PLAYING or when he is IDLE

// Sunny's energy increases when he is SLEEPING
// It decreases when he is PLAYING

// Sunny's mood increases when he is PLAYING
// It decreases when he is IDLE
public class StatusBar extends JLabel {
    private int statValue;

    public StatusBar() {
        super();

        statValue = Squid.DEFAULT_STAT_VALUE;
        setIcon(new ImageIcon("src/assets/bar-5.PNG"));

    }

    public int getStatValue() {
        return this.statValue;
    }

    public void setStatValue(int statValue) {
        this.statValue = statValue;
    }
}
