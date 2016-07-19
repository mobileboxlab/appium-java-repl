package com.mobilebox.repl.config;

import org.aeonbits.owner.Config;

import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Config.HotReload;

/**
 * Specify the necessary configurations of AUT through capabilities by storing
 * them in a file.
 */
@HotReload
@Sources("file:${user.home}/appium.txt")
public interface ConfigCapabilities extends Config {

	@DefaultValue("n/a")
	@Key("platform.name")
	String platformName();

	@Key("device.name")
	String deviceName();

	@Key("command.timeout")
	String cmdTimeout();

	@DefaultValue("")
	@Key("udid")
	String udid();

	@Key("app")
	String app();
	
	@DefaultValue("http://127.0.0.1:4723/wd/hub")
	@Key("appium.server")
	String appiumServer();
}