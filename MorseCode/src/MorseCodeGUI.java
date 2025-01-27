import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class MorseCodeGUI extends JFrame implements KeyListener{

    private JTextArea inputTextArea, morseCodeTextArea;
    private MorseBackend backend;

    public MorseCodeGUI() {
        super("Morse code translator");

        setSize(new Dimension(540, 760));
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#3d3d29"));    // így lehet beállítani a JFrame hátterét
        setLocationRelativeTo(null);

        backend = new MorseBackend();

        addGuiComponents();
    }

    private void addGuiComponents(){
        JLabel titleLabel = new JLabel("Morse code translator");
        titleLabel.setFont(new Font("Old English Text MT", Font.PLAIN, 37));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0,0,540, 100);

        // text input
        JLabel textinputLabel = new JLabel("Text: ");
        textinputLabel.setFont(new Font("Old English Text MT", Font.PLAIN, 22));
        textinputLabel.setForeground(Color.white);
        textinputLabel.setBounds(20, 100, 200, 30);

        inputTextArea = new JTextArea();
        inputTextArea.setFont(new Font("Old English Text MT", Font.PLAIN, 22));
        inputTextArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.addKeyListener(this);

        JScrollPane scrollInput = new JScrollPane(inputTextArea);
        scrollInput.setBounds(20, 132, 484, 200);

        // morse output
        JLabel morseLabel = new JLabel("Morse: ");
        morseLabel.setFont(new Font("Old English Text MT", Font.PLAIN, 22));
        morseLabel.setForeground(Color.white);
        morseLabel.setBounds(20, 368, 200, 30);
        add(morseLabel);

        morseCodeTextArea = new JTextArea();
        morseCodeTextArea.setFont(new Font("Old English Text MT", Font.PLAIN, 22));
        morseCodeTextArea.setEditable(false);
        morseCodeTextArea.setLineWrap(true);
        morseCodeTextArea.setWrapStyleWord(true);
        morseCodeTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane morseSCroll = new JScrollPane(morseCodeTextArea);
        morseSCroll.setBounds(20, 400, 484, 200);

        JButton playButton = new JButton("Play sound");
        playButton.setBounds(210, 680, 150, 30);
        playButton.setFont(new Font("Old English Text MT", Font.PLAIN, 22));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButton.setEnabled(false);
                Thread playMorseCodeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] morseCodeMessage = morseCodeTextArea.getText().split(" ");
                        backend.playSound(morseCodeMessage);
                        playButton.setEnabled(true);
                    }                    
                });
                playMorseCodeThread.start();
            }
        });

        
        add(titleLabel);
        add(textinputLabel);
        add(scrollInput);
        add(morseSCroll);
        add(playButton);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() != KeyEvent.VK_SHIFT){
            String inputText = inputTextArea.getText();
            morseCodeTextArea.setText(backend.translateToMorse(inputText));
        }
    }

}
