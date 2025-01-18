import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class PasswordGenerator extends JFrame{

    private PasswordGeneratorBackend backend;

    public PasswordGenerator() {
        super("Password generator");
        backend = new PasswordGeneratorBackend();
        setSize(540, 570);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);        // center
        
        addGUIComponents();
    }

    public void addGUIComponents(){
        JLabel titleLabel = new JLabel("Password generator");

        titleLabel.setFont(new Font("Consolas", Font.PLAIN, 34));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 540, 39);
        add(titleLabel);

        JTextArea outputTextArea = new JTextArea();

        outputTextArea.setEditable(false);
        outputTextArea.setFont(new Font("Dialog", Font.PLAIN, 24));
        
        JScrollPane passwordPane = new JScrollPane(outputTextArea);
        
        passwordPane.setBounds(25, 97, 479, 70);
        passwordPane.setBorder(BorderFactory.createLineBorder(Color.black));
        add(passwordPane);

        JLabel passwordLenghtLabel = new JLabel("Password length: ");

        passwordLenghtLabel.setFont(new Font("Consolas", Font.PLAIN, 19));
        passwordLenghtLabel.setBounds(25, 215, 272, 39);
        add(passwordLenghtLabel);

        JTextArea passwordLengthTextArea = new JTextArea();

        passwordLengthTextArea.setFont(new Font("Consolas", Font.PLAIN, 19));
        passwordLengthTextArea.setBounds(278, 215, 55, 39);
        passwordLengthTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
        add(passwordLengthTextArea);


        // uppercase
        JToggleButton upperCaseToggle = new JToggleButton("Uppercase");
        upperCaseToggle.setBounds(25, 302, 225, 56);
        upperCaseToggle.setFont(new Font("Consolas", Font.PLAIN, 18));
        add(upperCaseToggle);

        // lowercase
        JToggleButton lowerCaseToggle = new JToggleButton("Lowercase");
        lowerCaseToggle.setBounds(282, 302, 225, 56);
        lowerCaseToggle.setFont(new Font("Consolas", Font.PLAIN, 18));
        add(lowerCaseToggle);

        // numbers
        JToggleButton numbersToggle = new JToggleButton("Numbers");
        numbersToggle.setBounds(25, 373, 225, 56);
        numbersToggle.setFont(new Font("Consolas", Font.PLAIN, 18));
        add(numbersToggle);

        // symbols
        JToggleButton symbolsToggle = new JToggleButton("Symbols");
        symbolsToggle.setBounds(282, 373, 225, 56);
        symbolsToggle.setFont(new Font("Consolas", Font.PLAIN, 18));
        add(symbolsToggle);

        // generate
        JButton generateButton = new JButton("Generate");
        generateButton.setFont(new Font("Consolas", Font.PLAIN, 18));
        generateButton.setBounds(155, 461, 222, 41);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(passwordLengthTextArea.getText().length() <= 0){
                        System.out.println("no length was given");
                        return;
                    }
                    boolean anyToggleSelected = upperCaseToggle.isSelected() || lowerCaseToggle.isSelected() || numbersToggle.isSelected() ||    symbolsToggle.isSelected();
                    
                    int passwordLen = Integer.parseInt(passwordLengthTextArea.getText());
                    if(anyToggleSelected){
                        String generatedPassword = backend.generatePassword(passwordLen, upperCaseToggle.isSelected(), lowerCaseToggle.isSelected(), numbersToggle.isSelected(), symbolsToggle.isSelected());
                        outputTextArea.setText(generatedPassword);
                    }
                    else{
                        System.out.println("no parameter was given");
                    }
                }
                catch(NumberFormatException ex){
                    System.out.println("letters were given, instead of numbers");
                }
            }            
        });
        add(generateButton);
    }
}
