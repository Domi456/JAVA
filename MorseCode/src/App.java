import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        // thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MorseCodeGUI().setVisible(true);
            }            
        });
    }
}
