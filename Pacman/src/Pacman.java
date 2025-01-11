import javax.swing.*;
import java.awt.*;
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
            this.startX = x;
            this.startY = y;
        }

        public void updateDirection(char direction){
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelo();
            this.x += this.veloX;
            this.y += this.veloY;
            for (Block wallBlock : walls) {
                if(collision(this, wallBlock)){
                    this.x -= this.veloX;
                    this.y -= this.veloY;
                    this.direction = prevDirection;
                    updateVelo();
                }
            }
        }

        public void updateVelo(){
            if(this.direction == 'U'){
                this.veloX = 0;
                this.veloY = -tileSize/4;
            }
            else if(this.direction == 'D'){
                this.veloX = 0;
                this.veloY = tileSize/4;
            }
            else if(this.direction == 'L'){
                this.veloX = -tileSize/4;
                this.veloY = 0;
            }
            else if(this.direction == 'R'){
                this.veloX = tileSize/4;
                this.veloY = 0;
            }
        }

        public void reset(){
            this.x = this.startX;
            this.y = this.startY;   
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
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameover = false;

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
        System.out.println("lives: " + lives);
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
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
        g.setFont(new Font("Courier New", Font.PLAIN, 18));
        if(gameover){
            g.drawString("Game over " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else{
            g.drawString("Lives: " + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    public void move(){
        pacman.x += pacman.veloX;
        pacman.y += pacman.veloY;
        // pacman collision
        for (Block wallBlock : walls) {
            if(collision(pacman, wallBlock) || pacman.x <= 0 || pacman.x + pacman.width >= panelWidth){
                pacman.x -= pacman.veloX;
                pacman.y -= pacman.veloY;
                break;
            }
        }

        // ghost collision
        for (Block ghost : ghosts) {
            if(collision(ghost, pacman)){
                lives -= 1;
                if(lives == 0){
                    gameover = true;
                    return;
                }
                resetPositions();
            }
            if(ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D'){
                ghost.updateDirection('U');
            }
            ghost.x += ghost.veloX;
            ghost.y += ghost.veloY;
            for (Block wall : walls) {
                if(collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= panelWidth){
                    ghost.x -= ghost.veloX;
                    ghost.y -= ghost.veloY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }

        }

        // food collison
        Block foodEaten = null;
        for (Block food : foods) {
            if(collision(pacman, food)){
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);
    }

    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    public void resetPositions(){
        pacman.reset();
        pacman.veloX = 0;
        pacman.veloY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {     
        move();
        repaint();
        if(gameover){
            gameLoop.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("KeyEvent: " + e.getKeyCode());
        if(gameover){
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameover = false;
            gameLoop.start();
        }
        //pacman mozgás
        if(e.getKeyCode() == KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }

        if(pacman.direction == 'U'){
            pacman.img = playerUPImage;
        }
        else if(pacman.direction == 'D'){
            pacman.img = playerDownImage;
        }
        else if(pacman.direction == 'L'){
            pacman.img = playerLeftImage;
        }
        else if(pacman.direction == 'R'){
            pacman.img = playerRightImage;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
}
