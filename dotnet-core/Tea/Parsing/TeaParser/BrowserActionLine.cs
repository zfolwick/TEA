
namespace TeaParser
{
    using System;
    using System.Collections.Generic;

    public class ActionLine
    {
        public string By { get; private set; }
        public string Action { get; private set; }
        public string Text { get; private set; }
        public ActionLine(string[] line)
        {
            Action = line[0];
            By = line[1];
            Text = line[2];
        }


        public void PrintLine()
        {
            Console.WriteLine($" {Action}\t{By}\t{Text}");
        }

        public override string ToString()
        {
            return $"{Action} {By} {Text}";
        }
    }
    public class BrowserActionLine
    {
        // deprecating these three fields in favor of ActionLine class.
        private string by;
        private string action;
        private string text;

        public List<ActionLine> ActionLines { get; private set; }

        public BrowserActionLine(Tuple<string, string, string> line)
        {
            by = line.Item1;
            action = line.Item2;
            text = line.Item3;
        }
        public BrowserActionLine(List<Token> actionTokens)
        {
            ActionLines = new List<ActionLine>();

            int lineIndex = 0;
            string[] line = new string[3];
            foreach (var t in actionTokens)
            {
                if (lineIndex > 0 && lineIndex % 3 == 0)
                {
                    lineIndex = 0;
                    ActionLines.Add(new ActionLine(line));
                }
                // assign the actions
                // assign the selectors
                // assign the locators
                if (t.Type == Token.TokenType.TOKEN_ACTION ||
                     t.Type == Token.TokenType.TOKEN_SELECTOR ||
                     t.Type == Token.TokenType.TOKEN_LOCATOR)
                {
                    line[lineIndex++] = t.Value;
                    continue;
                }
            }
            // add the final line
            ActionLines.Add(new ActionLine(line));

          
        }
        // these methods are deprecated and will be removed.
        public String By { get => by; }
        public String Action { get => action; }
        public String Text { get => text; }
    }
}