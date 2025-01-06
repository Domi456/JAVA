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

    // game physics
    int veloY = 0;

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

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){        
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_O){
            System.out.println("JUMP");
        }
    }

    // not used, but necessary
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
