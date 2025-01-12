import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class FontMenu extends JDialog{
    private Notepad source;

    public FontMenu(Notepad source) {
        this.source = source;
        setTitle("Font menu");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(425, 350);
        setLocationRelativeTo(source);
        setModal(true);
        setLayout(null);

        addMenuComponents();
    }

    private void addMenuComponents(){
        addFontChooser();
        addFontStyleChooser();
    }

    private void addFontChooser(){
        JLabel fontLabel = new JLabel("Font: ");
        fontLabel.setBounds(10, 5, 125, 10);
        add(fontLabel);

        JPanel fontPanel = new JPanel();
        fontPanel.setBounds(10, 15, 125, 160);

        JTextField currentFont = new JTextField(source.getTextArea().getFont().getFontName());
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
        fontStylePanel.setBounds(145, 15, 25, 160);
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

        JTextField currentFontStyleField = new JTextField(currentFontStyleText);
        currentFontStyleField.setPreferredSize(new Dimension(125, 25));
        currentFontStyleField.setEditable(false);
        fontStylePanel.add(currentFontStyleField);

        JPanel listOfFontStylesPanel = new JPanel();
        listOfFontStylesPanel.setLayout(new BoxLayout(listOfFontStylesPanel, BoxLayout.Y_AXIS));
        listOfFontStylesPanel.setBackground(Color.white);

        JLabel plainStyle = new JLabel("Plain");
        plainStyle.setFont(new Font("Dialog", Font.PLAIN, 12));
        listOfFontStylesPanel.add(plainStyle);

        JLabel boldStyle = new JLabel("Bold");
        boldStyle.setFont(new Font("Dialog", Font.BOLD, 12));
        listOfFontStylesPanel.add(boldStyle);

        JLabel italicStyle = new JLabel("Italic");
        italicStyle.setFont(new Font("Dialog", Font.ITALIC, 12));
        listOfFontStylesPanel.add(italicStyle);

        JLabel bolditalicStyle = new JLabel("Bold italic");
        bolditalicStyle.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
        listOfFontStylesPanel.add(bolditalicStyle);

        JScrollPane scrollPane = new JScrollPane(listOfFontStylesPanel);
        scrollPane.setPreferredSize(new Dimension(125, 125));
        fontStylePanel.add(scrollPane);

        add(fontStylePanel);

    }

}
