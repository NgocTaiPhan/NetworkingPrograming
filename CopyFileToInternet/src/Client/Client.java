package Client;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {
    Socket client;
    DataInputStream response;
    DataOutputStream request;

    public Client() throws IOException {
        client = new Socket("localhost", 2000);
        response = new DataInputStream(client.getInputStream());
        request = new DataOutputStream(client.getOutputStream());

    }

    public void run() throws IOException {
        System.out.println(response.readUTF());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String userCommand = reader.readLine();
            if (userCommand.equalsIgnoreCase("exit")) {
                request.writeUTF(userCommand);
                request.flush();
                System.out.println(response.readUTF());
                client.close();
                break;
            }
            StringTokenizer stk = new StringTokenizer(userCommand);
            String command = stk.nextToken();
            if (command.equalsIgnoreCase("upload")) {

                String sourcePath = stk.nextToken();
                String destPath = stk.nextToken();
//                System.out.println(command);
//                System.out.println(sourcePath);
//                System.out.println(destPath);
                File file = new File(sourcePath);
                if (!file.exists()) {
                    System.out.println("File does not exist!");
                    continue;
                }
                sendFileToServer(file, destPath);


            }


        }
    }

    private void sendFileToServer(File sourceFile, String destPath) throws IOException {

        FileInputStream fis = new FileInputStream(sourceFile);
        request.writeUTF("up " + destPath);
        request.writeLong(sourceFile.length());
        request.flush();
        int byteRead;
        byte[] buff = new byte[1024];
        while ((byteRead = fis.read(buff)) != -1) {
            request.write(buff, 0, byteRead);

        }
        fis.close();
        System.out.println(response.readUTF());

    }

    public static void main(String[] args) throws IOException {
        new Client().run();
    }
}
