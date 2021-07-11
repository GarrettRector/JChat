import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Random;
import java.applet.*;

public class ServerLogger extends Applet{
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Login");
        String login = reader.readLine();
        System.out.println("Is Bot");
        String bot = reader.readLine();
        if (bot.toLowerCase(Locale.ROOT).equals("false")) {
            System.out.println("Password");
            String password = reader.readLine();
            Path loginPath = Paths.get("ServerFiles/src/login.csv");
            FileWriter fw = new FileWriter(String.valueOf(loginPath),true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\n");
            bw.write(login);
            bw.write(",");
            bw.write(password);
            bw.write(",");
            bw.write(getRandStr());
            bw.write(",");
            bw.write("null");
            bw.close();
        } else {
            String abspath = FileSystems.getDefault().getPath("ServerFiles/src/credentials/login.csv").normalize().toAbsolutePath().toString();
            FileWriter fw = new FileWriter(String.valueOf(abspath),true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\n");
            bw.write(login);
            bw.write(",");
            bw.write("null");
            bw.write(",");
            bw.write("null");
            bw.write(",");
            bw.write(getRandSecret());
            bw.close();
        }
    }

    public static String getRandStr() {
        String CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz123456789";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 7) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            string.append(CHARS.charAt(index));
        }
        return string.toString();
    }

    public static String getRandSecret() {
        String CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz123456789";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 16) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            string.append(CHARS.charAt(index));
        }
        return string.toString();
    }
}
