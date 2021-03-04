using CustomExtensions;
using OpenQA.Selenium;

namespace Tea
{
    public abstract class Action
    {
        protected By _by;
        public Action(By by) => _by = by;
        public abstract void Perform(WebBrowser browser, string? value);
    }

    public class Click : Action
    {
        
        public Click(By by) : base(by) {}
        public override void Perform(WebBrowser browser, string? value)
        {
            Element element = browser.FindElement(_by);
            element.Click();
        }
    }

    public class TypeText : Action
    {
        public TypeText(By by) : base(by) {}
        public override void Perform(WebBrowser browser, string value)
        {
            if ( value == "random")
            {
                browser.GetActiveElement().TypeText(value.GetRandomString(10).Replace("\"", ""));

            } else {
                browser.GetActiveElement().TypeText(value.Replace("\"", ""));
                
            }
        }
    }
}