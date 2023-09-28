package org.clevertec.clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    final private String address;
    final private int port;
    public Client (String address, int port) {
        this.address = address;
        this.port = port;
        System.out.println("Client started!");
    }
    public static void main(String[] args) {
        new Client("127.0.0.1", 1026).connect();
    }
    public void connect() {
        try (Socket socket = new Socket (InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String fromServer = "";
            while (!fromServer.contains("hello")) {
                fromServer = input.readUTF();
                System.out.println(fromServer);
                output.writeUTF(new Scanner(System.in).nextLine());
                fromServer = input.readUTF();
                System.out.println(fromServer);
            }

            //enter command
            fromServer = input.readUTF();
            System.out.println(fromServer);
            String command = new Scanner(System.in).nextLine();
            output.writeUTF(command);

            if(command.equalsIgnoreCase("transfer")) {
                //enter bank
                System.out.println(input.readUTF());
                output.writeUTF(new Scanner(System.in).nextLine());
                //enter who
                System.out.println(input.readUTF());
                output.writeUTF(new Scanner(System.in).nextLine());
            }
            //enter quantity
            fromServer = input.readUTF();
            System.out.println(fromServer);
            if(!fromServer.equalsIgnoreCase("bad command")) {
                output.writeUTF(new Scanner(System.in).nextLine());
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
