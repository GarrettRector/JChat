import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class JChat {
    public static void main(String[] args) {
        JFrame bckground = new JFrame("JChat");
        JFrame frame = new JFrame("JChat Updater");
        frame.setLayout(null);
        bckground.setLayout(null);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        int fheight = height/4;
        int fwidth = width/4;
        frame.setSize(fwidth, fheight);
        bckground.setSize(width,height);
        frame.setUndecorated(true);

        JPanel panel = new MotionPanel(frame);
        JPanel bckpanel = new JPanel();
        bckpanel.setBackground(new Color(42, 45, 50));
        panel.setBounds(0, 0, fwidth, fheight);
        bckpanel.setBounds(0, 0, width, height);
        panel.setBackground(new Color(42, 45, 50));
        frame.add(panel);
        bckground.add(bckpanel);
        frame.setAlwaysOnTop(true);
        frame.setLocation((size.width/2)-fwidth/2,(size.height/2)-fheight/2);

        bckground.setVisible(true);
        frame.setVisible(true);
        bckground.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static boolean isOnline() {
        boolean b = true;
        try{
            InetSocketAddress sa = new InetSocketAddress("104.191.118.227", 8818);
            Socket ss = new Socket();
            ss.connect(sa, 500);
            ss.close();
        }catch(Exception e) {
            b = false;
        }
        return b;
    }
}
