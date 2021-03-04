package com.tea.framework.parser;

import java.util.Arrays;
import java.util.Stack;

public class SourceScanner {

    private String buffer;
    private String[] bufferArray;
    private int position;
    private Stack<Integer> positionStack = new Stack<>();

    public int getPosition() {
        return position;
    }

    public SourceScanner(String source) {
        buffer = source;
        bufferArray = Arrays.stream(buffer.split(" "))
                .filter(b -> !b.isEmpty())
                .toArray(String[]::new);
    }

    public boolean atEndOfSource() {
        return position >= bufferArray.length;
    }

    public String read() {
        return (atEndOfSource()) ? null : bufferArray[position++];
    }

    /*
     *peeks at the next character in the buffer without incrementing the position.
     */
    public String peek() {
        return position >= bufferArray.length ? null : bufferArray[getPosition()];
    }

    public void push() {
        positionStack.push(getPosition());
    }

    public void pop() {
        position = positionStack.pop();
    }
}
