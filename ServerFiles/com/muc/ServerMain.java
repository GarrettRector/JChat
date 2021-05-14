package com.muc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        int port = 8818;
        Server server = new Server(port);
        server.start();
        ServerSocket Server = new ServerSocket(8505);
        while(true)
        {
            Socket client = Server.accept();
            InputStream in = client.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inr);
            String str = br.readLine();
            PrintStream ps = new PrintStream(client.getOutputStream());
            ps.println(str);
        }
    }
}
