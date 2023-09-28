package org.clevertec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@AllArgsConstructor
@Getter
public class Bank {
    private String bank;
    @SneakyThrows
    public String getUuid(String name) {
        String uuid;
        Connection connection = new ConnectionBD().connect();
        String SQL = "SELECT uuid from banks WHERE bank=? AND name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, bank);
        preparedStatement.setString(2, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            uuid = resultSet.getString("uuid");
        } else uuid = null;
        connection.close();
        return uuid;
    }
}
