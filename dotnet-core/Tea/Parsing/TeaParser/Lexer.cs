/*
I need to be capable of writing a parser that can parse a file containing html DOM element attribute type, an action to perform on them, and the DOM identifier to use when looking for them.
e.g.,
# here is a comment.
<# this is a block comment.
it will keep being ignored by the compiler until it finds the ending comment.
#>

http://www.example.com

Test Case: This test tries to log in using a random username.
click   xpath   //*[text() = 'Log In']
click   xpath   //*[@id='loginUsername']
type    id      random
# here's another comment   <# it's possible to block comment on the same line #>
click   id      loginPassword
type    id      password
click   xpath   //fieldset[5]/button


Test cases are defined in ACTION    SELECTOR    LOCATOR and can be read as: "perform an ACTION using a SELECTOR on a LOCATION on the webpage under test".

ACTION
The second column represents actions that can be taken on an element.  Actions that are currently supported are: click, type.  Soon asserts will be added.

SELECTOR
column 1 is the DOM element attribute to look for.  In the first case, we're looking for xpath, the second case we're looking for id.  Selenium has support for: css class, css id, text, xpath, tagname, name, partialLinkText, and this is what the first column represents.
This maps to the selenium By.{selector}

LOCATOR
The 3rd column is the value that should be used with the selector. For selectors of type 'xpath', the Locator should be a valid xpath.

Each line is a token type.
- lines starting with "http://" and "https://" are a TOKEN_URL and the rest of the url is part of the token
- lines starting with "Test Case:" are a TOKEN_TC_DESC and the rest of the line is part of the token
- lines starting with # are a comment and the token is the rest of the line
- text with <# and #> are block comments and are of type TOKEN_BLK_COMMENT.  the comment is the token value
- text that is the first on the line, and consists of: id, text, xpath, tagname, name, partialLinkText, and ends with a tab or a space are of type TOKEN_SELECTOR
- text that has a tab or space before and after, and the text consists of "click" and "type" are of type TOKEN_ACTION.  asserts will be added later.
- text that follows that is of type TEXT_LOCATOR
*/

using System;
using System.Collections.Generic;
using System.Text;

namespace TeaParser
{

    /*
    This lexer turns a string expression into a token.
    */
    public class Lexer
    {
        static readonly Dictionary<string, Func<int, string, Token>> map = new Dictionary<string, Func<int, string, Token>>
        {
            {"click", (p,v) => new Token(Token.TokenType.TOKEN_ACTION, p, v) }, 
            {"type", (p,v) => new Token(Token.TokenType.TOKEN_ACTION, p, v) }, 
            {"xpath", (p,v) => new Token(Token.TokenType.TOKEN_SELECTOR, p, v) },
            {"id", (p,v) => new Token(Token.TokenType.TOKEN_SELECTOR, p, v) },
            {"tagname", (p,v) => new Token(Token.TokenType.TOKEN_SELECTOR, p, v) },
            {"text", (p,v) => new Token(Token.TokenType.TOKEN_SELECTOR, p, v) },
            {"class", (p,v) => new Token(Token.TokenType.TOKEN_SELECTOR, p, v) },
            {Environment.NewLine, (p,v) => new Token(Token.TokenType.TOKEN_NEWLINE, p, v) },  //XPATH 

        };
        Stack<Token> tokens = new Stack<Token>();
        readonly SourceScanner _scanner;

        public Lexer(SourceScanner source)
        {
            _scanner = source;
        }

        public Token ReadNext()
        {
            Token token = null;

            //- lines starting with # are a comment and the token is the rest of the line
            // RemoveComments();
            TokenizeLineComments(ref token);
            if (token != null) 
            {
                tokens.Push(token);
                return token;
            }

            // deal with EOE
            if (_scanner.AtEndOfSource)
            {
                token = new Token(Token.TokenType.TOKEN_EOE, (tokens.Count == 0) ? _scanner.Position : tokens.Pop().Position + 1, null);
            }

            else if (_scanner.Peek() != null && map.ContainsKey(_scanner.Peek()))
                token = map[_scanner.Peek()](_scanner.Position, _scanner.Read());

            else if (isXpath() || 
                    (tokens.Count > 0 && tokens.Peek().Type.Equals(Token.TokenType.TOKEN_SELECTOR)))
                TokenizeLocator(ref token);
                
            //- lines starting with "http://" and "https://" are a TOKEN_URL and the rest of the url is part of the token
            else if (isUrl())
                token = new Token(
                    Token.TokenType.TOKEN_URL, _scanner.Position, _scanner.Read());

            //- lines starting with "Test Case:" are a TOKEN_TC_DESC and the rest of the line is part of the token

            else if (isTestCase())
                TokenizeTestCaseLine(ref token);

            if ( token == null )
                throw new Exception($"could not parse {_scanner.Peek()} at position {_scanner.Position}.");
            tokens.Push(token);
            
            return token;
        }

        private void TokenizeLocator(ref Token token)
        {
            // handle space
            var sb = new StringBuilder();
            var startpos = _scanner.Position;
                
            while (_scanner != null && !_scanner.AtEndOfSource )
            { 
                if ( _scanner.Peek().Contains("#")) break;
                // append a string to the xpath.
                sb.Append(" ");
                var next = _scanner.Peek();
                // xpath has quotes in it.
                if (next.Contains("'"))
                {
                    while ( _scanner.Peek() != null && _scanner.Peek() != "#")
                    {
                        sb.Append(_scanner.Read()).Append(" ");
                    }
                }

                if ( _scanner.Peek() != "#" )
                    sb.Append(_scanner.Read());
                else break;
            }
            token = new Token(
                    Token.TokenType.TOKEN_LOCATOR, startpos,sb.ToString().Trim());
        }

        private void TokenizeTestCaseLine(ref Token token)
        {
            StringBuilder sb;
            int startpos;
            ConsumeRestOfLineIntoToken(out sb, out startpos);

            token = new Token(Token.TokenType.TOKEN_TC_DESC, startpos, sb.ToString().Trim());
        }

        private void ConsumeRestOfLineIntoToken(out StringBuilder sb, out int startpos)
        {
            var peek = _scanner.Peek();
            sb = new StringBuilder();
            startpos = _scanner.Position;
            while (peek != null && !peek.Contains(Environment.NewLine))
            {
                sb.Append(" ").Append(_scanner.Read());
                peek = _scanner.Peek();
            }
            if ( peek != null ) sb.Append( " ").Append(peek);
        }

        private bool isTestCase()
        {
            _scanner.Push();
            var current = _scanner.Read();
            var next = _scanner.Read();
            _scanner.Pop();
            // 
            return _scanner.Peek() != null && 
                _scanner.Peek().StartsWith("Test") && 
                next.StartsWith("Case:");
            // the next word after "Test" is "Case".  If not, don't mutate the position.
        }
        private bool isUrl()
        {
            return _scanner.Peek() != null && _scanner.Peek().StartsWith("http://") || _scanner.Peek().StartsWith("https://");
        }

        private void TokenizeLineComments(ref Token t)
        {
            var peek = _scanner.Peek();
            if (peek != null && peek.StartsWith("#"))
            {
                 StringBuilder sb;
                 int startpos;
                ConsumeRestOfLineIntoToken(out sb, out startpos);
                startpos = tokens.Count;

                t = new Token(
                    Token.TokenType.TOKEN_LINE_COMMENT, startpos, sb.ToString().Trim());
            }
        }

        private bool isXpath()
        {
            return _scanner.Peek() != null & _scanner.Peek().StartsWith("//") && tokens.Peek().Value == "xpath";
        }
    }

}