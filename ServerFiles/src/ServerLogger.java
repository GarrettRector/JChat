import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.applet.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ServerLogger extends Applet{
    private static final String RandStr = getRandStr();
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String login = reader.readLine();
        String password = reader.readLine();

        Path loginPath = Paths.get("ServerFiles/src/login.csv");
        FileWriter fw = new FileWriter(String.valueOf(loginPath),true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("\n");
        bw.write(login);
        bw.write(",");
        bw.write(password);
        bw.write(",");
        bw.write(RandStr);
        bw.close();

        try(
                BufferedReader br = new BufferedReader(new FileReader("login.csv"));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br)
        ) {
            for(CSVRecord record : parser) {
                String logintoken = record.get("login");
                System.out.println(logintoken);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
}
