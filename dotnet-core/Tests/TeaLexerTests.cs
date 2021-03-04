using Xunit;
using System;

namespace TeaParser
{

    public class TeaLexerTest
    {
        [Theory]
        [InlineData("#here is a comment")]
        [InlineData("#here is a comment with a keyword click")]
        public void CanTokenizeAComment(string expression)
        {
            (Token.TokenType, int, string)[] expectedTokens 
                = new (Token.TokenType, int, string)[] { 
                    (Token.TokenType.TOKEN_LINE_COMMENT, 0, expression),
                    (Token.TokenType.TOKEN_EOE, 1, null)

                };

            AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));

        }
       
        [Theory]
        [InlineData("click", "xpath", "//iframe")]
        [InlineData("click", "xpath", "//*[text() = 'Log In']")]
        [InlineData("click", "id", "loginBtn")]
        [InlineData("click", "tagname", "input")]
        [InlineData("click", "text", "'the sun will come out tomorrow'")]
        [InlineData("click", "class", "codesnippet")]
        [InlineData(" click", "class", "codesnippet")] // space before 
        [InlineData(" click", " class", "codesnippet")] // space before 
        [InlineData(" click", "class", " codesnippet")] // space before 
        [InlineData("\tclick", "class", "codesnippet")] // tabs before 
        [InlineData("click", "\tclass", "codesnippet")] // tabs before 
        [InlineData("click", "class", "\tcodesnippet")] // tabs before 
        [InlineData("\tclick", "\tclass", "\tcodesnippet")] // tabs everywhere

        public void CanTokenizeAnActionLine(string action, string selector, string locator)
        {
            string expression = String.Join(" ", new String[] {action.Trim(), selector.Trim() , locator.Trim()});
            (Token.TokenType, int, string)[] expectedTokens 
                = new (Token.TokenType, int, string)[] { 
                    (Token.TokenType.TOKEN_ACTION, 0, action.Trim()),
                    (Token.TokenType.TOKEN_SELECTOR, 1, selector.Trim()),
                    (Token.TokenType.TOKEN_LOCATOR, 2, locator.Trim()),
                    (Token.TokenType.TOKEN_EOE, 3, null)

                };
            AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
        }

        [Theory]
        [InlineData("click",  "xpath",  "//iframe/div",  "# here is a comment")]
        [InlineData("click", "xpath", "//*[text() = 'Log In']", "# here is a comment")]
        public void CanTokenizeAComment_EndOfALine(string action,
                                                   string selector,
                                                   string locator,
                                                   string comment)
        {
            string expression = String.Join(" ",
                new String[] { action, selector, locator, comment });
            (Token.TokenType, int, string)[] expectedTokens 
                = new (Token.TokenType, int, string)[] { 
                    (Token.TokenType.TOKEN_ACTION, 0, action),
                    (Token.TokenType.TOKEN_SELECTOR, 1, selector),
                    (Token.TokenType.TOKEN_LOCATOR, 2, locator),
                    (Token.TokenType.TOKEN_LINE_COMMENT, 3, comment),
                    (Token.TokenType.TOKEN_EOE, 4, null),
            };

            AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
        }
        
        private static void AssertTokensEqual((Token.TokenType, int, string)[] expectedTokens, Lexer lexer)
        {
            foreach (var (t, p, v) in expectedTokens)
            {
                var token = lexer.ReadNext();
                Assert.Equal(t, token.Type);
                Assert.Equal(p, token.Position);
                Assert.Equal(v, token.Value);
            }
        }
        [Theory]
        [InlineData("http://www.example.com")]
        [InlineData("https://www.example.com")]
        public void CanTokenizeAUrl(string expression)
        {
            (Token.TokenType, int, string)[] expectedTokens 
                = new (Token.TokenType, int, string)[] { 
                    (Token.TokenType.TOKEN_URL, 0, expression),
                    (Token.TokenType.TOKEN_EOE, 1, null),
                };


            AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
        }
        [Fact]
        public void CanTokenizeATestCase()
        {
            string expression = "Test Case: This is a Test Case";
            (Token.TokenType, int, string)[] expectedTokens 
                = new (Token.TokenType, int, string)[] { 
                    (Token.TokenType.TOKEN_TC_DESC, 0, expression)
                };

            var lexer = new Lexer(new SourceScanner(expression));
            
            AssertTokensEqual(expectedTokens, lexer);
        }
        [Fact(Skip = "This test cannot run until the lexer can handle multiple lines.")]
        public void CanTokenizeABlockComment()
        {
            string startOfBlock = "<# Test Case: This is a Test Case";
            string endOfBlock = "Test Case: This is a Test Case #>";
            // block comments might come in different expressions. If a TOKEN_LF_COMMENT is seen, then the following tokens are all of type Token.TokenType.TOKEN_LINE_COMMENT until a token of tokentype TOKENTYPE.TOKEN_RT_COMMENT
            (Token.TokenType, int, string)[] expectedTokens 
                = new (Token.TokenType, int, string)[] { 
                    (Token.TokenType.TOKEN_LF_COMMENT, 0, "<#"),
                    (Token.TokenType.TOKEN_LINE_COMMENT, 0, startOfBlock.Remove(0,3)),
                    (Token.TokenType.TOKEN_RT_COMMENT, 0, endOfBlock.Remove(endOfBlock.Length-2,endOfBlock.Length)),

                };

            // var lexer = new Lexer(new SourceScanner(expression));
            
            // AssertTokensEqual(expectedTokens, lexer);

        }

        [Fact]
        public void CanTokenizeTagName()
        {

        }
    }
}