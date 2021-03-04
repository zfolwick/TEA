using System;
using System.Collections.Generic;
namespace TeaParser
{
    public class SourceScanner
    {
        // the expression to parse
        readonly string? _buffer;
        readonly string[] _bufferArray;
        //
        readonly Stack<int> _positionStack = new Stack<int>();
        // the current position within the buffer.
        public int Position {get; set;}
        // are we at the end of the source?
        public bool AtEndOfSource => Position >= _bufferArray.Length;
        public SourceScanner(string source){
            _buffer = source;
            string[] splitOn = new String[] {" "};
            _bufferArray = _buffer.Split(splitOn, StringSplitOptions.RemoveEmptyEntries);
        }
        // Reads the next character in the buffer.
        public string Read()
        {
            if (AtEndOfSource)
            {
                return null;

            } else
            {
                return _bufferArray[Position++];
            }
            
        }
        ///<summary>
        /// peeks at the next character in the buffer without incrementing the position.
        ///</summary>
        public string Peek() => Position >= _bufferArray.Length ? null:  _bufferArray[Position];

        public void Push() => _positionStack.Push(Position);

        public void Pop() => Position = _positionStack.Pop();
    }
}