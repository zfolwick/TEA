package com.tea.framework.SeleniumWrapper.Drivers;

import com.tea.framework.SeleniumWrapper.Elements.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.tea.framework.SeleniumWrapper.Elements.LogElement.write;

public class LoggingBrowser extends BrowserDecorator {
    public LoggingBrowser(WebBrowser Browser) {
        super(Browser);
    }

    @Override
    public WebDriver GetDriver() {
        return this.browser.GetDriver();
    }

    @Override
    public Element FindElement(By locator) {
        write("Find Element: " + locator.toString());
        return browser.FindElement(locator);
    }

    @Override
    public List<Element> FindElements(By locator) {
        write("Find Elements: " + locator.toString());
        return browser.FindElements(locator);
    }

    @Override
    public void GoTo(String url) {
        write("Go to URL: {url}");
        browser.GoTo(url);
    }

    @Override
    public void Quit() {
        write("Closing browser.");
        browser.Quit();
    }

    @Override
    public void Start(BrowserList browser) {
        write("Starting browser: {(Enum.GetName(typeof(BrowserList), browser))}");
        this.browser.Start(browser);
    }
}