package com.tea.framework.SeleniumWrapper.Drivers;

import com.tea.framework.SeleniumWrapper.Elements.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface BrowserBase {
    void Start(BrowserList browser);

    void Quit();

    void GoTo(String url);

    Element FindElement(By locator);

    List<Element> FindElements(By locator);

    Element FindElementInFrames(By by);

    List<Element> FindAllFrames();

    Element GetActiveElement();

    WebDriver GetDriver();
}


