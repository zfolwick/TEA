using Xunit;
using System;
using System.Collections.Generic;

namespace TeaParser
{
    public class BrowserActionLineTest
    {

        [Fact]
        [Trait("Category", "Unit")]
        public void CanLoadBrowserActionLine()
        {
            var act1 = "click";
            var selector1 = "xpath";
            var locator1 = "//*[text() = 'Log In']";
            var act2 = "type";
            var selector2 = "id";
            var locator2 = "loginUserName";
            var act3 = "click";
            var selector3 = "name";
            var locator3 = "passwordField";
            var act4 = "type";
            var selector4 = "password";
            var locator4 = "//*[@id='password']";
            var list = new List<Token>(){
                new Token(Token.TokenType.TOKEN_ACTION, 0, act1),
                new Token(Token.TokenType.TOKEN_ACTION, 1, selector1),
                new Token(Token.TokenType.TOKEN_ACTION, 2, locator1),
                new Token(Token.TokenType.TOKEN_ACTION, 0, act2),
                new Token(Token.TokenType.TOKEN_ACTION, 1, selector2),
                new Token(Token.TokenType.TOKEN_ACTION, 2, locator2),
                new Token(Token.TokenType.TOKEN_ACTION, 0, act3),
                new Token(Token.TokenType.TOKEN_ACTION, 1, selector3),
                new Token(Token.TokenType.TOKEN_ACTION, 2, locator3),
                new Token(Token.TokenType.TOKEN_ACTION, 0, act4),
                new Token(Token.TokenType.TOKEN_ACTION, 1, selector4),
                new Token(Token.TokenType.TOKEN_ACTION, 2, locator4),
                new Token(Token.TokenType.TOKEN_EOE, 4, null)
            };


            var lines = new BrowserActionLine(list);
            Assert.Equal(String.Join(" ", act1, selector1, locator1), lines.ActionLines[0].ToString());
            Assert.Equal(String.Join(" ", act2, selector2, locator2), lines.ActionLines[1].ToString());
            Assert.Equal(String.Join(" ", act1, selector3, locator3), lines.ActionLines[2].ToString());
            Assert.Equal(String.Join(" ", act2, selector4, locator4), lines.ActionLines[3].ToString());
        }

        [Fact]
        [Trait("Category", "Unit")]
        public void CanInterpretATeaFileInput()
        {
            string url = "https://www.reddit.com/login/?experiment_d2x_sso_login_link=enabled";
            string tc = "Test Case: Trying to login to reddit with a random username/password will fail.";
            // string action1 = "click xpath //*[text() = 'Log In']";
            string action1 = "click xpath //*[@id='loginUsername']";
            string action2 = "type id random";
            string action3 = "click id loginPassword";
            string action4 = "type id password";
            string action5 = "click xpath //fieldset[5]/button";
            
            var tea = new TeaFile(@"./../../../Resources/LoginWithFailingUsername.tea");

            Assert.Equal(url, tea.URL);
            Assert.Equal(tc, tea.TestCase);
            
            // Assert.Equal(action1, tea.ActionLines[0].ToString());
            Assert.Equal(action1, tea.ActionLines[0].ToString());
            Assert.Equal(action2, tea.ActionLines[1].ToString());
            Assert.Equal(action3, tea.ActionLines[2].ToString());
            Assert.Equal(action4, tea.ActionLines[3].ToString());
            Assert.Equal(action5, tea.ActionLines[4].ToString());
                 
        }


    }
}