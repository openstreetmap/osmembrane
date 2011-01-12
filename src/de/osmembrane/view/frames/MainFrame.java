package de.osmembrane.view.frames;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.ExitAction;
import de.osmembrane.model.AbstractFunctionGroup;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.view.AbstractFrame;
import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.panels.LibraryPanel;
import de.osmembrane.view.panels.InspectorPanel;
import de.osmembrane.view.panels.LibraryPanelGroup;
import de.osmembrane.view.panels.PipelinePanel;

/**
 * The Main window that is the center of OSMembrane and the first thing
 * you'll see after the splash screen.
 * 
 * @author tobias_kuhn
 *
 */
public class MainFrame extends AbstractFrame {
	
	private String notification;

	/**
	 * Creates the main frame.
	 * @see Spezifikation.pdf, chapter 2.1
	 */
	public MainFrame() {
		// WindowListener() von leeren methoden kürzen, wenn fertig
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				ActionRegistry.getInstance().get(ExitAction.class).actionPerformed(null);
				// TODO * check ob das ok is *
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		// menu bar
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(ActionRegistry.getInstance().get(ExitAction.class));
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		JMenu pipelineMenu = new JMenu("Pipeline");
		menuBar.add(pipelineMenu);
		
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(aboutMenu);
		
		setJMenuBar(menuBar);
			
		// tool bar
		JToolBar toolBar = new JToolBar("OSMembrane", JToolBar.HORIZONTAL);
		toolBar.add(ActionRegistry.getInstance().get(ExitAction.class));
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		// function library	
		LibraryPanel functionLibrary = new LibraryPanel();
		for (AbstractFunctionGroup afg : ModelProxy.getInstance().accessFunctions().getFunctionGroups()) {
			LibraryPanelGroup lpg = new LibraryPanelGroup(functionLibrary, afg);
			functionLibrary.addGroup(lpg);
		}
		JScrollPane paneLibrary = new JScrollPane(functionLibrary);			
		
		// pipeline view
		PipelinePanel pipelineView = new PipelinePanel();
		JScrollPane panePipeline = new JScrollPane(pipelineView);
		
		// function inspector
		InspectorPanel functionInspector = new InspectorPanel();
		JScrollPane paneInspector = new JScrollPane(functionInspector);		
		
		// split containers
		JSplitPane splitLibAndView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, paneLibrary, panePipeline);
		
		JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, splitLibAndView, paneInspector);
		getContentPane().add(splitMain);
				
		// center, then maximize
		pack();
		centerWindow();
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(String notification) {
		this.notification = notification;
	}
	
	
	
}