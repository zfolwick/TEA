using OpenQA.Selenium;

namespace Tea
{
    public class XPathStrategy : ActionStrategy
    {
        public XPathStrategy(WebBrowser browser, string action) : base(browser, action){}

        public override void Execute(string value)
        {
            Action act = null;

            By by = By.XPath(value);
            switch (_action)
            {
                case SelectorOptions.Action.Click: act = new Click(by); break;
                case SelectorOptions.Action.TypeText: act = new TypeText(by); break;
                default: throw new System.Exception("Invalid action chosen! " + _action);
            }
            act.Perform(Browser, value);
        }
    }
}