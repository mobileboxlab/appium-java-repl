package com.mobilebox.repl.selectors;

import static java.lang.String.format;

import org.openqa.selenium.By;

import io.appium.java_client.MobileBy;

/**
 * A very simple Android UISelector Expression Builder. Builds some of the most common UISelector
 * expressions. See
 * <a href="http://developer.android.com/tools/help/uiautomator/UiSelector.html" >UiSelector</a>
 */
public abstract class UISelector {

  /**
   * The UISelector String constructor.
   */
  private static final String UISELECTOR_CONSTRUCTOR = "new UiSelector().%s";

  /**
   * Set the search criteria to match the class property for a widget (for example,
   * "android.widget.Button").
   * 
   * @param className The class name
   * 
   * @return the UISelector expression. E.g: new UISelector.className("android.widget.Button").
   */
  public static By className(final String className) {
    return MobileBy
        .AndroidUIAutomator(format(UISELECTOR_CONSTRUCTOR, "className(\"" + className + "\")"));
  }

  /**
   * Set the search criteria to match the given resource ID.
   * 
   * @param id Value to match
   * 
   * @return the UISelector expression. E.g: new UISelector.resourceId("android:id/list")
   */
  public static By resourceId(final String id) {
    return MobileBy
        .AndroidUIAutomator(format(UISELECTOR_CONSTRUCTOR, "resourceId(\"" + id + "\")"));
  }

  /**
   * Set the search criteria to match the content-description property for a widget. The
   * content-description is typically used by the Android Accessibility framework to provide an
   * audio prompt for the widget when the widget is selected. The content-description for the widget
   * must match exactly with the string in your input argument. Matching is case-sensitive.
   * 
   * @param desc Value to match
   * 
   * @return the UISelector expression. E.g: new UISelector.description("my description")
   */
  public static By description(final String desc) {
    return MobileBy
        .AndroidUIAutomator(format(UISELECTOR_CONSTRUCTOR, "description(\"" + desc + "\")"));
  }

  /**
   * Set the search criteria to match the content-description property for a widget. The
   * content-description is typically used by the Android Accessibility framework to provide an
   * audio prompt for the widget when the widget is selected. The content-description for the widget
   * must contain the string in your input argument. Matching is case-insensitive.
   * 
   * @param desc Value to match
   * 
   * @return the UISelector expression. E.g: new UISelector.descriptionContains("my description")
   */
  public static By descriptionContains(final String desc) {
    return MobileBy.AndroidUIAutomator(
        format(UISELECTOR_CONSTRUCTOR, "descriptionContains(\"" + desc + "\")"));
  }

  /**
   * Set the search criteria to match the visible text displayed in a widget (for example, the text
   * label to launch an app). The text for the element must match exactly with the string in your
   * input argument. Matching is case-sensitive.
   * 
   * @param text Value to match
   * 
   * @return the UISelector expression. E.g: new UISelector.text("my text")
   */
  public static By text(final String text) {
    return MobileBy.AndroidUIAutomator(format(UISELECTOR_CONSTRUCTOR, "text(\"" + text + "\")"));
  }

  /**
   * Set the search criteria to match the visible text in a widget where the visible text must
   * contain the string in your input argument. The matching is case-sensitive.
   * 
   * @param text Value to match
   * 
   * @return the UISelector expression. E.g: new UISelector.textContains("my text")
   */
  public static By textContains(final String text) {
    return MobileBy
        .AndroidUIAutomator(format(UISELECTOR_CONSTRUCTOR, "textContains(\"" + text + "\")"));
  }

  /**
   * Chaining the search criteria on "new UiSelector()." Useful for to distinguish similar elements
   * based in the hierarchies they're in.
   * 
   * @param locator Value to match
   * @return the UISelector expression. E.g: new
   *         UiSelector().className("android.widget.RelativeLayout" ).enabled(true).instance(0);
   */
  public static By selectorChaining(final String locator) {
    return MobileBy.AndroidUIAutomator(format(UISELECTOR_CONSTRUCTOR, locator));
  }
}
