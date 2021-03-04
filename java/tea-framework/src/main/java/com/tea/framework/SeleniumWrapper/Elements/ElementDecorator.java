package com.tea.framework.SeleniumWrapper.Elements;

public class ElementDecorator extends Element {
    protected Element element;

    public ElementDecorator(Element element) {
        this.element = element;
        this.text = element.getText();
        this.by = element.by;
        this.enabled = element.isEnabled();
        this.displayed = element.isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    @Override
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public void Click() {
        element.Click();
    }

    @Override
    public String GetAttribute(String attributeName) {
        return element.GetAttribute(attributeName);
    }

    @Override
    public void TypeText(String text) {
        element.TypeText(text);
    }
}