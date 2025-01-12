import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Notepad extends JFrame{

    private JFileChooser fileChooser;
    private JTextArea textArea;
    private File currentFile;

    public Notepad() {
        super("Notepad");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // filechooser setup
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src\\assets"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        addGuiComponents();
    }

    public void addGuiComponents(){
        addToolbar();

        textArea = new JTextArea();
        add(textArea, BorderLayout.CENTER);
    }

    public void addToolbar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        // menu options
        menuBar.add(addFileMenu());

        add(toolBar, BorderLayout.NORTH);
    }

    public JMenu addFileMenu(){
        JMenu fileMenu = new JMenu("File");
        // new
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // reset title
                setTitle("Notepad");
                textArea.setText("");
                currentFile = null; 
            }            
        });
        fileMenu.add(newMenuItem);
        // open
        JMenuItem openMenuItem = new JMenuItem("Open");
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

                }catch(Exception ex2){
                    ex2.printStackTrace();
                }
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
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                if(currentFile == null){
                    saveAsMenuItem.doClick();
                }
            }
            
        });
        fileMenu.add(saveMenuItem);
        // exit
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }
}
