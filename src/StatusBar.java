import javax.swing.*;
import java.util.prefs.Preferences;

// Sunny's fullness increases when he is EATING
// It decreases when he is IDLE

// Sunny's energy increases when he is SLEEPING
// It decreases when he is PLAYING and when he is IDLE

// Sunny's mood increases when he is PLAYING
// It decreases when he is IDLE

// Sunny gets dirty after not opening the app for one whole day

public class StatusBar extends JLabel {

    Preferences prefs;

    private int statValue;
    private String barType;

    /* @param barType - The type of stat as a string, allows to differentiate between all three
                        status bar objects and also serves as the preference key */
    public StatusBar(String barType) {
        // Initializing preferences
        prefs = Preferences.userNodeForPackage(StatusBar.class);

        this.barType = barType;

        // Retrieving stat value from preferences
        this.statValue = prefs.getInt(barType, Squid.DEFAULT_STAT_VALUE);

        setIcon(new ImageIcon("src/assets/bar-" + this.statValue + ".PNG"));

    }

    public int getStatValue() {
        return prefs.getInt(getBarType(), Squid.DEFAULT_STAT_VALUE);
    }

    public void setStatValue(int statValue) {
        // Put new value inside preferences
        prefs.putInt(getBarType(), statValue);
    }

    public String getBarType() {
        return this.barType;
    }

    public void setBarType(String barType) {
        this.barType = barType;
    }
}
