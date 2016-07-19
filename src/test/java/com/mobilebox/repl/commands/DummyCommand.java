package com.mobilebox.repl.commands;

import com.mobilebox.repl.commands.CommandRef;


public class DummyCommand {

	@CommandRef(desc = "Command three", params = { "param1 - description one}" }, ret = "void")
	public void command() {
	}

}
