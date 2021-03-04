package com.tea.framework.SeleniumWrapper.Elements;

import org.openqa.selenium.By;

public class LogElement extends ElementDecorator {
    public LogElement(Element element) {
        super(element);
    }

    public static void write(String message) {
        System.out.println(message);
    }

    public By getBy() {
        return element.by;
    }

    public String getext() {
        write(String.format("Element Text = {%s}", this.element.getText()));
        return this.element.getText();
    }

    public boolean isEnabled() {
        write(String.format("Element enabled = {%s}", this.element.isEnabled()));
        return this.element.isEnabled();
    }

    public boolean isDisplayed() {
        write(String.format("Element displayed = {%s}", this.element.isDisplayed()));
        return this.element.isDisplayed();
    }

    public void Click() {
        write(String.format("Element clicked: " + this.element.getBy().toString()));
        this.element.Click();
    }

    public String GetAttribute(String attributeName) {
        write(String.format("Element's attribute = {%s}", this.element.GetAttribute(attributeName)));
        return this.element.GetAttribute(attributeName);
    }

    public void TypeText(String text) {
        write(String.format("Type Text = {%s}", text));
        this.element.TypeText(text);
    }
}