package de.osmembrane.view.frames;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.ChangeSettingsAction;
import de.osmembrane.controller.actions.DeleteFunctionAction;
import de.osmembrane.controller.actions.DuplicateFunctionAction;
import de.osmembrane.controller.actions.ExecutePipelineAction;
import de.osmembrane.controller.actions.ExitAction;
import de.osmembrane.controller.actions.ExportPipelineAction;
import de.osmembrane.controller.actions.GeneratePipelineAction;
import de.osmembrane.controller.actions.ImportPipelineAction;
import de.osmembrane.controller.actions.LoadPipelineAction;
import de.osmembrane.controller.actions.NewPipelineAction;
import de.osmembrane.controller.actions.PreviewPipelineAction;
import de.osmembrane.controller.actions.RedoAction;
import de.osmembrane.controller.actions.SavePipelineAction;
import de.osmembrane.controller.actions.ShowHelpAction;
import de.osmembrane.controller.actions.UndoAction;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractFrame;
import de.osmembrane.view.actions.StandardViewAction;
import de.osmembrane.view.actions.ViewAllAction;
import de.osmembrane.view.actions.ZoomInAction;
import de.osmembrane.view.actions.ZoomOutAction;
import de.osmembrane.view.panels.LibraryPanel;
import de.osmembrane.view.panels.InspectorPanel;
import de.osmembrane.view.panels.LibraryPanelGroup;
import de.osmembrane.view.panels.PipelinePanel;
import de.osmembrane.view.panels.LibraryFunction;
import de.osmembrane.view.panels.Tool;

/**
 * The Main window that is the center of OSMembrane and the first thing you'll
 * see after the splash screen.
 * 
 * @author tobias_kuhn
 * 
 */
public class MainFrame extends AbstractFrame {

	private static final long serialVersionUID = 6464462774273555770L;

	private String notification;

	/**
	 * The pipeline panel showing the pipeline
	 */
	private PipelinePanel pipelineView;

	/**
	 * The images, cursors and the selected item of the tools
	 */
	private Map<Tool, ImageIcon> toolsImages;
	private Map<Tool, Cursor> toolsCursors;

