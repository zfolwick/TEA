package com.tea.framework.parser;

import java.util.ArrayList;
import java.util.List;

public class BrowserActionLine {

    public List<ActionLine> actionLines;

    public List<ActionLine> getActionLines() {
        return actionLines;
    }

    public BrowserActionLine(List<Token> actionTokens) {
        actionLines = new ArrayList<>();

        int lineIndex = 0;
        String[] line = new String[3];
        for (Token t : actionTokens) {
            if (lineIndex > 0 && lineIndex % 3 == 0) {
                lineIndex = 0;
                actionLines.add(new ActionLine(line));
            }
            // assign the actions
            // assign the selectors
            // assign the locators
            if (t.getType() == Token.TokenType.TOKEN_ACTION ||
                    t.getType() == Token.TokenType.TOKEN_SELECTOR ||
                    t.getType() == Token.TokenType.TOKEN_LOCATOR)
            {
                line[lineIndex++] = t.getValue();
                continue;
            }
        }
        // add the final line
        actionLines.add(new ActionLine(line));
    }
}
