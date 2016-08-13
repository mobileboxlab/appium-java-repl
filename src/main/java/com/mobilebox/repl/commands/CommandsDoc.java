package com.mobilebox.repl.commands;

import static com.mobilebox.repl.misc.Utils.console;
import static com.mobilebox.repl.misc.Utils.consoleTitle;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public abstract class CommandsDoc {

  public static final String SEPARATOR = System.getProperty("line.separator");

  /**
   * Prints all commands in a given class. In order to define command in Java code, you need to
   * annotate the respective methods with <code>@CommandRef</code> annotation.
   * 
   * @param clazz A class that contains commands.
   */
  public static void printCommands(Class<?> clazz) {
    consoleTitle("Available Commands for " + clazz.getSimpleName().replaceAll("Commands", ""));

    StringBuffer ref = new StringBuffer();
    ref.append("---> Command: %s").append(SEPARATOR).append("---> Description: %s")
        .append(SEPARATOR).append("---> Parameters: %s").append(SEPARATOR).append("---> Return: %s")
        .append(SEPARATOR);

    for (Method method : clazz.getMethods()) {
      String descOfMethod = "";
      String[] params = {""};
      String returns = "";
      if (Modifier.isPublic(method.getModifiers())) {
        CommandRef def = method.getAnnotation(CommandRef.class);
        if (def != null) {
          params = def.params();
          descOfMethod = def.desc();
          returns = def.ret();
          console(String.format(ref.toString(), method.getName(), descOfMethod,
              Arrays.toString(params), returns));
        }

      }
    }
  }
}
