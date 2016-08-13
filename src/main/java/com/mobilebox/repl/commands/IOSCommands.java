package com.mobilebox.repl.commands;

import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.NEW_COMMAND_TIMEOUT;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;
import static io.appium.java_client.remote.MobilePlatform.IOS;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.mobilebox.repl.exceptions.CommandsException;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

@SuppressWarnings({"rawtypes"})
public class IOSCommands extends AppiumCommands<IOSDriver, IOSElement> {

  @Override
  @CommandRef(desc = "Start a new Appium session for iOS.", params = {
      "deviceName -  The kind of mobile device or emulator to use",
      "udid - Unique device identifier of the connected physical device",
      "app - The absolute local path or remote http URL to an .ipa or .apk",
      "server -  The Appium server URL",
      "timeout - How long (in seconds) Appium will wait for a new command from the client before assuming the client quit and ending the session."})
  void start(String deviceName, String udid, String app, String server, String timeout)
      throws CommandsException, MalformedURLException {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(PLATFORM_NAME, IOS);
    capabilities.setCapability(DEVICE_NAME, deviceName);
    capabilities.setCapability(NEW_COMMAND_TIMEOUT, timeout);
    capabilities.setCapability(UDID, udid);
    capabilities.setCapability(APP, app);
    setDriver(new IOSDriver<>(new URL(server), capabilities));
    setApp(app);
  }

}
