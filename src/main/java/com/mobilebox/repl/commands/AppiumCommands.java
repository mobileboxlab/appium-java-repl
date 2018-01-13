package com.mobilebox.repl.commands;

import static com.mobilebox.repl.commands.CommandsDoc.SEPARATOR;
import static com.mobilebox.repl.misc.Utils.console;
import static com.mobilebox.repl.misc.Utils.prettyXML;
import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.NEW_COMMAND_TIMEOUT;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;
import static java.lang.String.format;
import static java.lang.System.getProperty;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.aeonbits.owner.ConfigFactory;
import org.dom4j.DocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.mobilebox.repl.Appium;
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
  private final String ENGINE_NAME = "nashorn";
  private boolean logElement = true;

  abstract void start(String deviceName, String udid, String app, String server, String timeout)
      throws CommandsException, MalformedURLException;

  @CommandRef(desc = "Start a new Appium session from ${user.home}/appium.txt file")
  public void start() throws MalformedURLException, CommandsException {
    try {
      ConfigCapabilities config = ConfigFactory.create(ConfigCapabilities.class);
      DesiredCapabilities caps = new DesiredCapabilities();

      caps.setCapability(DEVICE_NAME, config.deviceName());
      caps.setCapability(NEW_COMMAND_TIMEOUT, config.cmdTimeout());
      caps.setCapability(APP, config.app());
      setApp(config.app());

      if (!config.udid().isEmpty()) {
        caps.setCapability(UDID, config.udid());
        setDeviceID(config.udid());
      }

      URL urlServer = new URL(config.appiumServer());
      setPlatformName(config.platformName(), urlServer, caps);

    } catch (Exception e) {
      throw new RuntimeException(
          format("An error has occurred: [%s] Please check the appium.txt file on: [%s]",
              e.getMessage(), getProperty("user.home")));
    }
  }

  @CommandRef(
      desc = "Start a new Appium session given Nashorn (JS) script file with DesiredCapabilities.",
      params = {"path - The script full path."}, ret = "")
  public void start(final String path) {
    final ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName(ENGINE_NAME);

    try {
      engine.eval(new FileReader(path));
      Invocable invocable = (Invocable) engine;
      DesiredCapabilities caps = (DesiredCapabilities) invocable.invokeFunction("main");

      String app = (String) caps.getCapability("app");
      if (app != null) {
        setApp(app);
      }

      String udid = (String) caps.getCapability("udid");
      if (udid != null) {
        setDeviceID(udid);
      }

      URL urlServer = new URL((String) caps.getCapability("appiumServer"));
      String platform = (String) caps.getCapability("platformName");
      setPlatformName(platform, urlServer, caps);
    } catch (Exception e) {
      throw new RuntimeException(
          format("An error has occurred: [%s] Please check the DesiredCapabilities on: [%s]",
              e.getMessage(), path));
    }
  }

  @CommandRef(desc = "Execute a Nashorn (JS) script.", params = {"path - The script full path."},
      ret = "")
  public void run(final String path) {
    final ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName(ENGINE_NAME);

    try {
      engine.eval(new FileReader(path));
      Invocable invocable = (Invocable) engine;
      invocable.invokeFunction("main", this);
    } catch (Exception e) {
      throw new RuntimeException(format("An error has occurred: [%s] Please check the script: [%s]",
          e.getMessage(), path));
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
  
  public void logElement(boolean log){
    logElement = log;
  }

  @CommandRef(desc = "Terminates the driver instance.")
  public void quit() {
    ((AppiumDriver<E>) getDriver()).quit();
  };

  @CommandRef(desc = "Find elements by ID.", params = {"id - The element id"},
      ret = "An element (AndroidElement or IOSElement).")
  public E id(String id) {
    return findElement(By.id(id));
  }

  @CommandRef(desc = "Find elements by ID.", params = {"id - The element id"},
      ret = "A list of elements (AndroidElement or IOSElement). This list is empty when no elements are found.")
  public List<E> ids(String id) {
    return findElements(By.id(id));
  }

  @CommandRef(desc = "Find element by class name.",
      params = {"className - The class property (for example, 'android.widget.Button')"},
      ret = "An element (AndroidElement or IOSElement).")
  public E className(String className) {
    return findElement(By.className(className));
  }

  @CommandRef(desc = "Find elements by class name.",
      params = {"className - The class property (for example, 'android.widget.Button')"},
      ret = "A list of elements (AndroidElement or IOSElement). This list is empty when no elements are found.")
  public List<E> classNames(String className) {
    return findElements(By.className(className));
  }

  @CommandRef(desc = "Find element by Xpath.", params = {"xpath -  A Xpath expression"},
      ret = "An element (AndroidElement or IOSElement).")
  public E xpath(String xpath) {
    return findElement(By.xpath(xpath));
  }

  @CommandRef(desc = "Find elements by Xpath.", params = {"xpath -  A Xpath expression"},
      ret = "A list of elements. This list is empty when no elements are found.")
  public List<E> xpaths(String xpath) {
    return findElements(By.xpath(xpath));
  }

  @CommandRef(desc = "Move back")
  public void back() {
    getDriver().navigate().back();
  };

  @CommandRef(desc = "Prints the current orientation of a mobile devices desktop.")
  public void orientation() {
    console(getDriver().getOrientation());
  }

  @CommandRef(desc = "Prints the capabilities of the current driver.")
  public void capabilities() {
    console(getDriver().getCapabilities().asMap());
  }

  @CommandRef(desc = "Retrieves a XML view of the current screen.",
      ret = "A XML view of the current screen.")
  public void source() throws IOException, DocumentException {
    console(prettyXML(getDriver().getPageSource()));
  }

  @CommandRef(desc = "Prints the ID of this session.")
  public void session() {
    console(getDriver().getSessionId());
  };

  @CommandRef(desc = "Prints all defined Strings from an app for the default language.")
  public void strings() {
    console(getDriver().getAppStringMap());
  };

  @CommandRef(desc = "Prints the device date and time for both iOS and Android devices.")
  public void time() {
    console(getDriver().getDeviceTime());
  };

  @CommandRef(desc = "Prints the current context.")
  public void context() {
    console(getDriver().getContext());
  };

  @CommandRef(desc = "Prints the available contexts.")
  public void contextHandles() {
    getDriver().getContextHandles().forEach(item -> console(item));
  };

  @CommandRef(desc = "Switch to a new context.", params = {"context - The context name."})
  public void context(String context) {
    getDriver().context(context);
  };

  @CommandRef(desc = "Prints the session details.")
  public void sessionDetails() {
    console(getDriver().getSessionDetails());
  };

  @CommandRef(desc = "Hides the keyboard if it is showing.")
  public void hideKeyboard() {
    getDriver().hideKeyboard();
  };

  protected E findElement(By locator) {
    E element;
    try {
      element = (E) getDriver().findElement(locator);
      printElement(element);
    } catch (Exception e) {
      throw new RuntimeException("Element not found: " + e.getMessage());
    }
    return element;
  }

  protected List<E> findElements(By locator) {
    List<E> elements = (List<E>) ((T) getDriver()).findElements(locator);
    console("Found: " + elements.size() + " elements" + SEPARATOR);
    for (E element : elements) {
      printElement(element);
    }
    return elements;
  }

  protected void printElement(E element) {
    if (logElement) {
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
  }

  private void setPlatformName(final String platform, URL urlServer, DesiredCapabilities caps) {
    switch (platform.toLowerCase()) {
      case "android":
        caps.setCapability(PLATFORM_NAME, "Android");
        setDriver(new AndroidDriver(urlServer, caps));
        break;
      case "ios":
        caps.setCapability(PLATFORM_NAME, "iOS");
        setDriver(new IOSDriver(urlServer, caps));
        break;
      default:
        throw new RuntimeException(
            format("Failed to start session. Please check the capabilities.", caps.toString()));
    }
  }
}
