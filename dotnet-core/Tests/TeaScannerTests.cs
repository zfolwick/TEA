using Xunit;
using System;

namespace TeaParser
{
    public class TeaSourceScannerTests
    {
        string _expression = "#here is a comment.";
        public SourceScanner _scanner;

        public TeaSourceScannerTests() => _scanner = new SourceScanner(_expression);

    
        [Fact]
        public void CanPeekExpressionTest()
        {

            string expectedString = _expression.Split(" ")[0];
            string actualString = _scanner.Peek();
            Assert.False( _scanner.AtEndOfSource);

            Assert.True( expectedString == actualString,
            $"unexpected character. expected character \"{expectedString}\", got \"{actualString}\" ");
        }
        [Fact]
        public void CanReadExpressionTest()
        {

            string[] expectedString = _expression.Split(" ");
            string actualString = _scanner.Read();
            Assert.False( _scanner.AtEndOfSource);

            Assert.True( expectedString[0] == actualString,
            $"unexpected string. expected \"{expectedString}\", got \"{actualString}\" ");
        }
        
        [Fact]
        public void CanFindTheEndOfExpression()
        {
            Assert.False(_scanner.AtEndOfSource);
            Assert.Equal(0, _scanner.Position);
            foreach ( var c in _expression )
                _scanner.Read();

            Assert.True(_scanner.AtEndOfSource);
            Assert.Equal(_expression.Split(" ").Length, _scanner.Position);
            Assert.Equal(null, _scanner.Read());
        }
        [Fact]
        public void CanPeek()
        {
            char expectedchar = _expression.ToCharArray()[0];
            Assert.False(_scanner.AtEndOfSource);
            Assert.Equal(0, _scanner.Position);

            _scanner.Peek();

            Assert.False(_scanner.AtEndOfSource);
            Assert.Equal(0, _scanner.Position);
        }
        [Fact]
        public void CanParseNewLines()
        {
            string firstline = "#here is a comment";
            string secondline = "click";
            string expression = firstline + Environment.NewLine + secondline;
            string[] expectedExpressions = expression.Split(Environment.NewLine);

            Assert.False(_scanner.AtEndOfSource);
            Assert.Equal(0, _scanner.Position);
            string actualString = _scanner.Read();

            Assert.True( expectedExpressions[0] == firstline,
                $"Expected the first line to be \"{firstline}\". got {expectedExpressions[0]} instead.");

        }
        [Fact]
        public void CanPush()
        {

            Assert.False( _scanner.AtEndOfSource );
            Assert.Equal( 0, _scanner.Position );

            _scanner.Push();
            foreach ( var t in _expression.Split() )
                Assert.Equal( t, _scanner.Read() );
            
            Assert.True( _scanner.AtEndOfSource );

            _scanner.Pop();
            
            Assert.False( _scanner.AtEndOfSource );
            Assert.Equal( 0, _scanner.Position );

        }
    }
}