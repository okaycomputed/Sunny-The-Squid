import javax.swing.*;

public class Ball extends JLabel {
    int directionX = -2;    // Bounces right/left
    int directionY = 2;   // Bounces up/down

    final int BALL_SIZE = 12;
    final int X_START   = 160;    // Initial X-Coordinate for ball
    final int Y_START   = 221;    // Initial Y-Coordinate for ball

    public Ball (ImageIcon ballIcon) {
        this.setVisible(true);
        this.setIcon(ballIcon);
        this.setBounds(X_START, Y_START, BALL_SIZE, BALL_SIZE);
    }

    public void moveBall() {
        int newX = getX() + directionX;
        int newY = getY() + directionY;
        this.setLocation(newX, newY);

    }

    // Ball will bounce OFF left/right walls
    public void bounceHorizontal() {
        directionX = -directionX;
    }

    // Ball will bounce OFF paddles
    public void bounceVertical() {
        directionY = -directionY;
    }

    public void reset() {
        this.setLocation(X_START, Y_START);
        bounceVertical();
    }

}
