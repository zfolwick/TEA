package com.tea.framework.SeleniumWrapper.Drivers;

import com.tea.framework.SeleniumWrapper.Elements.Element;
import com.tea.framework.SeleniumWrapper.Elements.LogElement;
import com.tea.framework.SeleniumWrapper.Elements.WebElementWrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static com.tea.framework.SeleniumWrapper.Elements.LogElement.write;

public class WebBrowser implements BrowserBase {
    private WebDriver webDriver;
    private WebDriverWait wait;


    private List<Element> ConvertToList(By locator, List<WebElement> nativeWebElements) {
        List<Element> elements = new ArrayList<>();
        for (WebElement nativeWebElement : nativeWebElements) {
            Element element = new WebElementWrapper(webDriver, nativeWebElement, locator);
            elements.add(element);
        }

        return elements;
    }

    public Element FindElementInFrames(By by) {
        List<Element> iframes = FindAllFrames();
        // need a record of whether I've already iterated over the list.
        write(String.format("There are {%s} frames to check.", iframes.size()));
        if (iframes.size() == 0)
            webDriver.switchTo().parentFrame();

        for (int i = 0; i < iframes.size(); i++) {
            webDriver.switchTo().frame(i);
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
            // redefine the context after switching to prevent staleReferenceeException.
            List<Element> frames = FindAllFrames();
            Element webElement = null;
            try {
                webElement = FindElement(by);

            } catch (NoSuchElementException e) {
                // it's not in here.
                continue;
            }

            if (webElement.isEnabled()) {
                // we found what we need and no longer need to iterate.
                return webElement;
            }

        }
        throw new NoSuchElementException("couldn't find the elemeent on the page");
    }

    public void GoTo(String url) {
        webDriver.navigate().to(url);
    }

    @Override
    public void Start(BrowserList browser) {
        switch (browser) {
            case Chrome:
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                webDriver = new ChromeDriver(options);
                break;
            default:
                throw new IllegalArgumentException(String.format("Browser {%-} is not able to be found"));
        }
        wait = new WebDriverWait(webDriver, 10);
    }


    public void Quit() {
        webDriver.quit();
    }

    public Element FindElement(By locator) {
        WebElement nativeWebElement = null;
        Element element = null;
        try {
            nativeWebElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element = new WebElementWrapper(webDriver, nativeWebElement, locator);

        } catch (NoSuchElementException e) {
            write("It doesn't exist in the current frame.  Search all frames.");
            element = FindElementInFrames(locator);
        }

        Element logElement = new LogElement(element);
        return logElement;
    }

    public List<Element> FindElements(By locator) {
        List<WebElement> nativeWebElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return ConvertToList(locator, nativeWebElements);
    }

    public List<Element> FindAllFrames() {
        By locator = By.xpath("//iframe");
        List<WebElement> nativeWebElements = webDriver.findElements(locator);
        return ConvertToList(locator, nativeWebElements);
    }

    public Element GetActiveElement() {
        WebElement nativeWebElement = webDriver.switchTo().activeElement();
        String v = nativeWebElement.getAttribute("id");
        Element element = new WebElementWrapper(webDriver, nativeWebElement, By.id(v));
        Element logElement = new LogElement(element);
        return logElement;
    }

    public WebDriver GetDriver() {
        return webDriver;
    }
}