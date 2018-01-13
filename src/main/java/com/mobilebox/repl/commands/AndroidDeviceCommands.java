package com.mobilebox.repl.commands;

import static com.mobilebox.repl.misc.Utils.consoleTitle;
import static com.mobilebox.repl.misc.Utils.console;
import static com.mobilebox.repl.devices.AndroidDeviceProperties.*;
import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.mobilebox.repl.devices.AndroidDeviceKeyEvent;
import com.mobilebox.repl.exceptions.CommandsException;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.Stream;

/**
 * Commands for control an Android device or emulator from outside of Appium code. With these
 * commands you can takes screenshots, set brightness, retrieves devices properties, etc. For
 * provide these functions we uses an ADB client implemented in pure Java: JADB
 *
 * @see <a href="https://github.com/vidstige/jadb">JADB</a>
 */
public class AndroidDeviceCommands {

  private String serial;
  private JadbConnection jadb;
  private JadbDevice device;

  /**
   * Create a new instance of {@link AndroidDeviceCommands}.
   * 
   * @param serial The Android Device serial.
   * @throws CommandsException
   */
  public AndroidDeviceCommands(final String serial) throws CommandsException {
    checkArgument(!serial.isEmpty(), "Cannot load the device the device serial is empty.");

    try {
      jadb = new JadbConnection();
      List<JadbDevice> devices = jadb.getDevices().stream()
          .filter(d -> d.getSerial().contains(serial)).collect(Collectors.toList());
      this.serial = serial;
      this.device = devices.get(0);
    } catch (IOException | JadbException e) {
      throw new CommandsException("Cannot load the device. Error:" + e.getMessage());
    }
  }

  @CommandRef(desc = "Prints the Android device serial.")
  public void serial() {
    console("Android Device ID: " + serial);
  }

  @CommandRef(desc = "Set brightness.", params = {"brightness - Value into the range [0,255]."})
  public void brightness(final int brightness) throws IOException, JadbException {
    device.executeShell("settings", "put", "system", "screen_brightness",
        Integer.toString(brightness));
  }

  @CommandRef(desc = "Sends the KEYCODE_CALL KeyEvent.")
  public void call() throws IOException, JadbException {
    keyEvent(AndroidDeviceKeyEvent.KEYCODE_CALL);
  }

  @CommandRef(desc = "Sends the KEYCODE_VOLUME_UP KeyEvent.")
  public void volumenUp() throws IOException, JadbException {
    keyEvent(AndroidDeviceKeyEvent.KEYCODE_VOLUME_UP);
  }

  @CommandRef(desc = "Sends the KEYCODE_VOLUME_DOWN KeyEvent.")
  public void volumenDown() throws IOException, JadbException {
    keyEvent(AndroidDeviceKeyEvent.KEYCODE_VOLUME_DOWN);
  }

  @CommandRef(desc = "Prints the device Android version.")
  public void version() throws IOException, JadbException {
    console("Android Version Release: " + prop("ro.build.version.release"));
  }

  @CommandRef(desc = "Prints device information related to Dalvik VM.")
  public void vm() throws IOException, JadbException {
    consoleTitle("Device Dalvik VM properties");
    for (Entry<String, String> entry : DALVIK_VM.entrySet()) {
      console(entry.getKey() + " = " + prop(entry.getValue()));
    }
  }

  @CommandRef(desc = "Prints device information related to DHCP.")
  public void dhcp() throws IOException, JadbException {
    consoleTitle("Device DHCP properties");
    for (Entry<String, String> entry : DHCP.entrySet()) {
      console(entry.getKey() + " = " + prop(entry.getValue()));
    }
  }

  @CommandRef(desc = "Prints device information related to GSM.")
  public void gsm() throws IOException, JadbException {
    consoleTitle("Device GSM properties");
    for (Entry<String, String> entry : GSM.entrySet()) {
      console(entry.getKey() + " = " + prop(entry.getValue()));
    }
  }

  @CommandRef(desc = "Prints device information related to Net.")
  public void net() throws IOException, JadbException {
    consoleTitle("Device Net properties");
    for (Entry<String, String> entry : NET.entrySet()) {
      console(entry.getKey() + " = " + prop(entry.getValue()));
    }
  }

  @CommandRef(desc = "Prints device information related to product.")
  public void productInfo() throws IOException, JadbException {
    consoleTitle("Device Product properties");
    for (Entry<String, String> entry : PRODUCT.entrySet()) {
      console(entry.getKey() + " = " + prop(entry.getValue()));
    }
  }

  @CommandRef(desc = "Prints device information related to Wifi.")
  public void wifi() throws IOException, JadbException {
    consoleTitle("Device WIFI properties");
    for (Entry<String, String> entry : WIFI.entrySet()) {
      console(entry.getKey() + " = " + prop(entry.getValue()));
    }
  }

  @CommandRef(desc = " Remove a file.", params = {"filePath - The file path to remove."})
  public void remove(final String filePath) throws IOException, JadbException {
    device.executeShell("rm", "-f", filePath);
  }

  @CommandRef(desc = "Prints all packages on the device.")
  public void packages() throws IOException, JadbException {
    console(getResponse(device.executeShell("pm", "list", "packages")));
  }

  @CommandRef(desc = "Prints battery status.")
  public void battery() throws IOException, JadbException {
    console(getResponse(device.executeShell("dumpsys", "battery")));
  }

  @CommandRef(desc = "Prints disk space usage.")
  public void disk() throws IOException, JadbException {
    console(getResponse(device.executeShell("df")));
  }

  @CommandRef(desc = "Prints a list of all the available shell programs.")
  public void shellPrograms() throws IOException, JadbException {
    console(getResponse(device.executeShell("ls", "/system/bin")));
  }

  @CommandRef(desc = "Take and save a device screenshot to file.",
      params = {"filePath - Full path to file"})
  public void screenshot(final String filePath) throws IOException, JadbException {
    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(new File(filePath));
      InputStream stdout = device.executeShell("screencap", "-p");
      Stream.copy(stdout, outputStream);
    } finally {
      if (outputStream != null)
        outputStream.close();
    }
  }

  @CommandRef(
      desc = "Execute a shell command in the emulator/device instance and then exits the remote shell.",
      params = {"command - A shell command.", "args - Shell commands arguments."})
  public void shell(final String command, final String... args) throws IOException, JadbException {
    console(getResponse(device.executeShell(command, args)));
  }

  @CommandRef(
      desc = "Given the name of a system environment variable,returns its value for this device.",
      params = {"propertye -   The property name"}, ret = "The property value")
  public String prop(final String property) throws IOException, JadbException {
    return getResponse(device.executeShell("getprop", property));
  }

  private void keyEvent(final int event) throws IOException, JadbException {
    console("Sends key event to the device. KeyEvent: [" + event + "]");
    device.executeShell("input", "keyevent", Integer.toString(event));
  }

  private String getResponse(InputStream input) {
    try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name())) {
      return scanner.useDelimiter("\\A").next();
    }
  }

}
