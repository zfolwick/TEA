package com.tea.framework.parser;

public class ActionLine {
    public String getBy() {
        return by;
    }

    public String getAction() {
        return action;
    }

    public String getText() {
        return text;
    }

    private String by;
    private String action;
    private String text;

    public ActionLine(String[] line) {
        action = line[0];
        by = line[1];
        text = line[2];
    }


    public void printLine() {
        System.out.println(
                String.format("%s\t%s\t%s", action, by, text)
        );
    }

    public String toString() {
        return String.format("%s\t%s\t%s", action, by, text);
    }
}