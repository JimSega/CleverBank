package org.clevertec;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    User user = new User("TestUser");
    Counter counter = Counter.getCounterMap(user);
    Command command = new Command(user, counter, new BigDecimal(10), 1);

    @Test
    void testGetAndDeposit() {
        assertDoesNotThrow(() -> command.getAndDeposit("withdrawal"));
        assertDoesNotThrow(() -> command.getAndDeposit("deposit"));
    }

    @Test
    void testTransfer() {
        assertDoesNotThrow(() -> command.transfer("TestBank  ", "TestUser"));
    }

}