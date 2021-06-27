import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginWindow extends JFrame {
    private final ChatClient client;
    JTextField loginField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");

    public LoginWindow() {
        super("JChat");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        this.client = new ChatClient("localhost", 8818);
        this.setSize(width/4, height/5);
        int x = width/2;
        int y = height/2;
        pack();
        client.connect();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        Font main = new Font("SansSerif", Font.PLAIN, 20);
        p.add(loginField);
        p.add(passwordField);
        p.add(loginButton);
        loginField.setFont(main);
        passwordField.setFont(main);
        loginButton.setFont(main);
        loginButton.addActionListener(e -> doLogin());
        getContentPane().add(p, BorderLayout.CENTER);
        pack();

        setVisible(true);
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
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login/password.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginWindow loginWin = new LoginWindow();
        loginWin.setVisible(true);
    }
}
