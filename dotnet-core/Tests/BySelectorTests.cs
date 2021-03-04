using Xunit;
using System.Collections.Generic;
using TeaParser;

namespace Tea
{
    public class BySelectorTests
    {

        [Fact]
        [Trait("Category", "Integration")]
        public void CanSelectFromBrowserActionLine()
        {
            var act1 = "click";
            var selector1 = "xpath";
            var locator1 = "//html";
            var list = new List<Token>(){
                new Token(Token.TokenType.TOKEN_ACTION, 0, act1),
                new Token(Token.TokenType.TOKEN_ACTION, 1, selector1),
                new Token(Token.TokenType.TOKEN_ACTION, 2, locator1),
                new Token(Token.TokenType.TOKEN_EOE, 3, null)
            };
            var browserActionLine = new BrowserActionLine(list);

            WebBrowser browser = new LoggingBrowser(new WebBrowser());
            browser.Start(BrowserList.Chrome);
            // browser.GoTo("https://www.reddit.com");
            DoAction by;
            foreach ( var line in browserActionLine.ActionLines)
            {
                by = new DoAction(ref browser, line.Action);
                ActionStrategy actionStrategy = by.SelectBy(line.By);
                Assert.NotNull(actionStrategy);

            }

            browser.Quit();
        }

    }
}