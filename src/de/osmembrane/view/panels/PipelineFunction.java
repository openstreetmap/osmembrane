package de.osmembrane.view.panels;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

/**
 * The pipeline function, i.e. the visual representation of a model function
 * that is actually drawn in the pipeline. Note, the functions in the
 * {@link LibraryPanel} and the one being dragged on the {@link PipelinePanel}
 * are just {@link ViewFunction}.
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineFunction extends ViewFunction {

	private static final long serialVersionUID = -7573627124702293974L;

	/**
	 * The function in the model that is represented by this pipeline function
	 */
	private AbstractFunction modelFunction;

	/**
	 * Creates a new pipeline function from an AbstractFunction out of the model
	 * 
	 * @param modelFunction
	 *            the function out of the model
	 */
	public PipelineFunction(AbstractFunction modelFunction) {
		// pretend this is a prototype
		super(modelFunction, false);
		this.modelFunction = modelFunction;
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				IView mainFrame = ViewRegistry.getInstance().getMainFrame();
				MainFrame mf = (MainFrame) mainFrame;
				//(mf.getTool()
				mf.getPipeline().selected(PipelineFunction.this);				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;
		highlighted = this.equals(mf.getPipeline().getSelected());
		
		super.paintComponent(g);
	}
	
	/**
	 * @return the model function
	 */
	public AbstractFunction getModelFunction() {
		return this.modelFunction;
	}

	/**
	 * @return the location this function's model function has saved
	 */
	public Point2D getModelLocation() {
		return this.modelFunction.getCoordinate();
	}

}
