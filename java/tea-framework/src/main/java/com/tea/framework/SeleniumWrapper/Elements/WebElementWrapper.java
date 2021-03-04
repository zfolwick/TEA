package com.tea.framework.SeleniumWrapper.Elements;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebElementWrapper extends Element {
    private WebDriver webDriver;
    private WebElement webElement;

    @Override
    public By getBy() {
        return _by;
    }

    private By _by;

    public WebElementWrapper(WebDriver driver, WebElement element, By by) {
        webDriver = driver;
        webElement = element;
        _by = by;
        text = element.getText();
    }

    @Override
    public boolean isEnabled() {
        return webElement.isEnabled();
    }

    @Override
    public boolean isDisplayed() {
        return webElement.isDisplayed();
    }

    @Override
    public void Click() {
        WaitForElementToBeClickable(_by);
        webElement.click();
    }

    @Override
    public String GetAttribute(String attributeName) {
        return webElement.getAttribute(attributeName);
    }

    @Override
    public void TypeText(String text) {
        WaitForElementToBeClickable(_by);
        webElement.clear();
        webElement.sendKeys(text);
    }

    public void WaitForElementToBeClickable(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(_by));
        } catch (TimeoutException w) {
            System.out.println(
                    String.format("Could not find element by {%s}.", _by.toString()));
            throw w;
        }
    }
}