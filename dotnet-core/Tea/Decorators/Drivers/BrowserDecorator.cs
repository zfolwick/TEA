
using System.Collections.Generic;
using OpenQA.Selenium;

namespace Tea
{
    public abstract class BrowserDecorator : WebBrowser
    {
        protected WebBrowser Browser;
        public BrowserDecorator(WebBrowser browser)
        {
            Browser = browser;
        }

        public override IWebDriver GetDriver()
        {
            return Browser.GetDriver();
        }

        public override Element GetActiveElement()
        {
            return Browser?.GetActiveElement();
        }
        public override Element FindElement(By locator)
        {
            return Browser?.FindElement(locator);
        }

        public override List<Element> FindElements(By locator)
        {
            return Browser?.FindElements(locator);
        }

        public override List<Element> FindAllFrames()
        {
            return Browser?.FindAllFrames();
        }

        public override Element FindElementInFrames(By by)
        {
            return Browser?.FindElementInFrames(by);
        }
        public override void GoTo(string url)
        {
            Browser?.GoTo(url);
        }

        public override void Quit()
        {
            Browser?.Quit();
        }
        public override void Start(BrowserList browser)
        {
            Browser?.Start(browser);
        }
    }
}