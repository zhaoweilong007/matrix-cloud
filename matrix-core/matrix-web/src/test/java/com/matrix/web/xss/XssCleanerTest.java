package com.matrix.web.xss;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XssCleanerTest {

    @Test
    void shouldRemoveExecutableContentAndRetainSafeMarkup() {
        String cleaned = XssCleaner.clean("<p>safe</p><script>alert(1)</script><a href=\"javascript:alert(1)\">link</a>");

        assertTrue(cleaned.contains("<p>safe</p>"));
        assertFalse(cleaned.contains("script"));
        assertFalse(cleaned.contains("javascript:"));
    }
}
