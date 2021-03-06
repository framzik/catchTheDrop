package ru.khrebtov.catch_the_drop;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GameWindow extends JFrame {

  private static GameWindow gameWindow;
  private static float lastFrameTime;
  private static Image background;
  private static Image drop;
  private static Image gameOver;
  private static float dropLeft = 200;
  private static float dropTop = -100;
  private static float dropV = 200;
  private static int score;

  public static void main(String[] args) throws IOException {
    background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
    drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
    gameOver = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));

    gameWindow = new GameWindow();
    gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    gameWindow.setLocation(200, 100);
    gameWindow.setSize(906, 478);
    gameWindow.setResizable(false);

    lastFrameTime = System.nanoTime();

    GameField gameField = new GameField();
    gameField.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        float dropRight = dropLeft + drop.getWidth(null);
        float dropBottom = dropTop + drop.getHeight(null);
        boolean isDrop = x >= dropLeft && x <= dropRight && y >= dropTop && y <= dropBottom;
        if (isDrop) {
          dropTop = -100;
          dropLeft = (int) (Math.random() * (gameWindow.getWidth() - drop.getWidth(null)));
          dropV = dropV + 20;
          score++;
          gameWindow.setTitle("Score: " + score);
        }
      }
    });
    gameWindow.add(gameField);
    gameWindow.setVisible(true);
  }

  private static void onRepaint(Graphics g) {
    float currentTime = System.nanoTime();
    float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
    lastFrameTime = currentTime;
    dropTop = dropTop + dropV * deltaTime;
    g.drawImage(background, 0, 0, null);
    g.drawImage(drop, (int) dropLeft, (int) dropTop, null);
    if (dropTop > gameWindow.getHeight()) {
      g.drawImage(gameOver, 280, 120, null);
    }
  }

  private static class GameField extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      onRepaint(g);
      repaint();
    }
  }
}
