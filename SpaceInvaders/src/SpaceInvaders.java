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

    Block ship;

    public SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);

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

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImage);
    }

    //@Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }



    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

}
