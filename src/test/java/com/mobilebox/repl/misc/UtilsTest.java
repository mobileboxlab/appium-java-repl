package com.mobilebox.repl.misc;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.mobilebox.repl.misc.Utils;

import static org.assertj.core.api.Assertions.*;

public class UtilsTest {

	@Test
	public void prettyXMLTest() throws IOException, DocumentException {
		String xml = "<?xml version=\"1.0\"?><class><student rollno=\"1889\"><firstname>Ireneo</firstname><lastname>Funes</lastname></student></class>";
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<class>\n   <student rollno=\"1889\">\n      <firstname>Ireneo</firstname>\n      <lastname>Funes</lastname>\n   </student>\n</class>\n";
		assertThat(Utils.prettyXML(xml)).isEqualToIgnoringCase(expected);
	}

	@Test
	public void prettyXMLTestShouldBeTrowDocumentException() {
		String xml = "FrayBentos</firstname><lastname>Funes</lastname></student></class>";
		assertThatThrownBy(() -> {
			Utils.prettyXML(xml);
		}).isInstanceOf(DocumentException.class)
				.hasMessageContaining(
						"Error on line 1 of document");
	}

}
