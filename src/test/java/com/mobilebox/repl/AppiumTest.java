package com.mobilebox.repl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobilebox.repl.Appium;

import static org.assertj.core.api.Assertions.*;

public class AppiumTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @BeforeClass
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterClass
  public void cleanUpStreams() {
    System.setOut(null);
  }

  @Test
  public void commandsAppiumTest() {
    Appium.commands_appium();
    assertThat(outContent.toString()).contains("Available Commands for Appium");
  }

  @Test
  public void commandsAndroidTest() {
    Appium.commands_android();
    assertThat(outContent.toString()).contains("Available Commands for Android");
  }

  @Test
  public void commandsAndroidDeviceTest() {
    Appium.commands_android_device();
    assertThat(outContent.toString()).contains("Available Commands for AndroidDevice");
  }

  @Test
  public void commandsIOSTest() {
    Appium.commands_ios();
    assertThat(outContent.toString()).contains("Available Commands for IOS");
  }
}
