import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 650; // 50 pixel a text panelnek

    // ablak elemei
    JFrame frame = new JFrame("Tic Tac Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    // játéktábla elemei
    JButton[][] buttons = new JButton[3][3];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    boolean gameOver = false;
    int turns = 0;

    public TicTacToe() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // x-re kattintva leáll a program
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.black);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Courier", Font.BOLD, 33));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton tile = new JButton();
                buttons[i][j] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Courier", Font.BOLD, 120));
                tile.setFocusable(false);
                //tile.setText(currentPlayer);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(gameOver) {
                            return;
                        }
                        JButton tile = (JButton)e.getSource();
                        if(tile.getText() == "") {
                            tile.setText(currentPlayer);
                            if(!gameOver){
                                turns++;
                                currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                                textLabel.setText(currentPlayer + "'s turn");
                                checkWinner();
                            }
                        }
                    }
                });
            }
        }
    }

    public void checkWinner(){
        // sorok
        for (int i = 0; i < 3; i++) {
            if(buttons[i][0].getText() == ""){
                continue;
            }
            if(buttons[i][0].getText() == buttons[i][1].getText() && 
            buttons[i][1].getText() == buttons[i][2].getText()) {
                for (int j = 0; j < 3; j++) {
                    setWinner(buttons[i][j]);
                }
                gameOver = true;
                return;
            }
        }

        // oszlopok
        for (int i = 0; i < 3; i++) {
            if(buttons[0][i].getText() == ""){
                continue;
            }
            if(buttons[0][i].getText() == buttons[1][i].getText() && 
            buttons[1][i].getText() == buttons[2][i].getText()) {
                for (int j = 0; j < 3; j++) {
                    setWinner(buttons[j][i]);
                }
                gameOver = true;
                return;
            }
        }

        // átlósan
        if(buttons[0][0].getText() == buttons[1][1].getText() &&
        buttons[1][1].getText() == buttons[2][2].getText() && buttons[0][0].getText() != ""){
            for (int i = 0; i < 3; i++) {
                setWinner(buttons[i][i]);
            }
            gameOver = true;
            return;
        }
        if(buttons[0][2].getText() == buttons[1][1].getText() &&
        buttons[1][1].getText() == buttons[2][0].getText() && buttons[0][2].getText() != ""){
            setWinner(buttons[0][2]);
            setWinner(buttons[2][0]);
            setWinner(buttons[1][1]);
            gameOver = true;
            return;
        }

        if(turns == 9){
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    setTie(buttons[i][j]);
                }
            }
            gameOver = true;
        }
    }

    public void setWinner(JButton button){
        button.setBackground(Color.black);
        button.setForeground(Color.green);
        if(currentPlayer == "X"){
            textLabel.setText("O wins!");
        }
        else{
            textLabel.setText("X wins!");
        }
    }

    public void setTie(JButton tile){
        tile.setForeground(Color.red);
        tile.setBackground(Color.black);
        textLabel.setText("Tie!");
    }
}
