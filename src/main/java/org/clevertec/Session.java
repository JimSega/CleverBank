package org.clevertec;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class Session extends Thread {
    private Socket socket;
    private final ServerSocket serverSocket;
    public Session(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        try {
            this.socket = serverSocket.accept();
        } catch (SocketException ignore) {
        }
    }
    @SneakyThrows
    public void run() {
        Message message = new Message(socket);
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            if (message.authorization(input, output) != null) {
                message.setCommand(input, output);
            }



            if (!Server.running) { //??????
                serverSocket.close();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
