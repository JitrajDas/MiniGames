import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    private class Tile {
        int x;
        int y;
        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize=16;
    Tile snakeHead;//Snake
    ArrayList<Tile> snakeBody;
    Tile food;//Food
    Random random;
    Timer gameLoop; //game logic
    int velocityX;
    int velocityY;
    boolean gameOver=false;
    int highScore = 0; // High score tracker
    SnakeGame(int boardWidth,int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.LIGHT_GRAY);
        addKeyListener(this);
        setFocusable(true);
        snakeHead=new Tile(5, 5);
        snakeBody=new ArrayList<Tile>();
        food=new Tile(10, 10);
        random=new Random();
        placeFood();
        velocityX=0;
        velocityY=1;
        gameLoop=new Timer(100, this);
        gameLoop.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //Food
        g.setColor(Color.ORANGE);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize,true);
        //Snake head
        g.setColor(Color.BLUE);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize,true);
        //Snake body
        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart=snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize,true);
        }
        //Score
        g.setFont(new Font("Sans-Serif",Font.BOLD,16));
        if(gameOver){
            //g.setColor(Color.BLACK);
            //g.drawString("Game Over! Try Again! Ur Score:"+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
            //g.drawString("High Score:" + String.valueOf(highScore), tileSize - 16, tileSize + 20); // Display high score
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString("Score:"+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
    }
    public void placeFood(){
        food.x=random.nextInt(boardWidth/tileSize);
        food.y=random.nextInt(boardHeight/tileSize);
    }
    public boolean collision(Tile tile1,Tile tile2){
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }
    public void move(){
        //eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        //Snake Body
        for(int i=snakeBody.size()-1;i>=0;i--){
            Tile snakePart=snakeBody.get(i);
            if(i==0){
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }
            else{
                Tile prevSnakePart=snakeBody.get(i-1);
                snakePart.x=prevSnakePart.x;
                snakePart.y=prevSnakePart.y;
            }
        }
        //Snake Head
        snakeHead.x+=velocityX;
        snakeHead.y+=velocityY;
         //game over
         for(int i=0;i<snakeBody.size();i++){
            Tile snakePart=snakeBody.get(i);
            //collide with snake head
            if(collision(snakeHead, snakePart)){
                gameOver=true;
            }
        }
        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardWidth || snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight){
            gameOver=true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            if (snakeBody.size() > highScore) { // Update high score
                highScore = snakeBody.size();
            }
            showGameOverDialog(); // Show pop-up dialog for game over
        }
    }
    private void showGameOverDialog() {
        String message = "Game Over! Your Score: " + snakeBody.size() + "\nHigh Score: " + highScore + "\nDo you want to play again?";
        int option = JOptionPane.showConfirmDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Restart the game
            resetGame();
        } else {
            // Exit the game
            System.exit(0);
        }
    }
    private void resetGame() {
        snakeHead = new Tile(5, 5);  // Reset snake head position
        snakeBody.clear();  // Clear snake body
        placeFood();  // Place new food
        velocityX = 0;  // Reset velocity
        velocityY = 1;
        gameOver = false;  // Reset game over flag
        gameLoop.start();  // Start the game loop again
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if((e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) && velocityY!=1){
            velocityX=0;
            velocityY=-1;
        }
        else if((e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) && velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }
        else if((e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }
        else if((e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
        
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}