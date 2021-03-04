using Xunit;
using System;

namespace CustomExtensions
{
    public class StringExtensionsTest
    {
        [Fact]
        public void CanGetARandomString()
        {
            string first = "".GetRandomString(10);
            string second = "".GetRandomString(10);
            Console.WriteLine($"Strings should be random: First: {first} ; Second: {second}.");
            Assert.NotEqual(first, second);
        }
        [Fact]
        public void CanSpecifyLength()
        {
            int expectedLength1 = 8;
            string first = "".GetRandomString(expectedLength1);
            int length1 = first.Length;
            int expectedLength2 = 22;
            string second = "".GetRandomString(22);
            int length2 = second.Length;
            Assert.NotEqual(length1, length2);
            Assert.True(length1 == expectedLength1, $"Expected {length1} to be {expectedLength1}");
            Assert.True(length2 == expectedLength2, $"Expected {length2} to be {expectedLength2}");
            Assert.False(first.Equals(second));
            Assert.False( first == second );
        }
    }
}