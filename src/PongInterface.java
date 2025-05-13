import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PongInterface extends JFrame implements KeyListener, ActionListener {
    public static final int MINIMUM_X      = 5;
    public static final int MINIMUM_Y      = 36;
    public static final int BORDER_WIDTH   = 320;
    public static final int BORDER_HEIGHT  = 430 - MINIMUM_X;

    private JFrame gameInterface;
    private Timer gameTimer;

    Actions actions = new Actions();

    int sunnyScore  = 0;
    int playerScore = 0;

    boolean leftPressed = false;
    boolean rightPressed = false;

    ImageIcon backdropImg = new ImageIcon("src/assets/backdrop.PNG");
    JLabel backdrop = new JLabel(backdropImg);

    ImageIcon exitImg = new ImageIcon("src/assets/exit.PNG");
    JButton exit = new JButton(exitImg);

    ImageIcon minimizeImg = new ImageIcon("src/assets/minimize.PNG");
    JButton minimize = new JButton(minimizeImg);

    ImageIcon backImg = new ImageIcon("src/assets/back.PNG");
    JButton back = new JButton(backImg);

    ImageIcon boardImg = new ImageIcon("src/assets/pong-board.png");
    JLabel pongBoard = new JLabel(boardImg);

    ImageIcon lineImg = new ImageIcon("src/assets/board-line.png");
    JLabel boardLine = new JLabel(lineImg);

    ImageIcon playerImg = new ImageIcon("src/assets/player-paddle.png");
    ImageIcon sunnyImg = new ImageIcon("src/assets/sunny-paddle.png");
    ImageIcon ballImg = new ImageIcon("src/assets/pong-ball.png");

    ImageIcon gameOverImg = new ImageIcon("src/assets/game-over-banner.png");
    JLabel gameOverBanner = new JLabel(gameOverImg);

    ImageIcon returnImg = new ImageIcon("src/assets/return.PNG");
    JButton returnButton = new JButton(returnImg);

    ImageIcon[] scores = new ImageIcon[6];

    JLabel sunnyScoreVal;
    JLabel playerScoreVal;

    JLabel winnerText = new JLabel("Winner");

    Paddle playerPaddle, sunnyPaddle;
    Ball pongBall;

    public PongInterface(JFrame gameInterface) {
        // Passing reference of main interface into the pong interface
        this.gameInterface = gameInterface;

        // Sets the size of the GUI (in pixels)
        setSize(330, 430);

        // Removing the title bar of the pong interface
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(false);
        setFocusable(true);
        addKeyListener(this);

        // Instantiating score ImageIcons
        for(int i = 0; i < scores.length; i++) {
            scores[i] = new ImageIcon("src/assets/score-" + i + ".png");
        }

        // Instantiating paddle and ball object for the game
        playerPaddle = new Paddle(119, 413, playerImg);
        sunnyPaddle = new Paddle(119, 38, sunnyImg);
        pongBall = new Ball(ballImg);

        // Instantiating internal game timer
        gameTimer = new Timer(8, this);

        addGameTitleBar();
        addEndScreen();
        addGameComponents();
        addScoreValues();

        add(backdrop);

    }

    public void addGameTitleBar() {
        Image cursor = getToolkit().getImage("src/assets/cursor.PNG");
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "Default"));

        // Creating the exit button
        exit.setBounds(291, 7, 32, 22);

        // Programmed so that the exit button will exit the application on click
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exit);

        minimize.setBounds(252, 7,32, 22);

        // Minimize button will minimize application to system tray
        minimize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(JFrame.ICONIFIED);
            }
        });
        add(minimize);

        back.setBounds(5, 7, 32, 22);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                gameInterface.setVisible(true);
            }
        });

        add(back);
    }

    public void addGameComponents() {
        // Adding board layout
        pongBoard.setBounds(5, 36, 320, 390);
        add(pongBoard);

        // Adding ball
        add(pongBall);

        // Adding line that separates the board
        boardLine.setBounds(7, 228, 316, 2);
        add(boardLine);

        // Adding both paddles
        add(playerPaddle);
        add(sunnyPaddle);
    }

    public void addScoreValues() {
        sunnyScoreVal = new JLabel(scores[0]);
        playerScoreVal = new JLabel(scores[0]);

        sunnyScoreVal.setBounds(13, 190, 28, 32);
        playerScoreVal.setBounds(13, 236, 28, 32);

        add(sunnyScoreVal);
        add(playerScoreVal);
    }

    public void addEndScreen() {
        // Game over banner
        gameOverBanner.setBounds(79, 180, 173, 93);
        gameOverBanner.setVisible(false);

        // Return button
        returnButton.setBounds(127, 231, 77, 27);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disposes all the current GUI components
                dispose();
                // Reveals the main screen/home screen
                gameInterface.setVisible(true);
            }
        });
        returnButton.setVisible(false);

        // Text to announce winner
        winnerText.setFont(gameInterface.getFont().deriveFont(Font.PLAIN, 24));
        winnerText.setVisible(false);

        add(winnerText);
        add(returnButton);
        add(gameOverBanner);
    }

    public void findWinner() {
        // Add winning text depending on who won
        if(playerScore == 5) {
            winnerText.setText("Player Wins!");
            winnerText.setBounds(95, 192, 142, 32);
        }

        else {
            winnerText.setText("Sunny Wins!");
            winnerText.setBounds(99, 192, 135, 32);
        }
    }

    public void showEndScreen() {
        winnerText.setVisible(true);
        returnButton.setVisible(true);
        gameOverBanner.setVisible(true);
    }

    public void updateScore(JLabel scoreVal, int newScore) {
        scoreVal.setIcon(scores[newScore]);
    }

    public void startGame() {
        gameTimer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Pauses the game timer as the game has ended
        if(playerScore >= 5 || sunnyScore >= 5) {
            findWinner();
            showEndScreen();

            // Update Sunny's stats
            // Ensure that points are not given if the game is not completed
            actions.play(GameInterface.mood, GameInterface.energy);

            gameTimer.stop();
            return;
        }

        // Moves the ball
        pongBall.moveBall();

        // Shifts paddle left
        // Used boolean values instead of directly assigning the 'movePaddle' method to
        // the key listener to ensure smoothness in movement
        if(leftPressed) {
            playerPaddle.movePaddle(-5);
        }

        // Shifts paddle right
        if(rightPressed) {
            playerPaddle.movePaddle(5);
        }

        // Ball bounces off LEFT and RIGHT borders
        // Checking the X value of the ball to ensure if it is
        // 1. Out of the (lower) bound of the border
        // 2. or out of the (upper) bound of the border
        if(pongBall.getX() <= MINIMUM_X || pongBall.getX() >= BORDER_WIDTH - pongBall.BALL_SIZE) {
            pongBall.bounceHorizontal();
        }

        // Paddle collision logic
        // Checking if the y-axis of the paddle and the ball are touching each other
        if (pongBall.getY() <= sunnyPaddle.getY() + sunnyPaddle.HEIGHT) {

            // Checks if the ball is in bounds of the paddle's x-value
            if (pongBall.getX() + pongBall.BALL_SIZE >= sunnyPaddle.getX() &&
                    pongBall.getX() <= sunnyPaddle.getX() + sunnyPaddle.WIDTH) {
                pongBall.bounceVertical();

                // Places the ball outside the paddle
                pongBall.setLocation(pongBall.getX(), sunnyPaddle.getY() + sunnyPaddle.HEIGHT + 1); // Pushes ball down
            }
        }

        // The bottom of the ball is more than or equals to the paddle's top - Implies collision
        if (pongBall.getY() + pongBall.BALL_SIZE >= playerPaddle.getY() &&
                // Ensures the position was above
                pongBall.getY() + pongBall.BALL_SIZE - pongBall.directionY < playerPaddle.getY()) {

            // Checking the x-axis of the paddle
            if (pongBall.getX() + pongBall.BALL_SIZE >= playerPaddle.getX() &&
                    pongBall.getX() <= playerPaddle.getX() + playerPaddle.WIDTH) {

                pongBall.bounceVertical(); // Bounce when hitting the paddle

                // Ensure that the ball is placed outside the paddle
                pongBall.setLocation(pongBall.getX(), playerPaddle.getY() - pongBall.BALL_SIZE - 1);
            }
        }

        /* If the ball touches any of the vertical bounds, the game will reset
           and points will be awarded to the corresponding winnerText */
        // When the ball is less than the minimum Y value, it indicates that it exceeded the bound in SUNNY'S SIDE
        // So, the player gets a point and the game resets
        if(pongBall.getY() <= MINIMUM_Y) {
            // Increments score
            playerScore++;

            // Updates value on interface
            updateScore(playerScoreVal, playerScore);

            // Resets the ball to the original position
            pongBall.reset();
        }

        // Ball flies off player's side, Sunny gets a point
        else if(pongBall.getY() >= (BORDER_HEIGHT - pongBall.BALL_SIZE)) {
            sunnyScore++;
            updateScore(sunnyScoreVal, sunnyScore);
            pongBall.reset();
        }

        // AI control for Sunny's paddle
        // When the CENTER of Sunny's paddle is MORE than the x-value of the ball,
        // it will move LEFT to reach the ball
        if(sunnyPaddle.getX() + (sunnyPaddle.getWidth()/2) > pongBall.getX()) {
            sunnyPaddle.movePaddle(-1);
        }

        // When it is LESS than the x-value of the ball, it will move RIGHT
        else if(sunnyPaddle.getY() + (sunnyPaddle.getWidth()/2) < pongBall.getX()) {
            sunnyPaddle.movePaddle(1);
        }
    }
}
