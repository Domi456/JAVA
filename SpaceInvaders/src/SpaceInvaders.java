import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.ImageFilter;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener{

    public class Block {
        int x, y, width, height;
        Image img;
        boolean alive = true;   // aliens
        boolean used = false;   // bullets

        public Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    int tileSize = 32;
    int rows = 16;
    int coloumns = 16;
    int boardWidth = tileSize*coloumns;
    int boardHeight = tileSize*rows;

    Image shipImage;
    Image alien_whiteImage;
    Image alien_cyanImage;
    Image alien_yellowImage;
    Image alien_magentaImage;
    ArrayList<Image> alienImageArray;

    //ship
    int shipWidth = tileSize*2;
    int shipHeight = tileSize;
    int shipX = tileSize*coloumns/2-tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVeloX = tileSize;

    //aliens
    ArrayList<Block> aliensArray;
    int alienWidth = tileSize * 2;
    int alienHeigth = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;
    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0;
    int alienVeloX = 1;

    //bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVeloY = -10;

    int score = 0;
    Block ship;
    Timer gameLoop;
    boolean gameover = false;

    public SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // load images
        shipImage = new ImageIcon(getClass().getResource("./img/ship.png")).getImage();
        alien_whiteImage = new ImageIcon(getClass().getResource("./img/alien.png")).getImage();
        alien_cyanImage = new ImageIcon(getClass().getResource("./img/alien-cyan.png")).getImage();
        alien_yellowImage = new ImageIcon(getClass().getResource("./img/alien-yellow.png")).getImage();
        alien_magentaImage = new ImageIcon(getClass().getResource("./img/alien-magenta.png")).getImage();

        alienImageArray = new ArrayList<Image>();
        alienImageArray.add(alien_cyanImage);
        alienImageArray.add(alien_whiteImage);
        alienImageArray.add(alien_yellowImage);
        alienImageArray.add(alien_magentaImage);

        aliensArray = new ArrayList<Block>();

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImage);
        bulletArray = new ArrayList<Block>();

        gameLoop = new Timer(1000/60, this);
        createAliens();
        gameLoop.start();

        // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // String[] fonts = ge.getAvailableFontFamilyNames();
        // for (String font : fonts) { --> foreach
        //     System.out.println(font);
        // }
    }

    //@Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        //aliens
        for (int i = 0; i < aliensArray.size(); i++) {
            Block alien = aliensArray.get(i);
            if(alien.alive == true){
                //System.out.println("Alien alive");
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
            else if(alien.alive == false){
                //System.out.println("Alien dead");
            }
        }

        //bullet
        g.setColor(Color.orange);
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            if(!bullet.used){
                g.drawRect(bullet.x, bullet.y, bullet.width, bulletHeight);
            }
        }

        //score
        //g.setColor(Color.orange); --> már orange, korábban beálítottam
        g.setFont(new Font("Magneto", Font.PLAIN, 32));
        if(gameover){
            g.drawString("GAME OVER " + String.valueOf(score), 10, 35);
        }
        else{
            g.drawString("SCORE: " + String.valueOf(score), 10, 35);
        }

    }

    public void move(){
        // aliens
        for (int i = 0; i < aliensArray.size(); i++) {
            Block alien = aliensArray.get(i);
            if(alien.alive == true){
                alien.x += alienVeloX;
                if(alien.x + alien.width >= boardWidth || alien.x <= 0){
                    alienVeloX *= -1;
                    alien.x += alienVeloX * 2;
                    //lejjebb mennek egy sort
                    for (int j = 0; j < aliensArray.size(); j++) {
                        aliensArray.get(j).y += alienHeigth;
                    }
                }
                if(alien.y >= ship.y){
                    gameover = true;
                }
            }
        }

        //bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVeloY;

            // ütközés érzékelés
            for (int j = 0; j < aliensArray.size(); j++) {
                Block alien = aliensArray.get(j);
                if(!bullet.used && alien.alive && collision(alien, bullet)){
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }
        // bullet ne legyen eltárolva miután kiment a képből
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
            bulletArray.remove(0);
        }

        // next level
        if(alienCount == 0){
            score += alienColumns * alienRows * 100;
            alienColumns = Math.min(alienColumns + 1, coloumns/2 - 2);
            alienRows += alienRows < 10 ? 1 : 0;
            aliensArray.clear();
            bulletArray.clear();
            alienVeloX = 1;
            createAliens();
        }
    }

    public void createAliens(){
        Random random = new Random();
        for (int i = 0; i < alienRows; i++) {
            for (int j = 0; j < alienColumns; j++) {
                int randomImgIndex = random.nextInt(alienImageArray.size());
                //System.out.println(alienImageArray.size());
                Block alien = new Block(alienX + j * alienWidth, alienY + i * alienHeigth, alienWidth, alienHeigth, alienImageArray.get(randomImgIndex));
                aliensArray.add(alien);                
            }
            alienCount = aliensArray.size();
            //System.out.println(alienCount);
        }
    }

    public boolean collision(Block alien, Block bullet){
        return alien.x < bullet.x + bullet.width && alien.x + alien.width > bullet.x && 
            alien.y < bullet.y + bullet.height && alien.y + alien.height > bullet.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameover){
            gameLoop.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameover){   // bármelyik billentyű
            ship.x = shipX;
            aliensArray.clear();
            bulletArray.clear();
            score = 0;
            alienVeloX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameover = false;
            createAliens();
            gameLoop.start();
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVeloX >= 0){
            ship.x -= shipVeloX;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVeloX <= boardWidth){
            ship.x += shipVeloX;
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            Block bullet = new Block(ship.x + shipWidth * 15/32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

}
