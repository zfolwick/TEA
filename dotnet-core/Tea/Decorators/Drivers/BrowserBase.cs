using System.Collections.Generic;
using OpenQA.Selenium;

namespace Tea
{
    public enum BrowserList
    {
        Chrome,
        Firefox,
        IE,
        Edge,
        Opera
    }

    public abstract class BrowserBase
    {
        public abstract void Start(BrowserList browser);
        public abstract void Quit();
        public abstract void GoTo(string url);
        public abstract Element FindElement(By locator);
        public abstract List<Element> FindElements(By locator);
        public abstract Element FindElementInFrames(By by);
        public abstract List<Element> FindAllFrames();
        public abstract Element GetActiveElement();
        public abstract IWebDriver GetDriver();
    }
}
