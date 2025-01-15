import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class Notepad extends JFrame{

    private JFileChooser fileChooser;

    private JTextArea textArea;
    public JTextArea getTextArea() {return textArea;}
    
    private JPanel footer;
    private JLabel wordCount;
    private JLabel charCount;

    private File currentFile;

    private UndoManager undoManager;

    public Notepad() {
        super("Notepad");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // filechooser setup
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src\\assets"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        undoManager = new UndoManager();
        addGuiComponents();
    }

    public void addGuiComponents(){
        textArea = new JTextArea();
        
        addToolbar();
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }            
        });
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateWordCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateWordCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateWordCount();
            }
            
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        wordCount = new JLabel("Words: 0");
        wordCount.setVisible(true);
        wordCount.setHorizontalAlignment(SwingConstants.RIGHT);
        wordCount.setForeground(Color.black);

        charCount = new JLabel("Characters: 0");
        charCount.setVisible(true);
        charCount.setHorizontalAlignment(SwingConstants.LEFT);
        charCount.setForeground(Color.black);

        footer.add(wordCount);
        footer.add(charCount);
        add(footer, BorderLayout.SOUTH);        // add --> (JFrame típusú) frame.add() metódus, nem írom ki teljesen, az 'extends JFrame' miatt     
    }

    public void updateWordCount(){
        String text = textArea.getText().trim();
        int wc = 0;
        if(!text.isEmpty()){
            wc = text.split("\\s+").length;
        }
        int charC = text.length();
        wordCount.setText("Words: " + wc);
        charCount.setText("Characters: " + charC);
    }

    public void addToolbar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);       

        // menu options
        menuBar.add(addFileMenu());
        menuBar.add(addEditMenu());
        menuBar.add(addFormatMenu());
        menuBar.add(addViewMenu());        

        add(toolBar, BorderLayout.NORTH);
    }

    private JMenu addFileMenu(){
        JMenu fileMenu = new JMenu("File");
        // new
        JMenuItem newMenuItem = new JMenuItem("New ctrl+n");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // reset title
                setTitle("Notepad");
                textArea.setText("");
                currentFile = null; 
            }            
        });

        KeyStroke newKeyStroke = KeyStroke.getKeyStroke("control N");
        InputMap newInputMap = textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap newActionMap = textArea.getActionMap();
        newInputMap.put(newKeyStroke, "New");
        newActionMap.put("New", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ctrl+n pressed, new");
                newMenuItem.doClick();
            }            
        });

        fileMenu.add(newMenuItem);
        // open
        JMenuItem openMenuItem = new JMenuItem("Open ctrl+o");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open file explorer
                int result = fileChooser.showOpenDialog(Notepad.this);
                if(result != JFileChooser.APPROVE_OPTION) return;
                newMenuItem.doClick();

                File selectedFile = fileChooser.getSelectedFile();
                currentFile = selectedFile;
                setTitle(selectedFile.getName());

                try{
                    FileReader reader = new FileReader(selectedFile);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    // store text
                    StringBuilder fileText = new StringBuilder();
                    String readText;
                    while ((readText = bufferedReader.readLine()) != null) {
                        fileText.append(readText + "\n");
                    }
                    textArea.setText(fileText.toString());
                    bufferedReader.close();

                }catch(Exception ex2){
                    ex2.printStackTrace();
                }
            }            
        });

        // ctrl + o = open
        KeyStroke openKeyStroke = KeyStroke.getKeyStroke("control O");
        InputMap openinputMap = textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap openactionMap = textArea.getActionMap();
        openinputMap.put(openKeyStroke, "Open");
        openactionMap.put("Open", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ctrl+o pressed, opening...");
                openMenuItem.doClick();
            }            
        });

        fileMenu.add(openMenuItem);
        // save as
        JMenuItem saveAsMenuItem = new JMenuItem("Save as");
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save dialog
                int result = fileChooser.showSaveDialog(Notepad.this);
                if(result != JFileChooser.APPROVE_OPTION) return;
                try{
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    if(!fileName.substring(fileName.length() - 4).equalsIgnoreCase(".txt")){
                        selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");
                    }
                    // create new file
                    selectedFile.createNewFile();
                    FileWriter filewriter = new FileWriter(selectedFile);
                    BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
                    bufferedwriter.write(textArea.getText());
                    bufferedwriter.close();
                    filewriter.close();

                    // headertitle set
                    setTitle(fileName);
                    currentFile = selectedFile;
                    JOptionPane.showMessageDialog(Notepad.this, "Saved file!");

                }catch(Exception ex){
                    ex.getStackTrace();
                }
            }
        });
        fileMenu.add(saveAsMenuItem);
        // save
        JMenuItem saveMenuItem = new JMenuItem("Save ctrl+s");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                if(currentFile == null){
                    saveAsMenuItem.doClick();
                }
                if (currentFile == null) return;
                try{
                    // write to current file
                    FileWriter writer = new FileWriter(currentFile);
                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                    bufferedWriter.write(textArea.getText());
                    bufferedWriter.close();
                    writer.close();
                }catch(Exception ex3){
                    ex3.printStackTrace();
                }
            }
            
        });
        // ctrl + s = save
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke("control S");
        InputMap inputMap = textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = textArea.getActionMap();
        inputMap.put(saveKeyStroke, "Save");
        actionMap.put("Save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ctrl+s pressed, saving...");
                saveMenuItem.doClick();
            }            
        });

        fileMenu.add(saveMenuItem);
        // exit
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Notepad.this.dispose();
            }
            
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu addEditMenu(){
        JMenu editMenu = new JMenu("Edit");
        // undo ------------------------------------------------
        JMenuItem undoMenuItem = new JMenuItem("Undo ctrl+z");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(undoManager.canUndo()){
                    undoManager.undo();
                }
            }            
        });

        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke("control Z");
        InputMap undoInputMap = textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap undoactionMap = textArea.getActionMap();
        undoInputMap.put(undoKeyStroke, "Undo");
        undoactionMap.put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ctrl+z pressed, undo");
                undoMenuItem.doClick();
            }            
        });

        editMenu.add(undoMenuItem);
        // redo -------------------------------------------
        JMenuItem redoMenuItem = new JMenuItem("Redo ctrl+z");
        redoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(undoManager.canRedo())                {
                    undoManager.redo();
                }
            }            
        });

        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke("control R");
        InputMap redoInputMap = textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap redoactionMap = textArea.getActionMap();
        redoInputMap.put(redoKeyStroke, "Redo");
        redoactionMap.put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ctrl+r pressed, redo");
                redoMenuItem.doClick();
            }            
        });

        editMenu.add(redoMenuItem);

        return editMenu;
    }

    private JMenu addFormatMenu(){
        JMenu formatMenu = new JMenu("Format");

        // word wrap -------------------------------------
        JCheckBoxMenuItem wordWrapMenuItem = new JCheckBoxMenuItem("Word wrap");
        wordWrapMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isCheccked = wordWrapMenuItem.getState();
                System.out.println("checked status: " + isCheccked);
                if(isCheccked){
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                }else{
                    textArea.setLineWrap(false);
                    textArea.setWrapStyleWord(false);
                }
            }            
        });
        formatMenu.add(wordWrapMenuItem);

        // align text ------------------------------------
        JMenu alignTextMenu = new JMenu("Align text");
        JMenuItem alignTextLeft = new JMenuItem("Left");
        alignTextLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("To the left");
                textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            }            
        });
        alignTextMenu.add(alignTextLeft);

        JMenuItem alignTextRight = new JMenuItem("Right");
        alignTextRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("To the right");
                textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            }            
        });
        alignTextMenu.add(alignTextRight);
        formatMenu.add(alignTextMenu);

        // font ------------------------------------------
        JMenuItem fontMenuItem = new JMenuItem("Font..");
        fontMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FontMenu(Notepad.this).setVisible(true);
            }            
        });
        formatMenu.add(fontMenuItem);

        return formatMenu;
    }

    private JMenu addViewMenu(){
        JMenu viewMenu = new JMenu("View");

        JMenu zoomMenu = new JMenu("Zoom");

        // zoom in
        JMenuItem zoomInMenuItem = new JMenuItem("Zoom in ctrl+scroll");
        zoomInMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFontSize = textArea.getFont();
                textArea.setFont(new Font(
                    currentFontSize.getName(),
                    currentFontSize.getStyle(),
                    currentFontSize.getSize() + 1
                ));
            }            
        });
        zoomMenu.add(zoomInMenuItem);

        // zoom out
        JMenuItem zoomOutMenuItem = new JMenuItem("Zoom out ctrl+scroll");
        zoomOutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFontSize = textArea.getFont();
                textArea.setFont(new Font(
                    currentFontSize.getName(),
                    currentFontSize.getStyle(),
                    currentFontSize.getSize() - 1
                ));
            }            
        });
        zoomMenu.add(zoomOutMenuItem);

        // default
        JMenuItem defaultMenuItem = new JMenuItem("Default zoom");
        defaultMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFontSize = textArea.getFont();
                textArea.setFont(new Font(
                    currentFontSize.getName(),
                    currentFontSize.getStyle(),
                    12
                ));
            }            
        });
        zoomMenu.add(defaultMenuItem);

        textArea.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0){
                    // scroll upwards
                    if(e.getWheelRotation() < 0){
                        zoomInMenuItem.doClick();
                    }
                    // scroll downwards
                    else if(e.getWheelRotation() > 0){
                        zoomOutMenuItem.doClick();
                    }
                }
            }            
        });

        viewMenu.add(zoomMenu);
        return viewMenu;

    }

}
