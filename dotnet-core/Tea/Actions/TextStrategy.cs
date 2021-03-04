using CustomExtensions;
using OpenQA.Selenium;

namespace Tea
{
    public class TextStrategy : ActionStrategy
    {

        public TextStrategy(WebBrowser browser, string action) : base(browser, action) {}
        public override void Execute(string value)
        {
            Action act = null;

            By by = By.LinkText(value);
            switch (_action)
            {
                case SelectorOptions.Action.Click: act = new Click(by); break;
                case SelectorOptions.Action.TypeText: act = new TypeText(by); break;
                default: throw new System.Exception("Invalid action chosen!");
            }

            act.Perform(Browser, value);
        }
    }
}