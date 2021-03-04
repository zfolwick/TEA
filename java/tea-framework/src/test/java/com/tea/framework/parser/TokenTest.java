package com.tea.framework.parser;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTest {
    @Test
    public void canCreateToken() {
        Token t = new Token(Token.TokenType.TOKEN_ACTION, 0, "click");

        assertEquals(Token.TokenType.TOKEN_ACTION, t.getType());
        assertEquals(0, t.getPosition());
        assertEquals("click", t.getValue());
    }
}
