import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    
    private class Cards {
        String value;
        String type;
        
        public Cards(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString(){
            return value + "-" + type;
        }

        public int getValue(){
            if("AJQK".contains(value)){
                if(value == "A"){
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); // 2 - 10
        }

        public boolean isAce(){
            return value == "A";
        }

        public String getImagePath(){
            return "./Cards/" + toString() + ".png";
        }
    }

    ArrayList <Cards> deck;
    Random random = new Random();   // keverés

    // dealer
    Cards hidden;
    ArrayList<Cards> dealerHand;
    int dealerSum;
    int dealerAceCount;

    // player
    ArrayList<Cards> playerHand;
    int playerSum;
    int playerAceCount;

    // window
    int windowWidth = 600;
    int windowHeight = windowWidth;
    int cardWidth = 110;    // ratio: 1/1.4
    int cardHeight = 154;

    JFrame window = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            try{
                // rejtett kártya
                Image hiddenCardImage = new ImageIcon(getClass().getResource("./Cards/BACK.png")).getImage();
                if(!stayButton.isEnabled()){
                    hiddenCardImage = new ImageIcon(getClass().getResource(hidden.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImage, 20, 20, cardWidth, cardHeight, null);

                // dealer hand
                for (int i = 0; i < dealerHand.size(); i++) {
                    Cards card = dealerHand.get(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }

                // player hand
                for (int i = 0; i < playerHand.size(); i++) {
                    Cards card = playerHand.get(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 320, cardWidth, cardHeight, null);
                }

                if(!stayButton.isEnabled()){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY:");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String message = "";
                    if(playerSum > 21){
                        message = "You lose!";
                    }
                    else if (dealerSum > 21){
                        message = "You win!";
                    }
                    else if (dealerSum == playerSum){
                        message = "Tie!";
                    }
                    else if(playerSum > dealerSum){
                        message = "You win!";
                    }
                    else if(dealerSum > playerSum){
                        message = "You lose!";
                    }

                    g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton againButton = new JButton("Another round");
    
    public BlackJack() {
        StartGame();

        window.setVisible(true);
        window.setSize(windowWidth, windowHeight);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        
        window.add(gamePanel);
        hitButton.setFocusable(false);
        stayButton.setFocusable(false);
        againButton.setFocusable(false);
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(againButton);
        againButton.setEnabled(false);
        window.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Cards card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if(reducePlayerAce() > 21){
                    hitButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                againButton.setEnabled(true);

                while (dealerSum < 17) {
                    Cards card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }
                gamePanel.repaint();
            }
        });

        againButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("PLAY AGAIN:");
                deck.clear();
                random = new Random();
                playerHand.clear();
                dealerHand.clear();
                dealerAceCount = 0;
                playerAceCount = 0;
                playerSum = 0;
                dealerSum = 0;
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                againButton.setEnabled(false);
                StartGame();
                //System.out.println(deck.size());
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    public void StartGame(){
        BuildDeck();
        ShuffleDeck();

        dealerHand = new ArrayList<Cards>();
        dealerSum = 0;
        dealerAceCount = 0;

        hidden = deck.remove(deck.size() - 1);
        dealerSum += hidden.getValue();
        dealerAceCount += hidden.isAce() ? 1 : 0;

        Cards card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        System.out.println("DEALER: ");
        System.out.println(hidden);
        System.out.println(dealerHand);
        System.out.println("Sum:" + dealerSum);
        System.out.println("Aces:" + dealerAceCount);

        playerHand = new ArrayList<Cards>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i = 0; i < 2; i++){
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

        System.out.println("PLAYER HAND:");
        System.out.println(playerHand);
        System.out.println("Sum: " + playerSum);
        System.out.println("Aces: " + playerAceCount);
    }

    public void BuildDeck(){
        deck = new ArrayList<Cards>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "H", "S", "D"};

        for(int i = 0; i < types.length; i++){
            for(int j = 0; j < values.length; j++){
                Cards card = new Cards(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK");
        System.out.println(deck);
    }

    public void ShuffleDeck(){
        for(int i = 0; i < deck.size(); i++){
            int j = random.nextInt(deck.size());
            Cards current = deck.get(i);
            Cards randomCard = deck.get(j);

            deck.set(i, randomCard);
            deck.set(j, current);
        }

        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }

    public int reducePlayerAce(){
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce(){
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }
}
