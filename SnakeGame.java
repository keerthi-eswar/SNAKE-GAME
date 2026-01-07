import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 1000;
    private final int HEIGHT = 1000;
    private final int ALL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    private final int[] x = new int[ALL_TILES];
    private final int[] y = new int[ALL_TILES];
    private int snakeLength;
    private int fruitX, fruitY;
    private char direction = 'R';
    private boolean running = true;
    private Timer timer;
    private int score = 0;

    private JButton restartButton;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                }
            }
        });
        initGame();
    }

    private void initGame() {
        score = 0;
        snakeLength = 10;
        direction = 'R';
        running = true;

        for (int i = 0; i < snakeLength; i++) {
            x[i] = 1000 - i * TILE_SIZE;
            y[i] = 1000;
        }

        spawnFruit();
        timer = new Timer(100, this);
        timer.start();

        if (restartButton != null) {
            remove(restartButton);
            restartButton = null;
        }
    }

    private void spawnFruit() {
        Random rand = new Random();
        fruitX = rand.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        fruitY = rand.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'L':
                x[0] -= TILE_SIZE;
                break;
            case 'R':
                x[0] += TILE_SIZE;
                break;
            case 'U':
                y[0] -= TILE_SIZE;
                break;
            case 'D':
                y[0] += TILE_SIZE;
                break;
        }
    }

    private void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
            showRestartButton();
        }
    }

    private void checkFruit() {
        if (x[0] == fruitX && y[0] == fruitY) {
            snakeLength++;
            score += 10;
            spawnFruit();
        }
    }

    private void showRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2, 100, 30);
        restartButton.addActionListener(e -> initGame());
        setLayout(null);
        add(restartButton);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            // Draw fruit
            g.setColor(Color.RED);
            g.fillRect(fruitX, fruitY, TILE_SIZE, TILE_SIZE);

            // Draw snake
            for (int i = 0; i < snakeLength; i++) {
                g.setColor(i == 0 ? Color.GREEN : Color.YELLOW);
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + score, 10, 20);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Game Over! Score: " + score, WIDTH / 2 - 100, HEIGHT / 2 - 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFruit();
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}