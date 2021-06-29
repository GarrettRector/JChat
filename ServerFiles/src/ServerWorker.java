import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;
    private final HashSet<String> topicSet = new HashSet<>();
    public int UsrOnl = 0;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (SocketException e) {
            try {
                handleLogoff();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ( (line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                } else if ("join".equalsIgnoreCase(cmd)) {
                    handleJoin(tokens);
                } else if ("leave".equalsIgnoreCase(cmd)) {
                    handleLeave(tokens);
                } else {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }

    private void handleLeave(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.remove(topic);
        }
    }

    public boolean isMemberOfTopic(String topic) {
        return topicSet.contains(topic);
    }

    private void handleJoin(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.add(topic);
        }
    }

    private void handleMessage(String[] tokens) {
        String sendTo = tokens[1];
        String body = tokens[2];

        boolean isTopic = sendTo.charAt(0) == '#';

        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker : workerList) {
            if (isTopic) {
                if (worker.isMemberOfTopic(sendTo)) {
                    String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
                    worker.send(outMsg);
                }
            } else {
                if (sendTo.equalsIgnoreCase(worker.getLogin())) {
                    String outMsg = "msg " + login + " " + body + "\n";
                    worker.send(outMsg);
                }
            }
        }
    }

    private void handleLogoff() throws IOException {
        UsrOnl = UsrOnl - 1;
        server.removeWorker(this);
        List<ServerWorker> workerList = server.getWorkerList();
        String onlineMsg = "offline " + login + "\n";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.err.println("User" + login + "Logged off at" + dtf.format(now));
        for(ServerWorker worker : workerList) {
            if (!login.equals(worker.getLogin())) {
                worker.send(onlineMsg);
            }
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
    }

    public static int loginCheck(String loginToken, String passwordToken) {
        try {
            Path login = Paths.get("ServerFiles/src/credentials/login.csv");
            BufferedReader br = new BufferedReader(new FileReader(String.valueOf(login)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] Ans = line.split(",");
                for (String ignored : Ans) {
                    if (Ans[0].equals(loginToken) && Ans[1].equals(passwordToken)) {
                        return 1;
                    }
                }
            }
            br.close();
        } catch (IOException ex) {
            System.err.println("Incorrect Login for user " + loginToken);
        }
        return 0;
    }

    public String getToken(String user) {
        try {
            Path login = Paths.get("ServerFiles/src/credentials/login.csv");
            BufferedReader br = new BufferedReader(new FileReader(String.valueOf(login)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] Ans = line.split(",");
                for (String ignored : Ans) {
                    if (Ans[0].equals(user)) {
                        return (Ans[2]);
                    } else {
                        return "404";
                    }
                }
            }
            br.close();
        } catch (IOException ignored) {
        }
        return null;
    }

    public Boolean isBot(String user) {
        try {
            Path login = Paths.get("ServerFiles/src/credentials/login.csv");
            BufferedReader br = new BufferedReader(new FileReader(String.valueOf(login)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] Ans = line.split(",");
                for (String ignored : Ans) {
                    if (Ans[0].equals(user)) {
                        if (Ans[3].equals("yes"))
                            return true;
                        } else {
                        return false;
                    }
                }
            }
            br.close();
        } catch (IOException ignored) {
        }
        return null;
    }

    public String getIp(String user) {
        try {
            Path login = Paths.get("ServerFiles/src/credentials/ips.csv");
            BufferedReader br = new BufferedReader(new FileReader(String.valueOf(login)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] Ans = line.split(",");
                for (String ignored : Ans) {
                    if (Ans[0].equals(user)) {
                        return Ans[1];
                    } else {
                        return null;
                    }
                }
            }
            br.close();
        } catch (IOException ignored) {
        }
        return null;
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];
            if (loginCheck(login, password) == 1) {
                UsrOnl = UsrOnl + 1;
                String msg = "ok login\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                try {
                    System.out.println(("User " + login + " Logged in successfully at " + dtf.format(now)) + " From the address " + Server.IPs[UsrOnl-1]);
                } catch(Exception e) {
                    System.out.println(("User " + login + " Logged in successfully at " + dtf.format(now)) + " From the address " + Server.IPs[UsrOnl]);
                }

                List<ServerWorker> workerList = server.getWorkerList();

                for(ServerWorker worker : workerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }

                String onlineMsg = "online " + login + "\n";
                for(ServerWorker worker : workerList) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }
            } else {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }

    private void send(String msg) {
        if (login != null) {
            try {
                outputStream.write(msg.getBytes());
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
