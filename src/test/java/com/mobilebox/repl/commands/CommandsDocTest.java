package com.mobilebox.repl.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobilebox.repl.commands.CommandsDoc;

public class CommandsDocTest {

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
  public void printCommands() {
    String expected =
        "------------------------------------\n::- Available Commands for DummyCommand -::\n------------------------------------\n---> Command: command\n---> Description: Command three\n---> Parameters: [param1 - description one}]\n---> Return: void\n";
    CommandsDoc.printCommands(DummyCommand.class);
    assertThat(outContent.toString()).isEqualToIgnoringWhitespace(expected);
  }
}
