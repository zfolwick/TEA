namespace Tea
{
    public abstract class ActionStrategy
    {
        protected string _action;
        protected WebBrowser Browser;
        public ActionStrategy(WebBrowser browser, string action)
        {
            _action = action;
            Browser = browser;
        }
        public abstract void Execute(string value);
    }  
}