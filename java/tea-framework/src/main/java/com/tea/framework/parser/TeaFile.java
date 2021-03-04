package com.tea.framework.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeaFile {
    public String getPath() {
        return path;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public String getUrl() {
        return url;
    }

    public String getTestCase() {
        return testCase;
    }

    public BrowserActionLine getLines() {
        return lines;
    }

    public List<ActionLine> getActionLines() {
        return lines.actionLines;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;
    private List<Token> tokens;
    private String url;
    private String testCase;
    private BrowserActionLine lines;

    public TeaFile(String path) throws FileNotFoundException {
        this.path = path;
        tokens = new ArrayList<Token>();
        ReadFile();
        ParseTokens();
    }

    private void ReadFile() throws FileNotFoundException {
        // Read the file and display it line by line.  
        Scanner file = new Scanner(new File(path));
        int count = 0;
        while (file.hasNextLine()) {
            count++;
            String line = file.nextLine();
            if (line.isEmpty()) continue;

            Lexer lexer = new Lexer(new SourceScanner(line));

            Token token = null;
            try {
                token = lexer.readNext();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new IllegalStateException("Failed to read line " + count + ".\n");
            }

            while (token != null && token.getType() != Token.TokenType.TOKEN_EOE) {
                tokens.add(token);
                token = lexer.readNext();
            }
        }

        file.close();
    }

    private void ParseTokens() {

        for (Token t : getTokens()) {
            if (t.getValue() != null && t.getValue().length() > 0) {
                // assign Url
                if (t.getType() == Token.TokenType.TOKEN_URL) {
                    url = t.getValue();
                    continue;
                }

                // assign testCase
                if (t.getType() == Token.TokenType.TOKEN_TC_DESC) {
                    testCase = t.getValue();
                    continue;
                }
            }
        }
        lines = new BrowserActionLine(tokens);
    }
}
