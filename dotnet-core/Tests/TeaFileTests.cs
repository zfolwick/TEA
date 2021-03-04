using Xunit;

namespace TeaParser
{
    public class StringExtensionsTest
    {
        [Fact]
        [Trait("Category", "Integration")]
        public void CanGetAFile()
        {
            var tea = new TeaFile("./../../../Resources/LoginWithFailingUsername.tea");
            Assert.NotNull(tea);
        }
    }
}