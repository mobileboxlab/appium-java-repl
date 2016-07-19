package com.mobilebox.repl.devices;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Android has a set of properties about the device that can be read using the
 * getprop command line utility. This class is a place to collect theses
 * properties.
 */
public class AndroidDeviceProperties {

	public static final Map<String, String> DALVIK_VM = ImmutableMap.<String, String>builder()
	        .put("Heap Growth Limit", "dalvik.vm.heapgrowthlimit")
	       	.put("Heap Max Free", "dalvik.vm.heapmaxfree")
	       	.put("Heap Min Free", "dalvik.vm.heapminfree")
	       	.put("Heap Size", "dalvik.vm.heapsize")
	       	.put("Heap Start Size", "dalvik.vm.heapstartsize")
	       	.put("Stack Trace File", "dalvik.vm.stack-trace-file")
	       	.build();

	public static final Map<String, String> DHCP = ImmutableMap.<String, String>builder()
	        .put("DNS 1", "dhcp.wlan0.dns1")
	       	.put("DNS 2", "dhcp.wlan0.dns2")
	       	.put("DNS 3", "dhcp.wlan0.dns3")
	       	.put("DNS 4", "dhcp.wlan0.dns4")
	       	.put("Domain", "dhcp.wlan0.domain")
	       	.put("IP Address", "dhcp.wlan0.ipaddress")
	       	.put("Mask", "dhcp.wlan0.mask")
	       	.put("Roaming", "dhcp.wlan0.roaming")
	       	.put("Server", "dhcp.wlan0.server")
	       	.put("Gateway", "dhcp.wlan0.gateway")
	       	.put("Vendor Info", "dhcp.wlan0.vendorInfo")
	       	.build();

	public static final Map<String, String> GSM = ImmutableMap.<String, String>builder()
	        .put("STK Setup Menu", "gsm.STK_SETUP_MENU")
	       	.put("Phone Type", "[gsm.current.phone-type")
	       	.put("Default PDP Context", "gsm.defaultpdpcontext.active")
	       	.put("Network Type", "gsm.network.type")
	       	.put("Operator Alpha", "gsm.operator.alpha")
	       	.put("ISO-Country", "gsm.operator.iso-country")
	       	.put("ISPS Roaming", "gsm.operator.ispsroaming")
	       	.put("Is Roaming?", "gsm.operator.isroaming")
	       	.put("SIM State", "gsm.sim.state")
	       	.put("Version Baseband", "gsm.version.baseband")
	       	.build();					
										
	public static final Map<String, String> NET = ImmutableMap.<String, String>builder()
	        .put("BT Name", "net.bt.name")
	       	.put("Change", "net.change")
	       	.put("DNS 1", "net.dns1")
	       	.put("DNS 2", "net.dns2")
	       	.build();							

	public static final Map<String, String> PRODUCT = ImmutableMap.<String, String>builder()
	        .put("Board", "ro.product.board")
	       	.put("Brand", "ro.product.brand")
	       	.put("CPU ABI2", "ro.product.cpu.abi2")
	       	.put("CPU ABI", "ro.product.cpu.abi")
	    	.put("Device", "ro.product.device")
	    	.put("Locale Language", "ro.product.locale.language")
	    	.put("Locale Region", "ro.product.locale.region")
	    	.put("Manufacturer", "ro.product.manufacturer")
	    	.put("Model", "ro.product.model")
	    	.put("Name", "ro.product.name")
	    	.put("Ship", "ro.product_ship")
	       	.build();				
	
	public static final Map<String, String> WIFI = ImmutableMap.<String, String>builder()
	        .put("Interface", "wifi.interface")
	       	.put("Driver Status", "wlan.driver.status")
	       	.put("WFD Status", "wlan.wfd.status")
	       	.build();		
}
