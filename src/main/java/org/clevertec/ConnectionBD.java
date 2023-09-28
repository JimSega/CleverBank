package org.clevertec;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionBD {
    @SneakyThrows
    public Connection connect() {
        Class.forName((String) OpenConfig.openFile().get("Connection"));
        return DriverManager.getConnection((String) OpenConfig.openFile().get("DriverManager"),
                (String) OpenConfig.openFile().get("user"), (String) OpenConfig.openFile().get("password"));
    }
}
