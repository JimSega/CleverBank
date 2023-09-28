package org.clevertec;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


@Getter
class Counter {
    private final String counter;
    private static final HashMap<String, Counter> counterMap = new HashMap<>();
    private String currency;

    @SneakyThrows
    public void setCurrency(User user) {
        Connection connection = new ConnectionBD().connect();
        String SQL = "SELECT currency from clients WHERE nameuser=?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, user.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        currency = resultSet.getString("currency");
        connection.close();
    }
    public static Counter getCounterMap(User user) {
        synchronized (Counter.class) {
            if (!counterMap.containsKey(user.getName())) {
                Counter counter = new Counter(user);
                counterMap.put(user.getName(), counter);
            }
            return counterMap.get(user.getName());
        }

    }
    @SneakyThrows
    protected Counter(User user) {
        Connection connectionUser = new ConnectionBD().connect();
        String SQL = "SELECT counter from clients WHERE nameuser=?";
        PreparedStatement preparedStatement = connectionUser.prepareStatement(SQL);
        preparedStatement.setString(1, user.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        counter = resultSet.getString("counter");
        connectionUser.close();
    }

    @SneakyThrows
    protected void accountStatement(LocalDate begin, LocalDate end) {
        Connection connection = new ConnectionBD().connect();
        String sqlName = "SELECT * from clients WHERE counter=?::uuid";
        PreparedStatement preparedStatementName = connection.prepareStatement(sqlName);
        preparedStatementName.setString(1 , counter);
        ResultSet resultSetName = preparedStatementName.executeQuery();
        resultSetName.next();
        String name = resultSetName.getString("nameuser");
        String dateOpen = resultSetName.getString("dateopen");
        String balance = resultSetName.getString("money").replaceAll(" \\?", "");
        resultSetName.close();
        String SQL = "SELECT * FROM public.transactions WHERE date BETWEEN ? AND ?  AND (tocounter=?::uuid OR fromcounter=?::uuid) " +
                "ORDER BY date ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setDate(1, Date.valueOf(begin));
        preparedStatement.setDate(2, Date.valueOf(end));
        preparedStatement.setString(3, counter);
        preparedStatement.setString(4, counter);


        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.printf("""
                \tStatement
                \tClever-Bank
                Client\t%s
                Currency\t%s
                Date open\t%s
                period\t%s - %s
                Date and time of formation\t%s, %s
                Balance\t%s
                """, name, currency, dateOpen, begin, end, LocalDate.now(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), balance);

        while (resultSet.next()) {
            System.out.println(resultSet.getString("summa").replaceAll(" \\?", ""));
        }
        connection.close();

    }
}
