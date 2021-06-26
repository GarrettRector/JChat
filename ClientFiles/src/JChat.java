import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class JChat {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JChat Updater");
        frame.setLayout(null);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        int fheight = height/4;
        int fwidth = width/4;
        frame.setSize(fwidth, fheight);
        frame.setUndecorated(true);
        JPanel panel = new MotionPanel(frame);
        panel.setBounds(0, 0, fwidth, fheight);
        panel.setBackground(new Color(42, 45, 50));
        frame.add(panel);
        frame.setAlwaysOnTop(true);
        frame.setLocation((size.width/2)-fwidth/2,(size.height/2)-fheight/2);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(isOnline()) {
            JFrame bckground = new JFrame("JChat");
            bckground.setLayout(null);
            bckground.setSize(width,height);
            JPanel bckpanel = new JPanel();
            bckpanel.setBackground(new Color(42, 45, 50));
            bckpanel.setBounds(0, 0, width, height);
            bckground.add(bckpanel);
            bckground.setVisible(true);
            bckground.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } else {
            JOptionPane.showMessageDialog(frame,"No internet or the server is Offline. Please try again later","Alert",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }

    public static boolean isOnline() {
        boolean b = true;
        try {
            String host = getip();
            if(host!=null) {
                InetSocketAddress sa = new InetSocketAddress(host, 8818);
                Socket ss = new Socket();
                ss.connect(sa, 500);
                ss.close();
            }
        } catch(Exception e) {
            b = false;
        }
        return b;
    }

    public static String getip() {
        Path properties = Paths.get("ClientFiles/src/config.properties");
        File configFile = new File(String.valueOf(properties));

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String host = props.getProperty("serverip");
            reader.close();
            return host;
        } catch (IOException ex) {
            return null;
        }
    }
}
