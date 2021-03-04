package com.tea.framework.parser;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ScannerTest {
    private String expression = "#here is a comment.";
    private SourceScanner scanner;
    public ScannerTest() {
        scanner = new SourceScanner(expression);
    }
    @Test
    public void CanPeekExpressionTest() {
        String expectedString = expression.split(" ")[0];

        String actualString = scanner.peek();

        assertFalse( scanner.atEndOfSource() );
        assertEquals(
                expectedString,
                actualString,
                String.format("unexpected character. expected character {%s}, got {%s}.", expectedString, actualString)
                );
    }

    @Test
    public void CanReadExpressionTest() {
        String[] expectedString = expression.split(" ");
        String actualString = scanner.read();
        assertFalse( scanner.atEndOfSource());

        assertEquals(
                expectedString[0],
                actualString,
                String.format("unexpected string. expected {%s}, got {%s}.", expectedString, actualString)
                );
    }

    @Test
    public void CanFindTheEndOfExpression(){
        assertFalse(scanner.atEndOfSource());
        assertEquals(0, scanner.getPosition());
        for (int i = 0 ; i < expression.length() ; i++ )
            scanner.read();

        assertTrue(scanner.atEndOfSource());
        assertEquals(expression.split(" ").length, scanner.getPosition());
        assertEquals(null, scanner.read());
    }

    @Test
    public void CanParseNewLines() {
        String firstline = "#here is a comment";
        String secondline = "click";
        String expression = firstline + System.lineSeparator() + secondline;
        String[] expectedExpressions = expression.split(System.lineSeparator());

        assertFalse(scanner.atEndOfSource());
        assertEquals(0, scanner.getPosition());
        String actualString = scanner.read();

        assertEquals(
                expectedExpressions[0],
                firstline,
                String.format("Expected the first line to be {%s}. got {%s} instead.", firstline, expectedExpressions[0])
                );
    }

    @Test
    public void CanPushAndPop() {
        assertFalse( scanner.atEndOfSource() );
        assertEquals( 0, scanner.getPosition() );

        scanner.push();
        String[] expected;
        Arrays.asList(expression.split(" "))
                .forEach(e -> assertEquals(e, scanner.read()));

        assertTrue( scanner.atEndOfSource() );

        scanner.pop();

        assertFalse( scanner.atEndOfSource() );
        assertEquals( 0, scanner.getPosition() );
    }
}
