import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class SussyShooter extends JPanel implements ActionListener,KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean alive=true; //used for aliens
        boolean used=false; //used for bullets

        Block(int x,int y,int width,int height,Image img){
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
            this.img=img;
        }
    }
    //board
    int tileSize=32;
    int rows=16;
    int columns=16;
    int boardWidth=tileSize*columns;
    int boardHeight=tileSize*rows;

    Image shipImg;
    Image tf;
    Image tfb;
    Image tfg;
    Image tfy;
    ArrayList<Image> tfImgArray; 

    //ship
    int swidth=tileSize*2;
    int sheight=tileSize;
    int sx=tileSize*columns/2-tileSize;
    int sy=boardHeight-tileSize*2;
    int svelocityX=tileSize; //ship speed

    //troll face
    ArrayList<Block> trollfaceArray;
    int tfWidth=tileSize*2;
    int tfHeight=tileSize;
    int tfX=tileSize;
    int tfY=tileSize;

    int tfRows=2;
    int tfColumns=3;
    int tfCount=0; //no. of tfs to clap
    int tfVelocityX=1; //troll face movement speed

    //bullets
    ArrayList<Block>bulletArray;
    int bulletWidth=tileSize/8;
    int bulletHeight=tileSize/2;
    int bulletVelocityY=-10; //bullet movement speed

    Block ship;
    Timer gameLoop;
    boolean gameOver=false;
    int score=0;
    int highScore = 0;

    SussyShooter(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        //load images
        shipImg=new ImageIcon(getClass().getResource("./ship.png")).getImage();
        tf=new ImageIcon(getClass().getResource("./tf.png")).getImage();
        tfb=new ImageIcon(getClass().getResource("./tfb.png")).getImage();
        tfg=new ImageIcon(getClass().getResource("./tfg.png")).getImage();
        tfy=new ImageIcon(getClass().getResource("./tfy.png")).getImage();

        tfImgArray=new ArrayList<Image>();
        tfImgArray.add(tf);
        tfImgArray.add(tfb);
        tfImgArray.add(tfg);
        tfImgArray.add(tfy);

        ship=new Block(sx, sy, swidth, sheight, shipImg);
        trollfaceArray=new ArrayList<Block>();
        bulletArray=new ArrayList<Block>();

        gameLoop=new Timer(1000/60,this); //game timer
        createTrollface();
        gameLoop.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        //troll face
        for(int i=0;i<trollfaceArray.size();i++){
            Block trollface=trollfaceArray.get(i);
            if(trollface.alive){
                g.drawImage(trollface.img, trollface.x, trollface.y, trollface.width, trollface.height, null);
            }
        }

        //bullets
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("Score: " + String.valueOf(score), 10, 35);
    }
    public void move(){
        //trollface
        for (int i = 0; i < trollfaceArray.size(); i++) {
            Block trollface = trollfaceArray.get(i);
            if (trollface.alive) {
                trollface.x += tfVelocityX;

                //if troll face touches borders
                if (trollface.x + trollface.width >= boardWidth || trollface.x <= 0) {
                    tfVelocityX *= -1;
                    trollface.x += tfVelocityX*2;

                    //move troll face down by one row
                    for (int j = 0; j < trollfaceArray.size(); j++) {
                        trollfaceArray.get(j).y += tfHeight;
                    }
                }
                if (trollface.y >= ship.y) {
                    gameOver = true;
                }
            }   

        }

        //bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;
            
            //bullet collision with trollface
            for (int j = 0; j < trollfaceArray.size(); j++) {
                Block trollface = trollfaceArray.get(j);
                if (!bullet.used && trollface.alive && detectCollision(bullet, trollface)) {
                    bullet.used = true;
                    trollface.alive = false;
                    tfCount--;
                    score += 100;
                }
            }

        }

        //clear bullets
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
            bulletArray.remove(0); //removes the first element of the array
        }
        
        //next level
        if (tfCount == 0) {
            //increase the number of aliens in columns and rows by 1
            score += tfColumns * tfRows * 100; //bonus points :)
            tfColumns = Math.min(tfColumns + 1, columns/2 -2); //cap at 16/2 -2 = 6
            tfRows = Math.min(tfRows + 1, rows-6);  //cap at 16-6 = 10
            trollfaceArray.clear();
            bulletArray.clear();
            createTrollface();
        }
    }
    public void createTrollface(){
        Random random=new Random();
        for(int r=0;r<tfRows;r++){
            for(int c=0;c<tfColumns;c++){
                int randomImgIndex=random.nextInt(tfImgArray.size());
                Block trollface=new Block(
                    tfX+c*tfWidth,
                    tfY+r*tfHeight,
                    tfWidth,
                    tfHeight,
                    tfImgArray.get(randomImgIndex)
                );
                trollfaceArray.add(trollface);
            }
        }
        tfCount=trollfaceArray.size();
    }
    public boolean detectCollision(Block a,Block b){
        return  a.x < b.x + b.width &&  //a's top left corner doesn't reach b's top right corner
        a.x + a.width > b.x &&  //a's top right corner passes b's top left corner
        a.y < b.y + b.height && //a's top left corner doesn't reach b's bottom left corner
        a.y + a.height > b.y;   //a's bottom left corner passes b's top left corner

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            if (score > highScore) {
                highScore = score;
            }
            gameLoop.stop();
            // Show high score in a pop-up dialog
        JOptionPane.showMessageDialog(this, "Game Over!\nPress any key to continue\nYour score: " + score + "\nHigh Score: " + highScore, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        if(gameOver){ //any key to restart
            ship.x = sx;
            bulletArray.clear();
            trollfaceArray.clear();
            gameOver = false;
            score = 0;
            tfColumns = 3;
            tfRows = 2;
            tfVelocityX = 1;
            createTrollface();
            gameLoop.start();
        }
        else if((e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) && ship.x-svelocityX >=0){
            ship.x -= svelocityX; //move left one tile
        }
        else if((e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D)&& ship.x+swidth+svelocityX <=boardWidth){
            ship.x += svelocityX; //move right one tile
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE){
            Block bullet=new Block(ship.x+swidth*15/32,ship.y,bulletWidth,bulletHeight,null);
            bulletArray.add(bullet);
        }
    }


    
}
