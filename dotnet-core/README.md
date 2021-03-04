# Purpose
To create and run basic UI tests on webpages. It performs actions on selectors and locators within the webpage's DOM tree that you specify within a .tea file.

# Classes

## Tea
* WebBrowser - A wrapper around selenium WebDriver.
* LoggingBrowser - A logging wrapper around WebBrowser.
* DoAction - The strategy selector for performing an action on an element.

### Action Strategies
Tea provides a selection of strategies for selecting locators the tester specifies in their .tea file.  Currently implemented strategies are:

* ActionStrategy
* TextStrategy
* IdStrategy
* XPathStrategy

## TeaParser
The TeaFile class is responsible for discovering the .tea files and parsing them into 
* TeaFile
* ActionLine