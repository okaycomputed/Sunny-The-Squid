public class Squid {
    public static final int IDLE           = 0;    // DEFAULT
    public static final int SLEEPING       = 1;
    public static final int EATING         = 2;
    public static final int PLAYING        = 3;
    public static final int BATHING        = 4;

    public static final int DEFAULT_STAT_VALUE = 5;

    // Sunny's cleanliness increases when he is BATHING (however, this is not reflected in the status bar, it
    // will be reflected in dust particles generating around his sprite)
    // Sunny will turn dirty after being IDLE for more than a day
    private boolean isClean;

    private int currentState;

    public Squid() {
        this.isClean = true;

        // Setting default state
        this.currentState = IDLE;
    }

    public boolean isClean() {
        return this.isClean;
    }

    public void setIsClean(boolean isClean) {
        this.isClean = isClean;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(int state) {
        this.currentState = state;
    }
}
