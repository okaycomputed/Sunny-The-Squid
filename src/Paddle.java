import javax.swing.*;

public class Paddle extends JLabel{
    final int HEIGHT  = 11;
    final int WIDTH   = 92;

    public Paddle(int x, int y, ImageIcon paddleIcon) {
        this.setVisible(true);
        this.setIcon(paddleIcon);
        this.setBounds(x, y, WIDTH, HEIGHT);

    }

    public void movePaddle(int moveAmt) {
        // Ensures that the paddle doesn't move out of bound of the pong board
        int minX = PongInterface.MINIMUM_X;
        int maxX = minX + PongInterface.BORDER_WIDTH - WIDTH;

        // Gets the maximum value between
        // (the minimum X) and the minimum value between (X + move amount and the boundary of the board)
        int newX = Math.max(minX, Math.min(getX() + moveAmt, maxX));

        // Sets new location for the paddle
        this.setLocation(newX, getY());
    }

}
