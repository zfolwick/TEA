package com.tea.framework.Actions;

import com.tea.framework.SeleniumWrapper.Drivers.WebBrowser;
import org.openqa.selenium.By;

import java.nio.charset.Charset;
import java.util.Random;

public abstract class Action {
    protected By _by;

    public Action(By by) {
        _by = by;
    }

    public abstract void Perform(WebBrowser browser, String value);

    protected String GetRandomString(int i) {
        byte[] a = new byte[i];
        new Random().nextBytes(a);
        return new String(a, Charset.forName("UTF-8"));
    }
}
