package org.clevertec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    @Test
    void testGetUuid() {
        Bank bank = new Bank("SWB");
        assertNotNull(bank.getUuid("Ivanov"));
        bank = new Bank("NotRealDate");
        assertNull(bank.getUuid("notRealDate"));
    }
}