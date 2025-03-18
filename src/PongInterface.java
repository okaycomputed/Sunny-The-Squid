import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PongInterface extends JFrame implements KeyListener, ActionListener {
    public static final int BORDER_HEIGHT  = 390;
    public static final int BORDER_WIDTH   = 320;
    public static final int MINIMUM_X      = 5;

    private JFrame gameInterface;
    private Timer gameTimer;

    int sunnyScore  = 0;
    int playerScore = 0;

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

        // Instantiating paddle and ball object for the game
        playerPaddle = new Paddle(119, 413, playerImg);
        sunnyPaddle = new Paddle(119, 38, sunnyImg);
        pongBall = new Ball(ballImg);

        // Instantiating internal game timer
        gameTimer = new Timer(10, this);

        addGameTitleBar();
        addGameComponents();

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

    public void startGame() {
        gameTimer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerPaddle.movePaddle(-20, WIDTH);
            System.out.println("Left");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerPaddle.movePaddle(20, WIDTH);
            System.out.println("Right");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(playerScore >= 5 || sunnyScore >= 5) {
            gameTimer.stop();
            return;
        }

        // Moves the ball
        pongBall.moveBall();

        // Ball bounces off LEFT and RIGHT borders
        // Checking the X value of the ball to ensure if it is
        // 1. Out of the (lower) bound of the border
        // 2. or out of the (upper) bound of the border
        if(pongBall.getX() <= MINIMUM_X || pongBall.getX() >= BORDER_WIDTH - pongBall.BALL_SIZE) {
            pongBall.bounceHorizontal();
        }
    }
}
