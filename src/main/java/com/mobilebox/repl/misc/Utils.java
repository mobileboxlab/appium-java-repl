package com.mobilebox.repl.misc;

import static com.mobilebox.repl.commands.CommandsDoc.SEPARATOR;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Utils {

	/**
	 * Pretty-Print XML.
	 * 
	 * @param xml
	 *            The XML
	 * @return A beautiful XML
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String prettyXML(final String xml) throws IOException, DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		StringWriter sw = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent(true);
		format.setIndentSize(3);
		XMLWriter xw = new XMLWriter(sw, format);
		xw.write(doc);
		return sw.toString();
	}

	/**
	 * Prints a String on console and then terminate the line.
	 * 
	 * @param msg
	 *            The String to be printed.
	 */
	public static void console(final String msg) {
		System.out.println(msg);
	}

	public static void consoleTitle(final String msg) {
		System.out.println("------------------------------------");
		System.out.println("::-  " + msg + "  -::");
		System.out.println("------------------------------------" + SEPARATOR);
	}
}
