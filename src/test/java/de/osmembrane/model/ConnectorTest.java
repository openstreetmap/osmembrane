package de.osmembrane.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.osmembrane.Application;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.AbstractFunctionPrototype;
import de.osmembrane.model.pipeline.ConnectorException;
import de.osmembrane.model.pipeline.ConnectorException.Type;
import de.osmembrane.model.pipeline.ConnectorType;
import de.osmembrane.model.pipeline.CopyType;

/**
 * Tests the connector.
 * 
 * @author tobias_kuhn
 * 
 */
public class ConnectorTest {

    private static AbstractFunction prototype;

    private AbstractFunction[] funcs = new AbstractFunction[3];

    private AbstractConnector conOut, conIn;

    /**
     * Initiates a full testable {@link Application}, then selects the first
     * function to satisfy the conditions, that
     * 
     * <ul>
     * <li>at least 1 in-connector</li>
     * <li>at least 1 out-connector</li>
     * <li>the first in-connector is of type entity</li>
     * <li>the first out-connector is of type entity</li>
     * <li>the first in-connector allows exactly 1 connection</li>
     * </ul>
     * 
     * Tests are performed on this function prototype then
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Application a = new Application();
        a.createModels();
        a.initiate();

        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();

        for (AbstractFunctionGroup afg : afp.getFunctionGroups()) {
            for (AbstractFunction af : afg.getFunctions()) {

                if ((af.getInConnectors() != null)
                        && (af.getInConnectors().length > 0)
                        && (af.getOutConnectors() != null)
                        && (af.getOutConnectors().length > 0)) {

                    AbstractConnector firstIn = af.getInConnectors()[0];
                    AbstractConnector firstOut = af.getOutConnectors()[0];

                    if ((firstIn.getType() != ConnectorType.ENTITY)
                            || (firstOut.getType() != ConnectorType.ENTITY)
                            || (firstIn.getMaxConnections() != 1)) {
                        continue;
                    }

                    prototype = af;

                    return;

                }
            } /* for */
        } /* for */

        fail("No suitable function for testing found! Check the osmdefinitions!");
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * Creates the necessary 3 functions on the pipeline before each test
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < 3; i++) {
            funcs[i] = prototype.copy(CopyType.WITHOUT_VALUES_AND_POSITION);
            ModelProxy.getInstance().getPipeline().addFunction(funcs[i]);
        }

        conOut = funcs[0].getOutConnectors()[0];
        conIn = funcs[1].getInConnectors()[0];
    }

    /**
     * Cleans the pipeline after each test
     */
    @After
    public void tearDown() {
        ModelProxy.getInstance().getPipeline().clear();
    }

    /**
     * Test method for
     * {@link de.osmembrane.model.pipeline.Connector#getParent()}.
     */
    @Test
    public void testGetParent() {
        assertEquals("Connector parent is not the function it belongs to",
                funcs[0], conOut.getParent());
    }

    /**
     * Test method for
     * {@link de.osmembrane.model.pipeline.Connector#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        assertNull("Connector description is not null", conOut.getDescription());
        // connectors do not have descriptions.
    }

    /**
     * Test method for {@link de.osmembrane.model.pipeline.Connector#getType()}.
     */
    @Test
    public void testGetType() {
        assertEquals("Connector type is suddenly not entity anymore",
                ConnectorType.ENTITY, conOut.getType());
    }

    /**
     * Test method for
     * {@link de.osmembrane.model.pipeline.Connector#getMaxConnections()}.
     */
    @Test
    public void testGetMaxConnections() {
        assertTrue(
                "Connector max connections is suddenly not (out > 0) and (in == 1) anymore",
                (conOut.getMaxConnections() > 0)
                        && (conIn.getMaxConnections() == 1));
    }

    /**
     * Test method for {@link de.osmembrane.model.pipeline.Connector#isFull()}.
     */
    @Test
    public void testIsFull() throws ConnectorException {
        funcs[0].addConnectionTo(funcs[1]);
        assertTrue(
                "Connector with max connections 1 is not full after adding 1 connection",
                conIn.isFull());
    }

    /**
     * Test method for {@link de.osmembrane.model.pipeline.ConnectorException}.
     */
    @Test
    public void testConnectionCycle() throws ConnectorException {
        funcs[0].addConnectionTo(funcs[1]);

        assertTrue(
                "More than one connection existing",
                (conIn.getConnections().length == 1)
                        && (conOut.getConnections().length == 1));

        funcs[1].addConnectionTo(funcs[2]);
        try {
            funcs[2].addConnectionTo(funcs[0]);
            fail("No exception thrown");
        } catch (ConnectorException ce) {
            assertTrue("Exception was not cycle",
                    ce.getType() == Type.LOOP_CREATED);
        }
    }

    /**
     * Test method for {@link de.osmembrane.model.pipeline.ConnectorException}.
     */
    @Test
    public void testTwoConnections() throws ConnectorException {
        funcs[0].addConnectionTo(funcs[1]);
        try {
            funcs[2].addConnectionTo(funcs[1]);
            fail("No exception thrown");
        } catch (ConnectorException ce) {
            assertTrue("Exception was not full", ce.getType() == Type.FULL);
        }

        assertTrue(
                "More than one connection between f0 and f1",
                (conIn.getConnections().length == 1)
                        && (conOut.getConnections().length == 1));
        assertTrue("More than zero connections outgoing from f2",
                (funcs[2].getOutConnectors()[0].getConnections().length == 0));
    }

    /**
     * Test method for {@link de.osmembrane.model.pipeline.ConnectorException}.
     */
    @Test
    public void testSameConnectionTwice() throws ConnectorException {
        funcs[0].addConnectionTo(funcs[1]);
        try {
            funcs[0].addConnectionTo(funcs[1]);
            fail("No exception thrown");
        } catch (ConnectorException ce) {
            assertTrue(
                    "Exception was not "
                            + Type.CONNECTION_ALREADY_EXISTS.toString()
                            + " but " + ce.getType().toString(),
                    ce.getType() == Type.CONNECTION_ALREADY_EXISTS);
        }

        assertTrue(
                "More than one connection between f0 and f1",
                (conIn.getConnections().length == 1)
                        && (conOut.getConnections().length == 1));
    }

    /**
     * Test method for
     * {@link de.osmembrane.model.pipeline.Connector#removeConnection()}.
     */
    @Test
    public void testRemoveConnection() throws ConnectorException {
        funcs[1].addConnectionTo(funcs[2]);
        funcs[1].removeConnectionTo(funcs[2]);
        assertTrue(
                "Removed connection still exists",
                (conIn.getConnections().length == 0)
                        && (conOut.getConnections().length == 0));
    }

    /**
     * Test method for
     * {@link de.osmembrane.model.pipeline.Connector#getConnections()}.
     */
    @Test
    public void testGetConnections() throws ConnectorException {
        funcs[0].addConnectionTo(funcs[1]);

        assertTrue(
                "Added connection is not the connection present in the model",
                (conOut.getConnections().length == 1)
                        && (conOut.getConnections()[0].equals(conIn)));

    }

    /**
     * Test method for {@link de.osmembrane.model.pipeline.Connector#unlink()}.
     */
    @Test
    public void testUnlink() throws ConnectorException {
        funcs[0].addConnectionTo(funcs[1]);
        funcs[1].addConnectionTo(funcs[2]);

        ModelProxy.getInstance().getPipeline().deleteFunction(funcs[1]);

        assertTrue("f0 still has outgoing connections",
                funcs[0].getOutConnectors()[0].getConnections().length == 0);
        assertTrue("f2 still has ingoing connections",
                funcs[2].getInConnectors()[0].getConnections().length == 0);

    }

}
