package de.osmembrane.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.AbstractTask;
import de.osmembrane.model.pipeline.ConnectorException;
import de.osmembrane.model.pipeline.CopyType;
import de.osmembrane.model.pipeline.Pipeline;
import de.osmembrane.model.pipeline.PipelineObserverObject;

/**
 * Intelligent, well behaving and upright Function descendant.
 * 
 * ******* FOR TESTING PURPOSES ONLY. WARRANTY VOID. *******
 * 
 * @author tobias_kuhn
 * 
 */
public class TestFunction extends AbstractFunction {

    private static final long serialVersionUID = -5397137995807781016L;

    /**
     * RNG for this class
     */
    private static final Random rng = new Random();

    /**
     * unique ID for this test function
     */
    private final long uniqueID;

    /**
     * the actual function contained
     */
    private AbstractFunction contained;

    /**
     * 
     * @param contained
     */
    public TestFunction(AbstractFunction contained) {
        this.uniqueID = rng.nextLong();
        this.contained = contained;
        assertNotNull(contained);
    }

    /**
     * Looks out for itself on the pipeline, if it finds itself, it
     * automatically assigns its values to those from the pipeline.
     * 
     * @param pl
     * @return true if found and assigned values, false otherwise
     */
    public boolean findOnPipeline(AbstractPipeline pl) {
        for (AbstractFunction af : pl.getFunctions()) {
            if (af instanceof TestFunction) {
                TestFunction tf = (TestFunction) af;

                if (tf.uniqueID == this.uniqueID) {
                    this.contained = tf.contained;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Asserts this function has task taskname set
     * 
     * @param taskName
     */
    public void assertTaskName(String taskName) {
        assertEquals(taskName, this.getActiveTask().getName());
    }

    /**
     * Asserts the param param is value.
     * 
     * @param param
     * @param value
     */
    public void assertParameter(int param, String value) {
        assertEquals(value, getActiveTask().getParameters()[param].getValue());
    }

    /**
     * Asserts this function is at loc
     * 
     * @param loc
     */
    public void assertLocation(Point2D loc) {
        assertEquals(loc, this.getUnrasteredCoordinate());
    }

    /**
     * Asserts there are exactly desiredCount connections to inTarget.
     * 
     * @param inTarget
     * @param desiredCount
     */
    public void assertConnectionCountTo(TestFunction inTarget, int desiredCount) {

        int outCount = 0;
        int inCount = 0;

        for (AbstractConnector ac : this.getOutConnectors()) {
            for (AbstractConnector to : ac.getConnections()) {
                if (to.getParent().equals(inTarget.contained)) {
                    outCount++;
                }
            }
        }

        for (AbstractConnector ac : inTarget.getInConnectors()) {
            for (AbstractConnector from : ac.getConnections()) {
                if (from.getParent().equals(this.contained)) {
                    inCount++;
                }
            }
        }

        assertEquals(desiredCount, outCount);
        assertEquals(desiredCount, inCount);
    }

    @Override
    public void update(Observable o, Object arg) {
        contained.update(o, arg);
    }

    @Override
    public AbstractFunctionGroup getParent() {
        return contained.getParent();
    }

    @Override
    protected void setPipeline(Pipeline pipeline) {
        try {
            // contained.setPipeline(pipeline);
            Method m = contained.getClass().getDeclaredMethod("setPipeline",
                    Pipeline.class);
            m.setAccessible(true);
            m.invoke(contained, pipeline);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractPipeline getPipeline() {
        return contained.getPipeline();
    }

    @Override
    public String getId() {
        return contained.getId();
    }

    @Override
    public String getFriendlyName() {
        return contained.getFriendlyName();
    }

    @Override
    public String getDescription() {
        return contained.getDescription();
    }

    @Override
    public BufferedImage getIcon() {
        return contained.getIcon();
    }

    @Override
    public AbstractTask[] getAvailableTasks() {
        return contained.getAvailableTasks();
    }

    @Override
    public AbstractTask getActiveTask() {
        return contained.getActiveTask();
    }

    @Override
    public void setActiveTask(AbstractTask task) {
        contained.setActiveTask(task);
    }

    @Override
    public Point2D getCoordinate() {
        return contained.getCoordinate();
    }

    @Override
    public void setCoordinate(Point2D coordinate) {
        contained.setCoordinate(coordinate);
    }

    @Override
    public AbstractConnector[] getInConnectors() {
        return contained.getInConnectors();
    }

    @Override
    public AbstractConnector[] getOutConnectors() {
        return contained.getOutConnectors();
    }

    @Override
    public void addConnectionTo(AbstractFunction function)
            throws ConnectorException {
        contained.addConnectionTo(function);
    }

    @Override
    public boolean removeConnectionTo(AbstractFunction function) {
        return contained.removeConnectionTo(function);
    }

    @Override
    protected void unlinkConnectors() {
        try {
            // contained.unlinkConnectors();
            Method m = contained.getClass().getDeclaredMethod(
                    "unlinkConnectors");
            m.setAccessible(true);
            m.invoke(contained);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void changedNotifyObservers(PipelineObserverObject poo) {
        try {
            // contained.changedNotifyObservers(poo);
            Method m = contained.getClass().getDeclaredMethod(
                    "changedNotifyObservers");
            m.setAccessible(true);
            m.invoke(contained, poo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractFunction copy(CopyType type) {
        return contained.copy(type);
    }

    @Override
    public synchronized void addObserver(Observer o) {
        contained.addObserver(o);
    }

    @Override
    public Point2D getUnrasteredCoordinate() {
        return contained.getUnrasteredCoordinate();
    }

    @Override
    public boolean isComplete() {
        return contained.isComplete();
    }

}
