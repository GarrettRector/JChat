import javax.swing.*;
import java.awt.*;

public class JChat {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JChat");
        frame.setLayout(null);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        frame.setSize(width/4, height/5);
        frame.setUndecorated(true);

        JPanel panel = new MotionPanel(frame);
        panel.setBounds(0, 0, width/4, height/5);
        panel.setBackground(new Color(60, 65, 70));
        frame.add(panel);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
