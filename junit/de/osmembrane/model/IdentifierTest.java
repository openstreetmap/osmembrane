package de.osmembrane.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class IdentifierTest {

	@Test
	public void identifierTest() {
		Identifier test = new Identifier("string");
		Identifier test2 = new Identifier("string");
		Identifier test3 = new Identifier("string2");
		
		System.out.println(test.equals(test2));
		assertNotSame(test, test3);
	}
}
