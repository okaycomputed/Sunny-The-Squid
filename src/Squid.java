public class Squid {
    public static final int SLEEPING       = 0;
    public static final int EATING         = 1;
    public static final int PLAYING        = 2;
    public static final int BATHING        = 3;
    public static final int ALONE          = 4;

    // This is the only circumstance that Sunny can have two states at once
    public static final int SLEEPING_ALONE = 5;

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
    // Sunny's cleanliness decreases when PLAYING or when he is left ALONE
    private int cleanliness;

    private int currentState;

}
