import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;

public class Dino extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 750;
    int boardHeight = 250;

    //images
    Image dinoImage;
    Image deadDinoImage;
    Image dinoJumpImage;
    Image cactus1Image;
    Image cactus2Image;
    Image cactus3Image;

    public class Block {
        int x, y, width, height;
        Image img;

        public Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }        
    }

    // dino -> character class
    int dinoWidth = 88;
    int dinoHeight = 94;
    int dinoX = 50;
    int dinoY = boardHeight - dinoHeight;

    Block dinosaur;
    Timer gameLoop;
    Timer placeCactusTimer;
    boolean gameOver = false;
    int score = 0;

    //cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;
    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArrayList;

    // game physics
    int veloX = -12;
    int veloY = 0;
    int gravity = 1;

    public Dino() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        dinoImage = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        deadDinoImage = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinoJumpImage = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Image = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Image = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Image = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();

        dinosaur = new Block(dinoX, dinoY, dinoWidth, dinoHeight, dinoImage);
        cactusArrayList = new ArrayList<Block>();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }

    public void placeCactus(){
        if (gameOver) {
            return;
        }
        double placeCactusChance = Math.random();
        if(placeCactusChance > .90){    // 10% chance
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Image);
            cactusArrayList.add(cactus);
        }
        else if(placeCactusChance > .70){   // 20% chance
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Image);
            cactusArrayList.add(cactus);
        }
        else if(placeCactusChance > .50){   // 20% chance
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Image);
            cactusArrayList.add(cactus);
        }

        if(cactusArrayList.size() > 10){
            cactusArrayList.remove(0);  // remove the 1st cactus
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){        
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);
        for (int i = 0; i < cactusArrayList.size(); i++) {
            Block cactus = cactusArrayList.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 24));
        if(gameOver){
            g.drawString("GAME OVER " + String.valueOf(score), 10, 35);
        }
        else{
            g.drawString("SCORE " + String.valueOf(score), 10, 35);
        }
    }

    public void move(){
        veloY += gravity;
        dinosaur.y += veloY;

        if(dinosaur.y > dinoY){
            dinosaur.y = dinoY;
            veloY = 0;
            dinosaur.img = dinoImage;
        }
        for (int i = 0; i < cactusArrayList.size(); i++) {
            Block cactus = cactusArrayList.get(i);
            cactus.x += veloX;
            if(collision(dinosaur, cactus)){
                gameOver = true;
                dinosaur.img = deadDinoImage;
            }
        }
        score++;
    }

    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width &&
            a.x + a.width > b.x &&
            a.y < b.y + b.height &&
            a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
            placeCactusTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_O){
            //System.out.println("JUMP");
            if(dinosaur.y == dinoY){
                veloY = -17;
                dinosaur.img = dinoJumpImage;
            }
        }
        if(gameOver){
            // restart game
            dinosaur.y = dinoY;
            dinosaur.img = dinoImage;
            veloY = 0;
            cactusArrayList.clear();
            score = 0;
            gameOver = false;
            gameLoop.start();
            placeCactusTimer.start();
        }
    }

    // not used, but necessary
    //
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
