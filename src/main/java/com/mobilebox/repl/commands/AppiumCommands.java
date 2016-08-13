package com.mobilebox.repl.commands;

import static com.mobilebox.repl.commands.CommandsDoc.SEPARATOR;
import static com.mobilebox.repl.misc.Utils.console;
import static com.mobilebox.repl.misc.Utils.prettyXML;
import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.NEW_COMMAND_TIMEOUT;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.aeonbits.owner.ConfigFactory;
import org.dom4j.DocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.mobilebox.repl.config.ConfigCapabilities;
import com.mobilebox.repl.exceptions.CommandsException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;


@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AppiumCommands<T extends AppiumDriver, E extends WebElement> {

  private String udid;
  
  private String app;

  private AppiumDriver<E> driver;

  abstract void start(String deviceName, String udid, String app, String server, String timeout)
      throws CommandsException, MalformedURLException;


  @CommandRef(desc = "Start a new Appium session from ${user.home}/appium.txt file")
  public void start() throws MalformedURLException, CommandsException {
    ConfigCapabilities config = ConfigFactory.create(ConfigCapabilities.class);
    DesiredCapabilities capabilities = new DesiredCapabilities();

    capabilities.setCapability(DEVICE_NAME, config.deviceName());
    capabilities.setCapability(NEW_COMMAND_TIMEOUT, config.cmdTimeout());
    capabilities.setCapability(APP, config.app());
    setApp(config.app());

    if (!config.udid().isEmpty()) {
      capabilities.setCapability(UDID, config.udid());
      setDeviceID(config.udid());
    }

    URL urlServer = new URL(config.appiumServer());

    switch (config.platformName().toLowerCase()) {
      case "android":
        capabilities.setCapability(PLATFORM_NAME, "Android");
        setDriver(new AndroidDriver(urlServer, capabilities));
        break;
      case "ios":
        capabilities.setCapability(PLATFORM_NAME, "iOS");
        setDriver(new IOSDriver(urlServer, capabilities));
        break;
      default:
        throw new RuntimeException(
            "Failed to start session. Please check the appium.txt file. Actual configuration: "
                + config.toString());
    }
  }

  protected T getDriver() {
    if (driver == null) {
      throw new RuntimeException("The driver is null. Please start a new session");
    }
    return (T) driver;
  };

  protected String getDeviceID() {
    return udid;
  };

  protected String getApp() {
    return app;
  };
  
  protected void setDriver(AppiumDriver<E> driver) {
    this.driver = driver;
  }

  protected void setDeviceID(String id) {
    this.udid = id;
  };
  
  protected void setApp(String app) {
    this.app = app;
  };

  @CommandRef(desc = "Terminates the driver instance.")
  public void quit() {
    ((AppiumDriver<E>) getDriver()).quit();
  };

  @CommandRef(desc = "Find elements by ID.", params = {"id - The element id"},
      ret = "A list of elements. This list is empty when no elements are found.")
  public List<E> byId(String id) {
    return findElements(By.id(id));
  }

  @CommandRef(desc = "Find elements by class name.",
      params = {"className - The class property (for example, 'android.widget.Button')"},
      ret = "A list of elements. This list is empty when no elements are found.")
  public List<E> byClassName(String className) {
    return findElements(By.className(className));
  }

  @CommandRef(desc = "Find elements by Xpath.", params = {"xpath -  A Xpath expression"},
      ret = "A list of elements. This list is empty when no elements are found.")
  public List<E> byXpath(String xpath) {
    return findElements(By.xpath(xpath));
  }

  @CommandRef(desc = "Move back")
  public void back() {
    ((AppiumDriver<E>) getDriver()).navigate().back();
  };

  @CommandRef(desc = "Prints the current orientation of a mobile devices desktop.")
  public void getOrientation() {
    console(((AppiumDriver<E>) getDriver()).getOrientation().toString());
  }

  @CommandRef(desc = "Prints the capabilities of the current driver.")
  public void getCapabilities() {
    console(((AppiumDriver<E>) getDriver()).getCapabilities().toString());
  }

  @CommandRef(desc = "Retrieves a XML view of the current screen.",
      ret = "A XML view of the current screen.")
  public String getSource() throws IOException, DocumentException {
    return prettyXML(((AppiumDriver<E>) getDriver()).getPageSource());
  }

  @CommandRef(desc = "Prints the ID of this session.")
  public void getSessionId() {
    console(((AppiumDriver<E>) getDriver()).getSessionId().toString());
  };

  @CommandRef(desc = "Prints all defined Strings from an app for the default language.")
  public void getAppStringMap() {
    console(((AppiumDriver<E>) getDriver()).getAppStringMap().toString());
  };

  @CommandRef(desc = "Prints the device date and time for both iOS and Android devices.")
  public void getDeviceTime() {
    console(((AppiumDriver<E>) getDriver()).getDeviceTime());
  };

  @CommandRef(desc = "Prints the current context.")
  public void getContext() {
    console(((AppiumDriver<E>) getDriver()).getContext());
  };

  @CommandRef(desc = "Prints the available contexts.")
  public void getContextHandles() {
    ((AppiumDriver<E>) getDriver()).getContextHandles().forEach(item -> console(item));
  };

  @CommandRef(desc = "Switch to a new context.",
      params = {"context - The context name."})
  public void context(String context) {
    ((AppiumDriver<E>) getDriver()).context(context);
  };
  
  @CommandRef(desc = "Prints the session details.")
  public void getSessionDetails() {
    console(((AppiumDriver<E>) getDriver()).getSessionDetails().toString());
  };

  @CommandRef(desc = "Hides the keyboard if it is showing.")
  public void hideKeyboard() {
    ((AppiumDriver<E>) getDriver()).hideKeyboard();
  };

  protected List<E> findElements(By locator) {
    List<E> elements = (List<E>) ((T) getDriver()).findElements(locator);
    console("Found: " + elements.size() + " elements" + SEPARATOR);
    for (E element : elements) {
      Point location = element.getLocation();
      Dimension size = element.getSize();
      console("---> Text: " + element.getText());
      console("---> TagName: " + element.getTagName());
      console("---> Enabled: " + element.isEnabled());
      console("---> Selected: " + element.isSelected());
      console("---> Displayed: " + element.isDisplayed());
      console("---> Location: [X=" + location.getX() + " Y=" + location.getY() + "]");
      console(
          "---> Size: [Height=" + size.getHeight() + " Width=" + size.getWidth() + "]" + SEPARATOR);
    }
    return elements;
  }

}
