package org.clevertec;

import lombok.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@AllArgsConstructor
@Setter
@Getter
class Message {
    private Socket socket;
    private User user;

    Message(@NonNull Socket socket) {
        this.socket = socket;
    }
    @SneakyThrows
    public User authorization(DataInputStream input, DataOutputStream output) {
        Connection connectionUser = new ConnectionBD().connect();
        while (user == null) {
            String SQL = "SELECT nameuser from clients WHERE nameuser=?";
            PreparedStatement preparedStatement = connectionUser.prepareStatement(SQL);
            output.writeUTF("Enter your name:");
            String nameUser = input.readUTF();
            preparedStatement.setString(1, nameUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getString("nameuser").equals(nameUser)) {
                output.writeUTF("Welcome");
                output.writeUTF("Enter your password:");
                String password = input.readUTF();
                SQL = "SELECT password from clients WHERE nameuser=? AND password=?";
                preparedStatement = connectionUser.prepareStatement(SQL);
                preparedStatement.setString(1, nameUser);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next() && resultSet.getString("password").equals(password)) {
                    user = new User(nameUser);
                    output.writeUTF("hello, " + nameUser);
                    connectionUser.close();
                    return user;
                } else output.writeUTF("This password not correct");

            } else output.writeUTF("This user does not exist");
        }
        connectionUser.close();
        return null;
    }

    @SneakyThrows
    public void setCommand(DataInputStream input, DataOutputStream output) {
        output.writeUTF("enter the name of the operation:");
        String response = input.readUTF().toLowerCase();

        switch (response) {
            case "exit" -> socket.close();
            case "get" -> {
                output.writeUTF("enter quantity:");
                BigDecimal quantity = new BigDecimal(input.readUTF()).setScale(2, RoundingMode.HALF_UP);
                int sign = -1;
                Counter counter = Counter.getCounterMap(user);
                Command command = new Command(user, counter, quantity, sign);
                command.getAndDeposit("withdrawal");
            }
            case "deposit" -> {
                output.writeUTF("enter quantity:");
                BigDecimal quantity = new BigDecimal(input.readUTF()).setScale(2, RoundingMode.HALF_UP);
                int sign = 1;
                Counter counter = Counter.getCounterMap(user);
                Command command = new Command(user, counter, quantity, sign);
                command.getAndDeposit("deposit");
            }
            case "transfer" -> {
                output.writeUTF("enter bank:");
                String bank = input.readUTF();

                output.writeUTF("enter who:");
                String name = input.readUTF();

                output.writeUTF("enter quantity:");
                BigDecimal quantity = new BigDecimal(input.readUTF()).setScale(2, RoundingMode.HALF_UP);
                Counter counter = Counter.getCounterMap(user);
                Command command = new Command(user, counter, quantity, -1);
                command.transfer(bank, name);

            }
            default -> {
                output.writeUTF("bad command");
                socket.close();
            }
        }

    }
}
