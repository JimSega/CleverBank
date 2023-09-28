package org.clevertec;

import org.junit.jupiter.api.Test;


import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

    @Test
    void testAccountStatement() throws SQLException{
        Connection connection = new ConnectionBD().connect();
        String sql = "SELECT nameuser from clients ORDER by random() LIMIT 1";
        Statement statement = connection.createStatement();
        ResultSet resultSet =statement.executeQuery(sql);
        resultSet.next();
        User user = new User(resultSet.getString("nameuser"));
        Counter counter = Counter.getCounterMap(user);
        counter.setCurrency(user);
        connection.close();
        assertDoesNotThrow(() -> counter.accountStatement(LocalDate.of(2023,1,1),
                LocalDate.of(2023, 9, 30)));

    }



}