namespace Tea
{
    public class DoAction
        {
            private WebBrowser _browser;
            private string _actionToPerform;
            
            public DoAction(ref WebBrowser browser, string action)
            {
                _browser = browser;
                _actionToPerform = action;
            }

            public ActionStrategy SelectBy(string by)
            {
                switch (by)
                {
                    case SelectorOptions.By.Text:
                        return new TextStrategy(_browser, _actionToPerform);

                    case SelectorOptions.By.Id:
                        return new IdStrategy(_browser, _actionToPerform);

                    case SelectorOptions.By.Xpath:
                        return new XPathStrategy(_browser, _actionToPerform);

                    default: 
                        throw new System.Exception($"Invalid By selector chosen: {by}");
                }
            }
        }
}