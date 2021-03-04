/*
    A teafile is a text file ending in .tea.  Teafiles will contain all the information required to run selenium test automation and actions. The Teafile class stores all the relevant information for the selenium architecture to run.

    A teafile consists of a url, test case keyword, and a series of actions- e.g.,

    /// LoginWithAFailingUsername.tea
    http://www.reddit.com

    Test Case: Trying to login to reddit with a random username/password will fail.
        click   xpath   //*[text() = 'Log In'] # Click login button
        click   xpath   //*[@id='loginUsername']
        type    id      random
        click   id      loginPassword
        type    id      password
        click   xpath   //fieldset[5]/button
*/

using System.Collections.Generic;

namespace TeaParser
{

    public class TeaFile
    {
        private string Path {get; set;}
        public List<Token> Tokens {get; private set;}
        public string URL { get; private set; }
        public string TestCase { get; private set; }
        public BrowserActionLine Lines {get; private set; }
        public List<ActionLine> ActionLines {
             get
             {
                  return Lines.ActionLines;
             }
            private set {} }
        public TeaFile(string path)
        {
            Path = path;
            Tokens = new List<Token>();
            ReadFile();
            ParseTokens();
        }

        private void ReadFile()
        {
            // Read the file and display it line by line.  
            System.IO.StreamReader file = new System.IO.StreamReader(Path);  
            string line;
            while((line = file.ReadLine()) != null)
            {
                var lexer = new TeaParser.Lexer(new TeaParser.SourceScanner(line));
                
                Token token = lexer.ReadNext();
                while ( token != null && token.Type != Token.TokenType.TOKEN_EOE)
                {
                    Tokens.Add(token);
                    token = lexer.ReadNext();
                }
                
            }

            file.Close();
        }

        private void ParseTokens()
        {
            
            foreach (var t in Tokens)
            {
                if ( t.Value != null && t.Value.Length > 0)
                {
                    // assign Url
                    if ( t.Type == Token.TokenType.TOKEN_URL ) { URL = t.Value; continue; }

                    // assign testCase
                    if ( t.Type == Token.TokenType.TOKEN_TC_DESC ) { TestCase = t.Value; continue; }
                }
            }
            Lines = new BrowserActionLine(Tokens);
        }
        
    }
}