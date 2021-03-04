using System;
using TeaParser;

namespace Tea
{
    class Program
    {
        private const int ERROR_BAD_ARGUMENTS = 2;
        private const int ERROR_INVALID_COMMAND_LINE = 1;

       
        static void Main(string[] args)
        {
            /* This tests the ability to act on a basic CSV file. */
            // BasicActionsTest(@"Resources/Actions.csv");
            /* This tests the framework's ability to run against a page who's elements take a while to load. */
            // IframeTest(@"Resources/CreateAccount.csv");
            /* This tests the ability of the framework to read assertions from the document. */
            // AssertionTest();
            // Lexer();
            // LexingWithAFile();
            // SeleniumTest();

            if ( args.Length == 0 )
            {
                PrintUsage();
                Console.WriteLine("Error: {0}", ERROR_INVALID_COMMAND_LINE);
                Environment.Exit(ERROR_INVALID_COMMAND_LINE);
            }

            if ( args.Length > 1 )
            {
                Console.WriteLine("At this time only a single .tea file is supported natively.");
                PrintUsage();
                Console.WriteLine("Error: {0}", ERROR_BAD_ARGUMENTS);
                Environment.Exit(ERROR_INVALID_COMMAND_LINE);
            }

            if ( args.GetValue(0).ToString().EndsWith(".tea") )
            {
                Console.WriteLine("Parsing tea file... Buckle up!");
                SeleniumTest(args.GetValue(0)?.ToString());
            }
        }

        private static void PrintUsage()
        {
            string EXPECTED_DATA_MESSAGE = 
            @"Need a .tea file passed in as an argument; e.g., 
            
$ tea {path-to-tea-file}";
            Console.WriteLine(EXPECTED_DATA_MESSAGE);
        }
        private static void SeleniumTest(string path)
        {
            string testFilePath = "Resources/LoginWithFailingUsername.tea";
            var tea = new TeaFile(path);
            WebBrowser browser = new LoggingBrowser(new WebBrowser());
            browser.Start(BrowserList.Chrome);
            browser.GoTo(tea.URL);
            
            try
            {
                foreach (var line in tea.ActionLines )
                {
                    new DoAction(ref browser, line.Action).SelectBy(line.By)
                                                        .Execute(line.Text);
                }
            } finally {
                browser.Quit();
            }
        }

        private static void LexingWithAFile()
        {
            var tea = new TeaFile("Resources/LoginWithFailingUsername.tea");
            Console.WriteLine(tea.URL);
            Console.WriteLine(tea.TestCase);
            tea.ActionLines.ForEach(line => Console.WriteLine(line.ToString()));
        }

        private static void Lexer()
        {
            string url = "http://www.example.com";
            string tc = "Test Case: Trying to login to reddit with a random username/password will fail.";
            string action1 = "  click   xpath   //*[text() = 'Log In'] # Click login button";
            string action2 = "  click   xpath   //*[@id='loginUsername']";
            string action3 = "  type    id      random";
            string action4 = "  click   id      loginPassword";
            string action5 = "  type    id      password";
            string action6 = "  click   xpath   //fieldset[5]/button";
            
            PrintLineToken(url);
            PrintLineToken(tc);
            PrintLineToken(action1);
            PrintLineToken(action2);
            PrintLineToken(action3);
            PrintLineToken(action4);
            PrintLineToken(action5);
            PrintLineToken(action6);
        }

        private static void PrintLineToken(string expression)
        {
            var lexer = new TeaParser.Lexer(new TeaParser.SourceScanner(expression));
            TeaParser.Token token = lexer.ReadNext();
            while ( token != null && token.Type != TeaParser.Token.TokenType.TOKEN_EOE )
            {
                Console.WriteLine(
                    $"( type: {token.Type}  position: {token.Position} value: {token.Value} )");
                token = lexer.ReadNext();
            }
        }

        private static void AssertionTest()
        {
            /*
            * TODO:
             * need a new syntactic element: the assertion.
             * Typically  an assertion on a page has a few forms:
             * 1. "elements  found by this locator  are equal to something I care about"
             * 2. "elements  found by this locator  have this property that is equal to something I care about"
             *
             * To make this work  a _real_ lexer must be built.
             */

        }
    }
}
