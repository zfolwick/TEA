using Xunit;
using TeaParser;

namespace Tea
{
    public class SeleniumIntegrationTests
    {

        [Fact]
        [Trait("Category", "Integration")]
        public void CanRunASeleniumTest()
        {
            var tea = new TeaFile("./../../../Resources/LoginWithFailingUsername.tea");
            WebBrowser browser = new LoggingBrowser(new WebBrowser());
            browser.Start(BrowserList.Chrome);
            browser.GoTo(tea.URL);
            
            foreach (var line in tea.ActionLines )
            {
                DoAction doAction = new DoAction(ref browser, line.Action);
                ActionStrategy strategy = doAction.SelectBy(line.By);
                strategy.Execute(line.Text);
            }

            browser.Quit();
        }
    }
}