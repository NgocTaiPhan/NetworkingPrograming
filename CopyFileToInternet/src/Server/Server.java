package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;

    public Server() throws IOException {
        serverSocket = new ServerSocket(2000);
        run();
    }


    public void run() throws IOException {
        while (true) {
            Socket server = serverSocket.accept();
            Connection connection = new Connection(server);
            connection.run();
        }

    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}