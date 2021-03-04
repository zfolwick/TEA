using System;
using OpenQA.Selenium;
using OpenQA.Selenium.Support.UI;
using SeleniumExtras.WaitHelpers;

namespace Tea
{
    public class WebElement : Element
    {
        private readonly IWebDriver webDriver;
        private readonly IWebElement webElement;
        private readonly By _by;

        public WebElement(IWebDriver driver, IWebElement element, By by)
        {
            webDriver = driver;
            webElement = element;
            _by = by;
        }
        public override By By => _by;

        public override string Text => webElement?.Text;

        public override bool? Enabled => webElement?.Enabled;

        public override bool? Displayed => webElement?.Displayed;

        public override void Click()
        {
            WaitForElementToBeClickable(_by);
            webElement?.Click();
        }

        public override string GetAttribute(string attributeName)
        {
            return webElement?.GetAttribute(attributeName);
        }

        public override void TypeText(string text)
        {
            WaitForElementToBeClickable(_by);
            webElement?.Clear();
            webElement?.SendKeys(text);
        }

        public void WaitForElementToBeClickable(By by)
        {
            var wait = new WebDriverWait(webDriver, TimeSpan.FromSeconds(30));
            try {
                wait.Until(ExpectedConditions.ElementToBeClickable(_by));
            } catch (WebDriverTimeoutException w) {
                Console.WriteLine($"Could not find element by {_by.ToString()}.");
                throw w;
            }
        }
    }
}