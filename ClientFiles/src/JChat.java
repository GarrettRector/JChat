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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JChat extends Component {

    private ChatClient client;
    public static JTextField loginField = new JTextField();
    public static JPasswordField passwordField = new JPasswordField();
    public static JButton loginButton = new JButton("Login");

    public JChat() throws InterruptedException, IOException {
        JFrame frame = new JFrame("JChat Updater");
        frame.setLayout(null);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        int fheight = (int) (height/3.5);
        int fwidth = (int) (width/3.5);
        frame.setSize(fwidth, fheight);
        frame.setUndecorated(true);
        JPanel panel = new MotionPanel(frame);
        panel.setBounds(0, 0, fwidth, fheight);
        panel.setBackground(new Color(42, 45, 50));
        Path properties = Paths.get("ClientFiles/src/jchat.png");
        File file = new File(String.valueOf(properties));
        BufferedImage myPicture = ImageIO.read(new File(String.valueOf(file)));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setBounds(height/3, width/3, width, height);
        panel.add(picLabel);
        frame.add(panel);
        frame.setAlwaysOnTop(true);
        frame.setLocation((size.width/2)-fwidth/2,(size.height/2)-fheight/2);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(isOnline()) {
            String ip = getip();
            String port = getport();
            this.client = new ChatClient(ip, Integer.parseInt(port));
            JFrame bckground = new JFrame("JChat");
            bckground.setLayout(null);
            bckground.setSize(width,height);
            JPanel bckpanel = new JPanel();
            bckpanel.setBackground(new Color(42, 45, 50));
            bckpanel.setBounds(0, 0, width, height);
            bckground.add(bckpanel);
            bckground.setVisible(true);
            bckground.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Thread.sleep(2000);
            frame.setVisible(false);
            JInternalFrame loginFrame = new JInternalFrame();
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Font main = new Font("SansSerif", Font.PLAIN, 20);
            bckground.add(loginField);
            bckground.add(passwordField);
            bckground.add(loginButton);
            loginField.setFont(main);
            passwordField.setFont(main);
            loginButton.setFont(main);
            loginButton.addActionListener(e -> doLogin());
            bckground.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame,"No internet or the server is Offline. Please try again later","",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }

    public boolean isOnline() {
        boolean b = true;
        try {
            String host = getip();
            String port = getport();
            if(host != null & port!=null) {
                InetSocketAddress sa = new InetSocketAddress(host, Integer.parseInt(port));
                Socket ss = new Socket();
                ss.connect(sa, 500);
                ss.close();
            }
        } catch(Exception e) {
            b = false;
        }
        return b;
    }

    public String getip() {
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

    public String getport() {
        Path properties = Paths.get("ClientFiles/src/config.properties");
        File configFile = new File(String.valueOf(properties));

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String port = props.getProperty("port");
            reader.close();
            return port;
        } catch (IOException ex) {
            return null;
        }
    }

    private void doLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();

        try {
            if (client.login(login, password)) {
                MainWindow mainWindow = new MainWindow(client);
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setSize(size);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                frame.getContentPane().add(mainWindow, BorderLayout.CENTER);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login/password.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        JChat jchat = new JChat();
    }
}
