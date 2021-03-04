using System;
using OpenQA.Selenium;

namespace Tea
{
    public class LogElement : ElementDecorator
    {
        public LogElement(Element element):base(element) {}
        public override By By => Element?.By;

        public override string Text 
        {
            get
            {
                Console.WriteLine($"Element Text = {Element?.Text}");
                return Element?.Text;
            }
        }

        public override bool? Enabled 
        {
            get
            {
                Console.WriteLine($"Element enabled = {Element?.Enabled}");
                return Element?.Enabled;
            }
        }

        public override bool? Displayed
        {
            get
            {
                Console.WriteLine($"Element displayed = {Element?.Displayed}");
                return Element?.Displayed;
            }
        }
        public override void Click()
        {
            Console.WriteLine($"Element clicked: " + Element?.By.ToString());
            Element?.Click();
        }

        public override string GetAttribute(string attributeName)
        {
            Console.WriteLine($"Element's attribute = {Element?.GetAttribute(attributeName)}");
            return Element?.GetAttribute(attributeName);
        }

        public override void TypeText(string text)
        {
            Console.WriteLine($"Type Text = {text}");
            Element?.TypeText(text);        }
    }
}