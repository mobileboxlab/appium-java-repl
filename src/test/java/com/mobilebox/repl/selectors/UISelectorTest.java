package com.mobilebox.repl.selectors;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.mobilebox.repl.selectors.UISelector;

import static org.assertj.core.api.Assertions.*;

public class UISelectorTest {

  @Test
  public void classNameTest() {
    String expected = "className(\"my.classname\")";
    By locator = UISelector.className("my.classname");
    assertThatByIsEqualTo(locator.toString(), expected);
  }

  @Test
  public void descriptionTest() {
    String expected = "description(\"desc\")";
    By locator = UISelector.description("desc");
    assertThatByIsEqualTo(locator.toString(), expected);
  }

  @Test
  public void descriptionContainsTest() {
    String expected = "descriptionContains(\"desc\")";
    By locator = UISelector.descriptionContains("desc");
    assertThatByIsEqualTo(locator.toString(), expected);
  }

  @Test
  public void resourceIdTest() {
    String expected = "resourceId(\"id\")";
    By locator = UISelector.resourceId("id");
    assertThatByIsEqualTo(locator.toString(), expected);
  }

  @Test
  public void selectorChainingTest() {
    String expected = "resourceId(\"id\").enabled(true).instance(0)";
    By locator = UISelector.selectorChaining(expected);
    assertThatByIsEqualTo(locator.toString(), expected);
  }

  @Test
  public void textTest() {
    String expected = "text(\"text\")";
    By locator = UISelector.text("text");
    assertThatByIsEqualTo(locator.toString(), expected);
  }

  @Test
  public void textContainsTest() {
    String expected = "textContains(\"text\")";
    By locator = UISelector.textContains("text");
    assertThatByIsEqualTo(locator.toString(), expected);
  }

  private void assertThatByIsEqualTo(final String actual, final String expected) {
    String msg = "By.AndroidUIAutomator: new UiSelector().";
    assertThat(actual).isEqualToIgnoringCase(msg + expected);
  }
}
