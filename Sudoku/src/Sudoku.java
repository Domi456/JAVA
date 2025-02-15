import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sudoku {
    class Tile extends JButton{
        int row;
        int col;
        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    int boardWidth = 600;
    int boardHegith = 650;

    JFrame board = new JFrame("Sudoku");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    String[] puzzle = {
        "--74916-5",
        "2---6-3-9",
        "-----7-1-",
        "-586----4",
        "--3----9-",
        "--62--187",
        "9-4-7---2",
        "67-83----",
        "81--45---"
    };

    String[] solution = {
        "387491625",
        "241568379",
        "569327418",
        "758619234",
        "123784596",
        "496253187",
        "934176852",
        "675832941",
        "812945763"
    };

    public Sudoku() {
        board.setSize(boardWidth, boardHegith);
        board.setResizable(false);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setLayout(new BorderLayout());
        board.setVisible(true);

        textLabel.setFont(new Font("Consolas", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku: 0");

        textPanel.add(textLabel);
        board.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9, 9));

        setupTiles();
        board.add(boardPanel, BorderLayout.CENTER);
    }

    private void setupTiles(){
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tile tile = new Tile(r, c);
                char tileChar = puzzle[r].charAt(c);
                tile.setText(String.valueOf(tileChar));
                boardPanel.add(tile);
                System.out.println("Added tile: " + tileChar);
            }
        }
    }

}
