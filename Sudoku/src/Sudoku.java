import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

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
    JPanel buttonsPanel = new JPanel();
    JButton numSelected = null;
    int errors = 0;
    int blank = 0;

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

        textLabel.setFont(new Font("Consolas", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku");

        textPanel.add(textLabel);
        board.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9, 9));
        setupTiles();
        board.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(1, 9));
        setupButtons();
        board.add(buttonsPanel, BorderLayout.SOUTH);

        board.setVisible(true);
    }

    private void setupTiles(){
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tile tile = new Tile(r, c);
                char tileChar = puzzle[r].charAt(c);
                //tile.setBackground(Color.black);
                if(tileChar != '-'){
                    tile.setFont(new Font("Consolas", Font.BOLD, 20));
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(Color.lightGray);
                }
                else{
                    tile.setFont(new Font("Consolas", Font.PLAIN, 20));
                    tile.setBackground(Color.white);
                    blank++;
                }
                boardPanel.add(tile);
                tile.setFocusable(false);
                //System.out.println("Added tile: " + tileChar);
                tile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tile tile = (Tile) e.getSource();
                        int r = tile.row;
                        int c = tile.col;
                        if(numSelected != null){
                            if(tile.getText() != ""){
                                return;
                            }
                            String numSelectedString = numSelected.getText();
                            String tileSolution = String.valueOf(solution[r].charAt(c));
                            if(tileSolution.equals(numSelectedString)){
                                tile.setText(numSelectedString);
                                blank--;
                                //System.out.println(blank);
                                if(blank == 0){
                                    textLabel.setText("Sudoku solved! Errors: " + errors); 
                                }
                            }
                            else{
                                errors++;
                                textLabel.setText("Errors: " + errors);
                            }
                        }
                    }                    
                });
            }
        }       
    }

    private void setupButtons(){
        for (int i = 1; i < 10; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Consolas", Font.PLAIN, 20));
            button.setText(String.valueOf(i));
            button.setFocusable(false);
            button.setBackground(Color.black);
            button.setForeground(Color.white);
            buttonsPanel.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton btn = (JButton) e.getSource();
                    if(numSelected != null){
                        numSelected.setBackground(Color.black);
                    }
                    numSelected = btn;
                    numSelected.setBackground(Color.darkGray); 
                }                
            });
        }
    }

}
