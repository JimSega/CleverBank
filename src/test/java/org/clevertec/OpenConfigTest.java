package org.clevertec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenConfigTest {
    @Test
    void testOpenFile() {
        boolean read = OpenConfig.openFile().toString().isEmpty();
        assertFalse(read);
    }

}