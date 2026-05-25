import javax.swing.JFrame;
import ui.ProEA;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ProEA proEA = new ProEA(frame);
        frame.setContentPane(proEA);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
}