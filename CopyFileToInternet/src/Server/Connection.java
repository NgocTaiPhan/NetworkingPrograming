package Server;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class Connection {
    Socket socket;
    DataInputStream request;
    DataOutputStream response;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        request = new DataInputStream(socket.getInputStream());
        response = new DataOutputStream(socket.getOutputStream());
    }

    public DataInputStream getRequest() {
        return request;
    }

    public void setRequest(DataInputStream request) {
        this.request = request;
    }

    public DataOutputStream getResponse() {
        return response;
    }

    public void setResponse(DataOutputStream response) {
        this.response = response;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() throws IOException {
        System.out.println("Client connected!");
        response.writeUTF("Connected to server");
        response.flush();

        String line = request.readUTF();
        System.out.println(line);


        StringTokenizer stk = new StringTokenizer(line);
        String command = stk.nextToken();
        if (command.equalsIgnoreCase("exit")) {
            response.writeUTF("Disconnected");
            response.flush();
            socket.close();

        }
        if (command.equalsIgnoreCase("up")) {
            //Tách dest path từ lệnh gửi từ client
            String destpath = stk.nextToken();
            long fileLength = request.readLong();
            writeFileFromInternet(fileLength, destpath);

        }


    }

    private void writeFileFromInternet(long fileLength, String destPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(destPath);
        byte[] buff = new byte[1024];
        while (fileLength > 0) {
            //So sánh fileLength với buff length vì 1 lần đọc 1024 byte nhưng khi gần hết sẽ có thể không đủ 1024 byte
            int byteToRead = fileLength < buff.length ? (int) fileLength : buff.length;
            //Số byte thực sự đã đọc được
            int bytesRead = request.read(buff, 0, byteToRead);
            fos.write(buff, 0, bytesRead);
            fileLength -= bytesRead;//trừ đi số byte đọc được

        }
        fos.close();
        response.writeUTF("WriteDone!");
        response.flush();
    }


}
