package com.mobilebox.repl;

import static com.mobilebox.repl.commands.CommandsDoc.printCommands;
import static com.mobilebox.repl.commands.CommandsDoc.SEPARATOR;
import static com.mobilebox.repl.misc.Utils.console;
import javarepl.Main;

import com.mobilebox.repl.commands.AndroidCommands;
import com.mobilebox.repl.commands.AndroidDeviceCommands;
import com.mobilebox.repl.commands.AppiumCommands;
import com.mobilebox.repl.commands.CommandRef;
import com.mobilebox.repl.commands.IOSCommands;

/**
 * This class acts as entry point to JavaREPL also provides commands that basically are wrappers
 * over some Appium methods such as find an element, get source, etc.
 * 
 * @see <a href="https://github.com/albertlatacz/java-repl">JavaREPL</a>
 *
 */
public class Appium {

  public static AndroidCommands android;
  public static IOSCommands ios;

  static {
    android = new AndroidCommands();
    ios = new IOSCommands();
  }

  public static void main(String... args) throws Exception {
    welcome();
    Main.main(args);
  }

  private static void welcome() {
    console("----------------------------");
    console(" :::- Appium Java REPL -::: " + SEPARATOR);
    console("Type import static com.mobilebox.repl.Appium.*;" + SEPARATOR);
    console("Type help() for more options.");
    console("-----------------------------" + SEPARATOR);
  }

  @CommandRef(desc = "Prints this help.")
  public static void help() {
    printCommands(Appium.class);
  }

  @CommandRef(desc = "Quit Appium REPL")
  public static void exit() {
    System.exit(0);
  }

  @CommandRef(desc = "Prints all commands available for Android and iOS.")
  public static void commands_appium() {
    printCommands(AppiumCommands.class);
  }

  @CommandRef(desc = "Prints all commands available especifc for Android.")
  public static void commands_android() {
    printCommands(AndroidCommands.class);
  }

  @CommandRef(desc = "Prints all commands available for iOS.")
  public static void commands_ios() {
    printCommands(IOSCommands.class);
  }

  @CommandRef(desc = "Prints all commands available for Android Device.")
  public static void commands_android_device() {
    printCommands(AndroidDeviceCommands.class);
  }

}
