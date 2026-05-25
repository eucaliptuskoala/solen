package org.solen.business.practicecases.creationstrategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.solen.business.practicecases.creationstrategy.NameUtils.normalizeName;

class NameUtilsTest {

    @Test
    void normalizeName_null_returnsNull() {
        assertNull(normalizeName(null));
    }

    @Test
    void normalizeName_blank_returnsBlank() {
        assertEquals("", normalizeName(""));
        assertEquals(" ", normalizeName(" "));
    }

    @Test
    void normalizeName_trimsAndCapitalizes() {
        assertEquals("Hello", normalizeName("  hello  "));
        assertEquals("World", normalizeName("WORLD"));
        assertEquals("Test", normalizeName("test"));
    }
}
