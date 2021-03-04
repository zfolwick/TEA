using System.Collections.Generic;
using OpenQA.Selenium;
using System;

namespace Tea
{
    public class LoggingBrowser : BrowserDecorator
    {
        public LoggingBrowser(WebBrowser Browser) : base(Browser){}

        public override IWebDriver GetDriver()
        {
            return base.GetDriver();
        }
        
        public override Element FindElement(By locator)
        {
            Console.WriteLine($"Find Element: " + locator.ToString());
            return Browser?.FindElement(locator);
        }

        public override List<Element> FindElements(By locator)
        {
            Console.WriteLine($"Find Elements: " + locator.ToString());
            return Browser?.FindElements(locator);
        }

        public override void GoTo(string url)
        {
            Console.WriteLine($"Go to URL: {url}");
            Browser?.GoTo(url);
        }

        public override void Quit()
        {
            Console.WriteLine($"Closing browser.");
            Browser?.Quit();
        }

        public override void Start(BrowserList browser)
        {
            Console.WriteLine($"Starting browser: {(Enum.GetName(typeof(BrowserList), browser))}");
            Browser?.Start(browser);
        }
    }
}