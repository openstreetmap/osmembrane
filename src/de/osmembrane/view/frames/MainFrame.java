package de.osmembrane.view.frames;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.ChangeSettingsAction;
import de.osmembrane.controller.DeleteFunctionAction;
import de.osmembrane.controller.DuplicateFunctionAction;
import de.osmembrane.controller.ExecutePipelineAction;
import de.osmembrane.controller.ExitAction;
import de.osmembrane.controller.ExportPipelineAction;
import de.osmembrane.controller.GeneratePipelineAction;
import de.osmembrane.controller.ImportPipelineAction;
import de.osmembrane.controller.LoadPipelineAction;
import de.osmembrane.controller.NewPipelineAction;
import de.osmembrane.controller.PreviewPipelineAction;
import de.osmembrane.controller.RedoAction;
import de.osmembrane.controller.SavePipelineAction;
import de.osmembrane.controller.ShowHelpAction;
import de.osmembrane.controller.UndoAction;
import de.osmembrane.model.AbstractFunctionGroup;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractFrame;
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
		setWindowTitle(I18N.getInstance().getString("osmembrane"));
		
		// WindowListener() von leeren methoden kürzen, wenn fertig
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {			
			}

			@Override
			public void windowClosing(WindowEvent e) {
				ActionRegistry.getInstance().get(ExitAction.class).actionPerformed(null);
				// TODO * check ob das ok is *
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {			
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
		});
		
		// menu bar
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(ActionRegistry.getInstance().get(NewPipelineAction.class));
		fileMenu.add(ActionRegistry.getInstance().get(LoadPipelineAction.class));
		fileMenu.add(ActionRegistry.getInstance().get(SavePipelineAction.class));
		fileMenu.add(new JSeparator());
		fileMenu.add(ActionRegistry.getInstance().get(ImportPipelineAction.class));
		fileMenu.add(ActionRegistry.getInstance().get(ExportPipelineAction.class));
		fileMenu.add(new JSeparator());
		fileMenu.add(ActionRegistry.getInstance().get(ChangeSettingsAction.class));
		fileMenu.add(new JSeparator());
		fileMenu.add(ActionRegistry.getInstance().get(ExitAction.class));
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.add(ActionRegistry.getInstance().get(UndoAction.class));
		editMenu.add(ActionRegistry.getInstance().get(RedoAction.class));
		editMenu.add(new JSeparator());
		editMenu.add(ActionRegistry.getInstance().get(DuplicateFunctionAction.class));
		editMenu.add(ActionRegistry.getInstance().get(DeleteFunctionAction.class));
		menuBar.add(editMenu);
		
		JMenu pipelineMenu = new JMenu("Pipeline");
		pipelineMenu.add(ActionRegistry.getInstance().get(PreviewPipelineAction.class));
		pipelineMenu.add(ActionRegistry.getInstance().get(GeneratePipelineAction.class));
		pipelineMenu.add(ActionRegistry.getInstance().get(ExecutePipelineAction.class));
		menuBar.add(pipelineMenu);
		
		JMenu aboutMenu = new JMenu("About");
		aboutMenu.add(ActionRegistry.getInstance().get(ShowHelpAction.class));
		menuBar.add(aboutMenu);
		
		setJMenuBar(menuBar);
			
		// tool bar
		JToolBar toolBar = new JToolBar("OSMembrane", JToolBar.HORIZONTAL);
		toolBar.add(ActionRegistry.getInstance().get(NewPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(LoadPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(SavePipelineAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance().get(ImportPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(ExportPipelineAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance().get(UndoAction.class));
		toolBar.add(ActionRegistry.getInstance().get(RedoAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance().get(PreviewPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(GeneratePipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(ExecutePipelineAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance().get(ShowHelpAction.class));
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