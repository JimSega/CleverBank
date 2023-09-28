package org.clevertec;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class Command {
    private final User user;
    private final BigDecimal quantity;
    private final Counter counter;
    private final int sign;
    public Command(User user, Counter counter, BigDecimal quantity, int sign) {
        this.user = user;
        this.counter = counter;
        this.quantity = quantity;
        this.sign = sign;
    }




    protected void getAndDeposit(String type) {
        synchronized (counter) {
            Connection connection = null;
            try {
                connection = new ConnectionBD().connect();
                PreparedStatement getTotal = connection
                        .prepareStatement("SELECT money::decimal from clients WHERE nameuser=?");
                getTotal.setString(1, user.getName());
                ResultSet resultSet = getTotal.executeQuery();
                resultSet.next();
                BigDecimal total = resultSet.getBigDecimal("money");
                if ((quantity.compareTo(total) > 0 && sign == -1) || quantity.compareTo(BigDecimal.valueOf(0)) < 0) {
                    System.out.println("insufficient funds or not correct");
                } else {
                    PreparedStatement statementUpdate = connection
                            .prepareStatement("UPDATE clients SET money=? WHERE nameuser=?");
                    BigDecimal resultMoney = total.add(quantity.multiply(BigDecimal.valueOf(sign)));
                    statementUpdate.setBigDecimal(1, resultMoney);
                    statementUpdate.setString(2, user.getName());
                    if(counter.getCurrency() == null) {
                        counter.setCurrency(user);
                    }
                    Transaction result = (statementUpdate.executeUpdate() == 1) ? new Transaction(
                            "Clever-Bank","Clever-Bank", type, counter.getCounter(),
                            counter.getCounter(), quantity, LocalDate.now().toString(),
                            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                            counter.getCurrency())
                            : null;

                    if(result != null) {
                        result.writeRow();
                    } else {System.out.println("transaction write error");}

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    assert connection != null;
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    protected void transfer(String bankToName, String name) {
        Bank bankTo = new Bank(bankToName);
        String uuid = bankTo.getUuid(name);
        if (uuid == null) {
            System.out.println("not found bank or client");
        } else {
            synchronized (counter) {
                Connection connection = null;
                try {
                    connection = new ConnectionBD().connect();
                    PreparedStatement getTotal = connection
                            .prepareStatement("SELECT money::decimal from clients WHERE nameuser=?");
                    getTotal.setString(1, user.getName());
                    ResultSet resultSet = getTotal.executeQuery();
                    resultSet.next();
                    BigDecimal total = resultSet.getBigDecimal("money");
                    if (quantity.compareTo(total) > 0 || quantity.compareTo(BigDecimal.valueOf(0)) <= 0) {
                        System.out.println("insufficient funds or not correct");
                    } else {
                        PreparedStatement statementUpdate = connection
                                .prepareStatement("UPDATE clients SET money=? WHERE nameuser=?");
                        BigDecimal resultMoney = total.add(quantity.multiply(BigDecimal.valueOf(sign)));
                        statementUpdate.setBigDecimal(1, resultMoney);
                        statementUpdate.setString(2, user.getName());
                        if(counter.getCurrency() == null) {
                            counter.setCurrency(user);
                        }
                        Transaction result = (statementUpdate.executeUpdate() == 1) ? new Transaction(
                                "Clever-Bank", bankToName,"transfer", counter.getCounter(), uuid, quantity,
                                LocalDate.now().toString(),
                                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), counter.getCurrency())
                                : null;

                        if(result != null) {
                            result.writeRow();
                        } else {System.out.println("transaction write error");}

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        assert connection != null;
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        }


    }

}
