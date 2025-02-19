public class Squid {
    public static final int IDLE           = 0;    // DEFAULT
    public static final int SLEEPING       = 1;
    public static final int EATING         = 2;
    public static final int PLAYING        = 3;
    public static final int BATHING        = 4;

    public static final int DEFAULT_STAT_VALUE = 5;

    // Sunny's fullness increases when he is EATING
    // It decreases when he is PLAYING or when he is LEFT ALONE
    private int fullness;

    // Sunny's energy increases when he is SLEEPING
    // It decreases when he is PLAYING
    private int energy;

    // Sunny's mood increases when he is PLAYING
    // It decreases when he is left ALONE
    private int mood;

    // Sunny's cleanliness increases when he is BATHING (however, this is not reflected in the status bar, it
    // will be reflected in dust particles generating around his sprite)
    // Sunny will turn dirty after playing TWICE or being left alone for more than a day
    private boolean isClean;

    private int currentState;

    public Squid() {
        this.fullness = DEFAULT_STAT_VALUE;
        this.energy = DEFAULT_STAT_VALUE;
        this.mood = DEFAULT_STAT_VALUE;
        this.isClean = false;

        // Setting default squid state
        this.currentState = IDLE;
    }

    public int getFullness() {
        return this.fullness;
    }

    public void setFullness (int statPoints) {
        this.fullness = statPoints;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int statPoints) {
        this.energy = statPoints;
    }

    public int getMood() {
        return this.mood;
    }

    public void setMood(int statPoints) {
        this.mood = statPoints;
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
