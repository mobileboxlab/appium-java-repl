package com.mobilebox.repl.exceptions;

public class CommandsException extends Exception {

  private static final long serialVersionUID = -1822933755277157084L;

  public CommandsException(final String message) {
    super(message.replace("\n", ""));
  }

}
