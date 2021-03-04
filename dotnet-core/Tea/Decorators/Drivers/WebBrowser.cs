using System;
using System.Collections.Generic;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Support.UI;
using SeleniumExtras.WaitHelpers;

namespace Tea
{
    public class WebBrowser : BrowserBase
    {
        private IWebDriver webDriver;
        private WebDriverWait wait;
        public override void Start(BrowserList browser)
        {
            switch (browser)
            {
                case BrowserList.Chrome:
                    ChromeOptions options = new ChromeOptions();
                    options.AddArguments("--disable-notifications");
                    webDriver = new ChromeDriver(options);
                    break;
                default:
                    throw new ArgumentOutOfRangeException(nameof(browser), browser, null);
            }
            wait = new WebDriverWait(webDriver, TimeSpan.FromSeconds(10));
        }
        public override IWebDriver GetDriver()
        {
            return webDriver;
        }
        public override Element GetActiveElement()
        {
            IWebElement nativeWebElement = webDriver.SwitchTo().ActiveElement();
            string v = nativeWebElement.GetAttribute("id");
            Element element = new WebElement(webDriver, nativeWebElement,By.Id(v));
            Element logElement = new LogElement(element);
            return logElement;
        }
        public override Element FindElement(By locator)
        {
            IWebElement nativeWebElement = null;
            Element element = null;
            try
            {
                nativeWebElement = wait.Until(ExpectedConditions.ElementExists(locator));
                element = new WebElement(webDriver, nativeWebElement, locator);

            }
            catch (System.Exception)
            {
                Console.WriteLine("It doesn't exist in the current frame.  Search all frames.");
                element = FindElementInFrames(locator);
            }
            Element logElement = new LogElement(element);
            return logElement;
        }

        public override List<Element> FindElements(By locator)
        {
            var nativeWebElements = wait.Until(ExpectedConditions.PresenceOfAllElementsLocatedBy(locator));
            return ConvertToList(locator, nativeWebElements);
        }

        private List<Element> ConvertToList(By locator, IEnumerable<IWebElement> nativeWebElements)
        {
            var elements = new List<Element>();
            foreach (var nativeWebElement in nativeWebElements)
            {
                Element element = new WebElement(webDriver, nativeWebElement, locator);
                elements.Add(element);
            }
            return elements;
        }

        public override Element FindElementInFrames(By by)
        {
            List<Element> iframes = FindAllFrames();
            // need a record of whether I've already iterated over the list.
            Console.WriteLine($"There are {iframes.Count} frames to check.");
            if (iframes.Count == 0)
                webDriver.SwitchTo().ParentFrame();

            for (int i = 0; i < iframes.Count; i++)
            {
                webDriver.SwitchTo().Frame(i);
                wait.Until(ExpectedConditions.VisibilityOfAllElementsLocatedBy(by));
                // redefine the context after switching to prevent staleReferenceeException.
                var frames = FindAllFrames();
                Element webElement = null;
                try
                {
                    webElement = FindElement(by);

                }
                catch (NoSuchElementException)
                {
                    // it's not in here.
                    continue;
                }
                
                if (webElement?.Enabled is bool)
                {
                    // we found what we need and no longer need to iterate.
                    return webElement;
                }
                
            }
            throw new NoSuchElementException();
        }

        public override List<Element> FindAllFrames()
        {
            By locator = By.XPath("//iframe");
            var nativeWebElements = webDriver.FindElements(locator);
            return ConvertToList(locator, nativeWebElements);
        }
        public override void GoTo(string url)
        {
            webDriver.Navigate().GoToUrl(url);
        }

        public override void Quit()
        {
            webDriver.Quit();
        }
    }
}