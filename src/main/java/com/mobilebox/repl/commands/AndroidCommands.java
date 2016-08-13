package com.mobilebox.repl.commands;

import static com.mobilebox.repl.misc.Utils.console;
import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.NEW_COMMAND_TIMEOUT;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;
import static io.appium.java_client.remote.MobilePlatform.ANDROID;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.mobilebox.repl.app.APKInspector;
import com.mobilebox.repl.exceptions.CommandsException;
import com.mobilebox.repl.selectors.UISelector;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

@SuppressWarnings({"rawtypes"})
public class AndroidCommands extends AppiumCommands<AndroidDriver, AndroidElement> {

  @Override
  @CommandRef(desc = "Start a new Appium session for Android.", params = {
      "deviceName -  The kind of mobile device or emulator to use",
      "udid - Unique device identifier of the connected physical device",
      "app - The absolute local path or remote http URL to an .ipa or .apk",
      "server -  The Appium server URL",
      "timeout - How long (in seconds) Appium will wait for a new command from the client before assuming the client quit and ending the session."})
  public void start(String deviceName, String udid, String app, String server, String timeout)
      throws CommandsException, MalformedURLException {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(PLATFORM_NAME, ANDROID);
    capabilities.setCapability(DEVICE_NAME, deviceName);
    capabilities.setCapability(NEW_COMMAND_TIMEOUT, timeout);
    capabilities.setCapability(UDID, udid);
    capabilities.setCapability(APP, app);
    setDriver(new AndroidDriver<>(new URL(server), capabilities));
    setDeviceID(udid);
    setApp(app);
  }

  @CommandRef(desc = "Retrieves an AndroidDeviceCommands instance.")
  public AndroidDeviceCommands getDevice() throws CommandsException {
    return new AndroidDeviceCommands(getDeviceID());
  }

  @CommandRef(desc = "Find elements by text.",
      params = {
          "text -  The visible text displayed in a widget (for example, the text label to launch an app)."},
      ret = "A list of AndroidElement. This list is empty when no elements are found.")
  public List<AndroidElement> byText(final String text) {
    return findElements(UISelector.text(text));
  }

  @CommandRef(desc = "Find elements by text that contains a given text.",
      params = {
          "text -  The visible text displayed in a widget (for example, the text label to launch an app). "
              + "The text for the element must match exactly with the string in your input argument. "
              + "Matching is case-sensitive."},
      ret = "A list of AndroidElement. This list is empty when no elements are found.")
  public List<AndroidElement> byTextContains(final String text) {
    return findElements(UISelector.textContains(text));
  }

  @CommandRef(
      desc = "Find elements by an UISelector expression. Chaining the search criteria on 'new UiSelector()'.",
      params = {
          "selector - The UISelector expression. E.g: className('android.widget.RelativeLayout').enabled(true).instance(0);"},
      ret = "A list of AndroidElement. This list is empty when no elements are found.")
  public List<AndroidElement> byUISelector(final String selector) {
    return findElements(UISelector.selectorChaining(selector));
  }

  @CommandRef(desc = "Prints the current activity being run on the mobile device.")
  public void getActivity() {
    console(getDriver().currentActivity());
  }

  @CommandRef(desc = "Open the notification shade, on Android devices.")
  public void openNotifications() {
    getDriver().openNotifications();
  }

  @CommandRef(desc = "Close the app which was provided in the capabilities at session creation.")
  public void closeApp() {
    getDriver().closeApp();
  };

  @CommandRef(desc = "Launch the app which was provided in the capabilities at session creation.")
  public void launchApp() {
    getDriver().launchApp();
  };

  @CommandRef(desc = "Prints detailed information about the app.")
  public void getAppInfo() throws CommandsException {
    new APKInspector().inspect(getApp()).toConsole();
  };

}
