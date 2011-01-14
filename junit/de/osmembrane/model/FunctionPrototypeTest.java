package de.osmembrane.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.AbstractTask;
import de.osmembrane.model.xml.XMLTask;

/**
 * Simple test for automatic FunctionPrototype generation.
 * 
 * @author jakob_jarosch
 */
public class FunctionPrototypeTest {

	private FunctionPrototype prototype = new FunctionPrototype();
	
	/**
	 * test the initate() method.
	 */
	@Test
	public void testInitiate() {
		prototype.initiate("src/de/osmembrane/resources/xml/osmosis-structure.xml");
	}

	/**
	 * Test the getFunctionGroups() method.
	 * !! If {@link FunctionPrototypeTest#testInitiate()} fails, this also fails.
	 */
	@Test
	public void testGetFunctionGroups() {
		testInitiate();
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~ Test: getFunctionGroups() ~~~~~~~~~~~~~~~~~~~~~~~");
		
		for(AbstractFunctionGroup group : prototype.getFunctionGroups()) {
			System.out.println(group.getFriendlyName());
			for(AbstractFunction function : group.getFunctions()) {
				/* Check if the parent is the correct one, with the same instance */
				assertEquals("Parent is not the right one", group, function.getParent());
				
				System.out.println("\t" + function.getFriendlyName());
				for(AbstractTask task : function.getAvailableTasks()) {
					System.out.println("\t\t" + task.getName());
				}
			}
		}
	}

	/**
	 * Test the getFunctionGroup() method.
	 * !! If {@link FunctionPrototypeTest#testInitiate()} fails, this also fails.
	 */
	@Test
	public void testGetFunctionGroup() {
		testInitiate();
		
		System.out.println("\n");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~ Test: getFunctionGroup() ~~~~~~~~~~~~~~~~~~~~~~~");
		
		AbstractFunctionGroup firstFunctionGroup = prototype.getFunctionGroups()[0];
		AbstractFunctionGroup firstFunctionGroupCopy = prototype.getFunctionGroup(firstFunctionGroup);
		
		System.out.println(firstFunctionGroup);
		System.out.println(firstFunctionGroupCopy);
		System.out.println(firstFunctionGroup.same(firstFunctionGroupCopy));
		
		assertTrue("Not the same function-group", (firstFunctionGroup.same(firstFunctionGroupCopy)));
		assertFalse("The same function-group-instance", firstFunctionGroup.equals(firstFunctionGroupCopy));
	}

	/**
	 * Test the getFunction() method.
	 * !! If {@link FunctionPrototypeTest#testInitiate()} fails, this also fails.
	 */
	@Test
	public void testGetFunction() {
		testInitiate();
		
		System.out.println("\n");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~ Test: getFunction() ~~~~~~~~~~~~~~~~~~~~~~~");
		
		AbstractFunction firstFunction = prototype.getFunctionGroups()[0].getFunctions()[0];
		AbstractFunction firstFunctionCopy = prototype.getFunction(firstFunction);
		
		System.out.println(firstFunction);
		System.out.println(firstFunctionCopy);
		System.out.println(firstFunction.same(firstFunctionCopy));
		
		assertTrue("Not the same function", (firstFunction.same(firstFunctionCopy)));
		assertFalse("The same function-instance", firstFunction.equals(firstFunctionCopy));
	}

}
