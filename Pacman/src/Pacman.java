import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.awt.event.*;

public class Pacman extends JPanel implements ActionListener, KeyListener{
    public class Block {
        int x, y, width, height, startX, startY;
        Image img;
        char direction = 'U';
        int veloX = 0;
        int veloY = 0;
        
        // ctor
        public Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    private int rowCount = 21;
    private int colCount = 19;
    private int tileSize = 32;
    private int panelWidth = colCount * tileSize;
    private int panelHeight = rowCount * tileSize;

    // ghosts
    private Image wallImage;
    private Image blueGhostImage;
    private Image redGhostImage;
    private Image pinkGhostImage;
    private Image orangeGhostImage;

    // player
    private Image playerUPImage;
    private Image playerDownImage;
    private Image playerLeftImage;
    private Image playerRightImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    Timer gameLoop;

    // ctor
    public Pacman() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        wallImage = new ImageIcon(getClass().getResource("./img/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./img/blueGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./img/redGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./img/pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./img/orangeGhost.png")).getImage();
        playerUPImage = new ImageIcon(getClass().getResource("./img/pacmanUp.png")).getImage();
        playerDownImage = new ImageIcon(getClass().getResource("./img/pacmanDown.png")).getImage();
        playerLeftImage = new ImageIcon(getClass().getResource("./img/pacmanLeft.png")).getImage();
        playerRightImage = new ImageIcon(getClass().getResource("./img/pacmanRight.png")).getImage();

        loadMap();
        System.out.println("walls: " + walls.size());
        System.out.println("ghosts: " + ghosts.size());
        System.out.println("foods: " + foods.size());
        gameLoop = new Timer(50, this);     // 20fps, this => ActionListener
        gameLoop.start();
    
    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                String row = tileMap[i];
                char tileMapChar = row.charAt(j);

                int x = j * tileSize;
                int y = i * tileSize;

                if(tileMapChar == 'X'){
                    Block wall = new Block(x, y, tileSize, tileSize, wallImage);
                    walls.add(wall);
                }
                else if(tileMapChar == 'b'){
                    Block ghost = new Block(x, y, tileSize, tileSize, blueGhostImage);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'p'){
                    Block ghost = new Block(x, y, tileSize, tileSize, pinkGhostImage);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'o'){
                    Block ghost = new Block(x, y, tileSize, tileSize, orangeGhostImage);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'r'){
                    Block ghost = new Block(x, y, tileSize, tileSize, redGhostImage);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'P'){
                    pacman = new Block(x, y, tileSize, tileSize, playerRightImage);
                }
                else if(tileMapChar == ' '){
                    Block food = new Block(x + 14, y + 14, 4, 4, null);
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(pacman.img ,pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts){
            g.drawImage(ghost.img, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for (Block wall : walls){
            g.drawImage(wall.img, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.white);
        for (Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("KeyEvent: " + e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
}
