package com.matrix.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class InvitationCodeGeneratorTest {

    @Test
    void generatesStableSixCharacterCode() {
        String first = InvitationCodeGenerator.generateUniqueCode(123456789L);

        assertEquals(6, first.length());
        assertEquals(first, InvitationCodeGenerator.generateUniqueCode(123456789L));
        assertNotEquals(first, InvitationCodeGenerator.generateUniqueCode(987654321L));
    }
}
