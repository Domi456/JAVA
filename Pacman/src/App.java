import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int rowCount = 21;
        int colCount = 19;
        int tileSize = 32;
        int frameWidth = colCount * tileSize;
        int framHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pacman");
        frame.setSize(frameWidth, framHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Pacman pacmanoPacman = new Pacman();
        frame.add(pacmanoPacman);
        frame.pack();
        pacmanoPacman.requestFocus();
        frame.setVisible(true);
    }
}