	/**
	 * Creates the main frame.
	 * 
	 * @see Spezifikation.pdf, chapter 2.1
	 */
	public MainFrame() {
		// window title
		setWindowTitle(I18N.getInstance().getString("osmembrane"));

		// closing window listener
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				ActionRegistry.getInstance().get(ExitAction.class)
						.actionPerformed(null);
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

		// set own glass pane used for drag & drop
		setGlassPane(new MainFrameGlassPane());

		// initialize tools
		Toolkit tk = Toolkit.getDefaultToolkit();
		toolsImages = new HashMap<Tool, ImageIcon>();

		toolsImages.put(Tool.DEFAULT_MAGIC_TOOL, null);
		toolsImages
				.put(Tool.SELECTION_TOOL,
						new ImageIcon(
								MainFrame.class
										.getResource("/de/osmembrane/resources/cursors/cursor-select.png")));
		toolsImages
				.put(Tool.VIEW_TOOL,
						new ImageIcon(
								MainFrame.class
										.getResource("/de/osmembrane/resources/cursors/cursor-move.png")));
		toolsImages
				.put(Tool.CONNECTION_TOOL,
						new ImageIcon(
								MainFrame.class
										.getResource("/de/osmembrane/resources/cursors/cursor-connect.png")));

		toolsCursors = new HashMap<Tool, Cursor>();
		toolsCursors.put(Tool.DEFAULT_MAGIC_TOOL, Cursor.getDefaultCursor());
		for (Entry<Tool, ImageIcon> e : toolsImages.entrySet()) {
			if (e.getKey() != Tool.DEFAULT_MAGIC_TOOL) {
				toolsCursors.put(e.getKey(), tk.createCustomCursor(e.getValue()
						.getImage(), new Point(0, 0),
						String.valueOf(e.getKey())));
			}
		}

		/*
		 * register all actions that are specific for *this* view and not the
		 * data flow to the model
		 */
		ActionRegistry.getInstance().register(new StandardViewAction());
		ActionRegistry.getInstance().register(new ViewAllAction());
		ActionRegistry.getInstance().register(new ZoomInAction());
		ActionRegistry.getInstance().register(new ZoomOutAction());

		// menu bar
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(I18N.getInstance().getString(
				"View.Menu.File"));
		fileMenu.add(ActionRegistry.getInstance().get(NewPipelineAction.class));
		fileMenu.add(ActionRegistry.getInstance().get(LoadPipelineAction.class));
		fileMenu.add(ActionRegistry.getInstance().get(SavePipelineAction.class));
		fileMenu.add(new JSeparator());
		fileMenu.add(ActionRegistry.getInstance().get(
				ImportPipelineAction.class));
		fileMenu.add(ActionRegistry.getInstance().get(
				ExportPipelineAction.class));
		fileMenu.add(new JSeparator());
		fileMenu.add(ActionRegistry.getInstance().get(
				ChangeSettingsAction.class));
		fileMenu.add(new JSeparator());
		fileMenu.add(ActionRegistry.getInstance().get(ExitAction.class));
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu(I18N.getInstance().getString(
				"View.Menu.Edit"));
		editMenu.add(ActionRegistry.getInstance().get(UndoAction.class));
		editMenu.add(ActionRegistry.getInstance().get(RedoAction.class));
		editMenu.add(new JSeparator());
		editMenu.add(ActionRegistry.getInstance().get(
				DuplicateFunctionAction.class));
		editMenu.add(ActionRegistry.getInstance().get(
				DeleteFunctionAction.class));
		menuBar.add(editMenu);

		JMenu viewMenu = new JMenu(I18N.getInstance().getString(
				"View.Menu.View"));
		editMenu.add(ActionRegistry.getInstance().get(StandardViewAction.class));
		editMenu.add(ActionRegistry.getInstance().get(ViewAllAction.class));
		editMenu.add(new JSeparator());
		editMenu.add(ActionRegistry.getInstance().get(ZoomInAction.class));
		editMenu.add(ActionRegistry.getInstance().get(ZoomOutAction.class));
		menuBar.add(viewMenu);

		JMenu pipelineMenu = new JMenu(I18N.getInstance().getString(
				"View.Menu.Pipeline"));
		pipelineMenu.add(ActionRegistry.getInstance().get(
				PreviewPipelineAction.class));
		pipelineMenu.add(ActionRegistry.getInstance().get(
				GeneratePipelineAction.class));
		pipelineMenu.add(ActionRegistry.getInstance().get(
				ExecutePipelineAction.class));
		menuBar.add(pipelineMenu);

		JMenu aboutMenu = new JMenu(I18N.getInstance().getString(
				"View.Menu.About"));
		aboutMenu.add(ActionRegistry.getInstance().get(ShowHelpAction.class));
		menuBar.add(aboutMenu);

		setJMenuBar(menuBar);

		// tool bar with actions
		JToolBar toolBar = new JToolBar(I18N.getInstance().getString(
				"osmembrane"), JToolBar.HORIZONTAL);
		toolBar.add(ActionRegistry.getInstance().get(NewPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(LoadPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(SavePipelineAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance()
				.get(ImportPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance()
				.get(ExportPipelineAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance().get(UndoAction.class));
		toolBar.add(ActionRegistry.getInstance().get(RedoAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance().get(
				PreviewPipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(
				GeneratePipelineAction.class));
		toolBar.add(ActionRegistry.getInstance().get(
				ExecutePipelineAction.class));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(ActionRegistry.getInstance().get(ShowHelpAction.class));
		getContentPane().add(toolBar, BorderLayout.NORTH);

		// tools bar with tools for editing the pipeline
		JToolBar toolsBar = new JToolBar(I18N.getInstance().getString(
				"osmembrane"), JToolBar.HORIZONTAL);

		ButtonGroup tools = new ButtonGroup();
		// will store the buttons later
		final Map<JToggleButton, Tool> toolsButtons = new HashMap<JToggleButton, Tool>();
		ActionListener toolsButtonsActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Tool t = toolsButtons.get(e.getSource());
				pipelineView.setActiveTool(t, toolsCursors.get(t));
			}
		};

		JToggleButton magicTool = new JToggleButton(
				toolsImages.get(Tool.DEFAULT_MAGIC_TOOL), true);
		magicTool.addActionListener(toolsButtonsActionListener);
		tools.add(magicTool);
		toolsBar.add(magicTool);

		JToggleButton selectTool = new JToggleButton(
				toolsImages.get(Tool.SELECTION_TOOL));
		selectTool.addActionListener(toolsButtonsActionListener);
		tools.add(selectTool);
		toolsBar.add(selectTool);

		JToggleButton viewTool = new JToggleButton(
				toolsImages.get(Tool.VIEW_TOOL));
		viewTool.addActionListener(toolsButtonsActionListener);
		tools.add(viewTool);
		toolsBar.add(viewTool);

		JToggleButton connectTool = new JToggleButton(
				toolsImages.get(Tool.CONNECTION_TOOL));
		connectTool.addActionListener(toolsButtonsActionListener);
		tools.add(connectTool);
		toolsBar.add(connectTool);

		toolsButtons.put(magicTool, Tool.DEFAULT_MAGIC_TOOL);
		toolsButtons.put(selectTool, Tool.SELECTION_TOOL);
		toolsButtons.put(viewTool, Tool.VIEW_TOOL);
		toolsButtons.put(connectTool, Tool.CONNECTION_TOOL);

		toolsBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolsBar.add(ActionRegistry.getInstance().get(StandardViewAction.class));
		toolsBar.add(ActionRegistry.getInstance().get(ViewAllAction.class));
		toolsBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolsBar.add(ActionRegistry.getInstance().get(ZoomInAction.class));
		toolsBar.add(ActionRegistry.getInstance().get(ZoomOutAction.class));
		toolBar.add(toolsBar);

		// function library
		LibraryPanel functionLibrary = new LibraryPanel();
		for (AbstractFunctionGroup afg : ModelProxy.getInstance()
				.accessFunctions().getFunctionGroups()) {
			LibraryPanelGroup lpg = new LibraryPanelGroup(functionLibrary, afg);
			functionLibrary.addGroup(lpg);
		}
		JScrollPane paneLibrary = new JScrollPane(functionLibrary);
		paneLibrary
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		paneLibrary.getVerticalScrollBar().setUnitIncrement(10);

		// function inspector
		InspectorPanel functionInspector = new InspectorPanel();
		JScrollPane paneInspector = new JScrollPane(functionInspector);

		// pipeline view
		pipelineView = new PipelinePanel(functionInspector);
		JScrollPane panePipeline = new JScrollPane(pipelineView);

		// split containers
		JSplitPane splitLibAndView = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, true, paneLibrary, panePipeline);

		JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, splitLibAndView, paneInspector);
		getContentPane().add(splitMain);

		// center, then maximize
		pack();
		pipelineView.setPreferredSize(new Dimension((int) (getWidth() * 0.7),
				pipelineView.getPreferredSize().height));
		pack();
		centerWindow();
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	/**
	 * @param notification
	 *            the notification to set
	 */
	public void setNotification(String notification) {
		this.notification = notification;
	}

	/**
	 * @return the pipeline panel
	 */
	public PipelinePanel getPipeline() {
		return pipelineView;
	}

	/**
	 * Draws a specific view function that is currently dragged & drop where the
	 * cursor is
	 * 
	 * @param libraryFunction
	 *            the view function to be drawn
	 * @param point
	 *            top left position of the view function to be drawn
	 */
	public void paintDragAndDrop(LibraryFunction libraryFunction, Point point) {
		MainFrameGlassPane mfgp = (MainFrameGlassPane) getGlassPane();
		mfgp.dragAndDrop(libraryFunction, point);
	}

	/**
	 * Stops the drag and drop display
	 */
	public void endDragAndDrop() {
		MainFrameGlassPane mfgp = (MainFrameGlassPane) getGlassPane();
		mfgp.endDragAndDrop();
	}

}