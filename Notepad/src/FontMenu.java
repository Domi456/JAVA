import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.JDBCType;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class FontMenu extends JDialog{

    private Notepad source;
    private JTextField currentFont, currentFontStyleField, currentFontSizeField;
    private JPanel currentColorPanel;

    public FontMenu(Notepad source) {
        this.source = source;
        setTitle("Font menu");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(425, 350);
        setLocationRelativeTo(source);
        setModal(true);
        setLayout(null);
        setResizable(false);

        addMenuComponents();
    }

    private void addMenuComponents(){
        addFontChooser();
        addFontStyleChooser();
        fontSizeChanger();
        fontColorChanger();

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(230, 265, 75, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FontMenu.this.dispose();
            }            
        });
        add(cancelButton);

        JButton applyButton = new JButton("Apply");
        applyButton.setBounds(315, 265, 75, 25);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get font type
                String fontType = currentFont.getText();
                int fontStyle;
                switch(currentFontStyleField.getText()){
                    case "Plain":
                        fontStyle = Font.PLAIN;
                        break;
                    case "Bold":
                        fontStyle = Font.BOLD;
                        break;
                    case "Italic":
                        fontStyle = Font.ITALIC;
                        break;
                    default:
                        fontStyle = Font.BOLD | Font.ITALIC;
                        break;
                }
                
                // get font size
                int fontSize = Integer.parseInt(currentFontSizeField.getText());

                // get font color
                Color fontColor = currentColorPanel.getBackground();

                // create font
                Font newFont = new Font(fontType, fontStyle, fontSize);

                source.getTextArea().setFont(newFont);
                source.getTextArea().setForeground(fontColor);

                FontMenu.this.dispose();
            }            
        });
        add(applyButton);
    }

    private void addFontChooser(){
        JLabel fontLabel = new JLabel("Font: ");
        fontLabel.setBounds(10, 5, 125, 10);
        add(fontLabel);

        JPanel fontPanel = new JPanel();
        fontPanel.setBounds(10, 15, 125, 160);

        currentFont = new JTextField(source.getTextArea().getFont().getFontName());
        currentFont.setPreferredSize(new Dimension(125, 25));
        currentFont.setEditable(false);
        fontPanel.add(currentFont);

        JPanel listofFontsPanel = new JPanel();
        listofFontsPanel.setLayout(new BoxLayout(listofFontsPanel, BoxLayout.Y_AXIS));

        listofFontsPanel.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(listofFontsPanel);
        scrollPane.setPreferredSize(new Dimension(125, 125));


        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontnames = ge.getAvailableFontFamilyNames();

        for (String fontName : fontnames) {
            JLabel nameLabel = new JLabel(fontName);
            nameLabel.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    currentFont.setText(fontName);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    nameLabel.setOpaque(true);
                    nameLabel.setBackground(Color.BLUE);
                    nameLabel.setForeground(Color.white);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    nameLabel.setBackground(null);
                    nameLabel.setForeground(null);
                }

                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                
            });
            listofFontsPanel.add(nameLabel);
        }
        fontPanel.add(scrollPane);

        add(fontPanel);
    }

    private void addFontStyleChooser(){
        JLabel fontStyleLabel = new JLabel("Font style: ");
        fontStyleLabel.setBounds(145, 5, 125, 10);
        add(fontStyleLabel);

        JPanel fontStylePanel = new JPanel();
        fontStylePanel.setBounds(145, 15, 125, 160);
        int currentFontStyle = source.getTextArea().getFont().getStyle();
        String currentFontStyleText;
        
        switch (currentFontStyle) {
            case Font.PLAIN:
                currentFontStyleText = "Plain";
                break;
            case Font.BOLD:
                currentFontStyleText = "Bold";
                break;
            case Font.ITALIC:
                currentFontStyleText = "Italic";
                break;
            default:
                currentFontStyleText = "Bold italic";
                break;
        }

        currentFontStyleField = new JTextField(currentFontStyleText);
        currentFontStyleField.setPreferredSize(new Dimension(125, 25));
        currentFontStyleField.setEditable(false);
        fontStylePanel.add(currentFontStyleField);

        JPanel listOfFontStylesPanel = new JPanel();
        listOfFontStylesPanel.setLayout(new BoxLayout(listOfFontStylesPanel, BoxLayout.Y_AXIS));
        listOfFontStylesPanel.setBackground(Color.white);

        // plain button --------------------------------------------------------------------
        JLabel plainStyle = new JLabel("Plain");
        plainStyle.setFont(new Font("Dialog", Font.PLAIN, 12));
        plainStyle.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                currentFontStyleField.setText(plainStyle.getText());
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                plainStyle.setOpaque(true);
                plainStyle.setBackground(Color.blue);
                plainStyle.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                plainStyle.setBackground(null);
                plainStyle.setForeground(null);
            }
            
        });
        listOfFontStylesPanel.add(plainStyle);

        // bold button --------------------------------------------------------------------
        JLabel boldStyle = new JLabel("Bold");
        boldStyle.setFont(new Font("Dialog", Font.BOLD, 12));
        boldStyle.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                currentFontStyleField.setText(boldStyle.getText());
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                boldStyle.setOpaque(true);
                boldStyle.setBackground(Color.blue);
                boldStyle.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boldStyle.setBackground(null);
                boldStyle.setForeground(null);
            }
            
        });
        listOfFontStylesPanel.add(boldStyle);

        // italic button --------------------------------------------------------------------
        JLabel italicStyle = new JLabel("Italic");
        italicStyle.setFont(new Font("Dialog", Font.ITALIC, 12));
        italicStyle.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("italic clicked");
                currentFontStyleField.setText(italicStyle.getText());
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                italicStyle.setOpaque(true);
                italicStyle.setBackground(Color.blue);
                italicStyle.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                italicStyle.setBackground(null);
                italicStyle.setForeground(null);
            }
            
        });
        listOfFontStylesPanel.add(italicStyle);

        // bilditalic button --------------------------------------------------------------------
        JLabel bolditalicStyle = new JLabel("Bold italic");
        bolditalicStyle.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
        bolditalicStyle.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Bolditaslic clicked");
                currentFontStyleField.setText(bolditalicStyle.getText());
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                bolditalicStyle.setOpaque(true);
                bolditalicStyle.setBackground(Color.blue);
                bolditalicStyle.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bolditalicStyle.setBackground(null);
                bolditalicStyle.setForeground(null);
            }
            
        });
        listOfFontStylesPanel.add(bolditalicStyle);

        JScrollPane scrollPane = new JScrollPane(listOfFontStylesPanel);
        scrollPane.setPreferredSize(new Dimension(125, 125));
        fontStylePanel.add(scrollPane);

        add(fontStylePanel);
    }

    private void fontSizeChanger(){
        JLabel fontSizeLabel = new JLabel("Font size: ");
        fontSizeLabel.setBounds(275, 5, 125, 10);
        add(fontSizeLabel);

        JPanel fontSizePanel = new JPanel();
        fontSizePanel.setBounds(275, 15, 125, 160);

        currentFontSizeField = new JTextField(
            Integer.toString(source.getTextArea().getFont().getSize())
        );
        currentFontSizeField.setPreferredSize(new Dimension(125, 25));
        currentFontSizeField.setEditable(false);
        fontSizePanel.add(currentFontSizeField);

        JPanel listofFontSizesPanel = new JPanel();
        listofFontSizesPanel.setLayout(new BoxLayout(listofFontSizesPanel, BoxLayout.Y_AXIS));
        listofFontSizesPanel.setBackground(Color.white);

        for (int i = 8; i <= 72; i+=2) {
            JLabel fontSizeValueLabel = new JLabel(Integer.toString(i));
            fontSizeValueLabel.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    currentFontSizeField.setText(fontSizeValueLabel.getText());
                }

                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {
                    fontSizeValueLabel.setOpaque(true);
                    fontSizeValueLabel.setBackground(Color.blue);
                    fontSizeValueLabel.setForeground(Color.white);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    fontSizeValueLabel.setBackground(null);
                    fontSizeValueLabel.setForeground(null);
                }
                
            });
            listofFontSizesPanel.add(fontSizeValueLabel);
        }
        JScrollPane scrollPane = new JScrollPane(listofFontSizesPanel);
        scrollPane.setPreferredSize(new Dimension(125, 125));
        fontSizePanel.add(scrollPane);

        add(fontSizePanel);
    }

    private void fontColorChanger(){
        currentColorPanel = new JPanel();
        currentColorPanel.setBounds(175, 200, 23, 23);
        currentColorPanel.setBackground(source.getTextArea().getForeground());
        currentColorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(currentColorPanel);

        JButton chooseColorButton = new JButton("Choose color");
        chooseColorButton.setBounds(10, 200, 150, 25);
        chooseColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(null, "Select a color", Color.black);
                currentColorPanel.setBackground(c);
            }            
        });
        add(chooseColorButton);

    }

}
