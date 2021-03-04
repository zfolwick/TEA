using OpenQA.Selenium;
using Xunit;

namespace Tea
{
    public class WebdriverTests
    {
        [Fact]
        public void CanCreateAWebDriver()
        {
            // Test assumes that chromedriver is installed and on the PATH, and able to be run.
            WebBrowser b = new WebBrowser();
            b.Start(BrowserList.Chrome);
            Assert.NotNull(b.GetDriver());
            b.Quit();
        }
        [Fact]
        public void CanCreateALoggingBrowser()
        {
            // Test assumes that chromedriver is installed and on the PATH, and able to be run.
            WebBrowser b = new LoggingBrowser(new WebBrowser());
            b.Start(BrowserList.Chrome);
            Assert.NotNull(b.GetDriver());
            b.Quit();
        }

        [Fact]
        public void CanNavigateThroughAUserFlow()
        {

            WebBrowser browser = new LoggingBrowser(new WebBrowser());
            browser.Start(BrowserList.Chrome);
            browser.GoTo("https://www.reddit.com");
            By loginBtnBy = By.XPath("//*[text() = 'Log In']");
            browser.FindElement(loginBtnBy).Click();
            browser.Quit();
        }

        [Fact(Skip = "skipping this as it doesn't seem to be reliably passing.")]
        public void iFramesTest()
        {
            By loginBtnBy = By.XPath("//*[text() = 'Log In']");
            By loginTxtBy = By.XPath("//*[@id='loginUsername']");

            string url = "http://www.reddit.com";
            WebBrowser browser = new LoggingBrowser(new WebBrowser());
            browser.Start(BrowserList.Chrome);
            browser.GoTo(url);

            browser.FindElement(loginBtnBy).Click();
            
            Element usernameTextField = browser.FindElement(loginTxtBy);

            usernameTextField?.TypeText("derp");

            browser.Quit();
        }

        
    }
}