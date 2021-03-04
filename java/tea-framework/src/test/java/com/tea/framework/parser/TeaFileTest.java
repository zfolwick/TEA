package com.tea.framework.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class TeaFileTest {
    private static final String teaFile = "src/test/resources/LoginWithFailingUsername.tea";
    @Test
    public void CanGetAFile() throws FileNotFoundException {
        TeaFile tea = new TeaFile(teaFile);
        Assertions.assertNotNull(tea);
    }

    @Test
    public void CanInterpretATeaFileInput() throws FileNotFoundException {
        String url = "http://www.reddit.com";
        String tc = "Test Case: Trying to login to reddit with a random username/password will fail.";
        String action1 = "click\txpath\t//*[text() = 'Log In']";
        String action2 = "click\txpath\t//*[@id='loginUsername']";
        String action3 = "type\tid\trandom";
        String action4 = "click\tid\tloginPassword";
        String action5 = "type\tid\tpassword";
        String action6 = "click\txpath\t//fieldset[5]/button";

        TeaFile tea = new TeaFile(teaFile);

        Assertions.assertEquals(url, tea.getUrl());
        Assertions.assertEquals(tc, tea.getTestCase());

        Assertions.assertEquals(action1, tea.getActionLines().get(0).toString());
        Assertions.assertEquals(action2, tea.getActionLines().get(1).toString());
        Assertions.assertEquals(action3, tea.getActionLines().get(2).toString());
        Assertions.assertEquals(action4, tea.getActionLines().get(3).toString());
        Assertions.assertEquals(action5, tea.getActionLines().get(4).toString());
        Assertions.assertEquals(action6, tea.getActionLines().get(5).toString());
    }
}