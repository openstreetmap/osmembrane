/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.view.frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.ArrangePipelineAction;
import de.osmembrane.controller.actions.ChangePipelineSettingsAction;
import de.osmembrane.controller.actions.ChangeSettingsAction;
import de.osmembrane.controller.actions.DeleteSelectionAction;
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
import de.osmembrane.controller.actions.ResetViewAction;
import de.osmembrane.controller.actions.SaveAsPipelineAction;
import de.osmembrane.controller.actions.SavePipelineAction;
import de.osmembrane.controller.actions.ShowAboutAction;
import de.osmembrane.controller.actions.ShowHelpAction;
import de.osmembrane.controller.actions.ShowQuickstartAction;
import de.osmembrane.controller.actions.UndoAction;
import de.osmembrane.controller.actions.ViewAllAction;
import de.osmembrane.controller.actions.ZoomInAction;
import de.osmembrane.controller.actions.ZoomOutAction;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.AbstractFrame;
import de.osmembrane.view.interfaces.IMainFrame;
import de.osmembrane.view.interfaces.IZoomDevice;
import de.osmembrane.view.panels.InspectorPanel;
import de.osmembrane.view.panels.LibraryPanel;
import de.osmembrane.view.panels.LibraryPanelGroup;
import de.osmembrane.view.panels.PipelinePanel;
import de.osmembrane.view.panels.Tool;

/**
 * The Main window that is the center of OSMembrane and the first thing you'll
 * see after the splash screen.
 * 
 * @see "Spezifikation.pdf, chapter 2.1 (German)"
 * 
 * @author tobias_kuhn
 * 
 */
public class MainFrame extends AbstractFrame implements IMainFrame {

    private static final long serialVersionUID = 6464462774273555770L;

    /**
     * The PipelinePanel showing the pipeline
     */
    private PipelinePanel pipelineView;

    /**
     * The images, cursors and the selected item of the tools
     */
    private Map<Tool, ImageIcon> toolsImages;
    private Map<Tool, Cursor> toolsCursors;

    /**
     * Creates the {@link MainFrame}.
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
        boolean showStartScreen = (Boolean) ModelProxy.getInstance()
                .getSettings().getValue(SettingType.SHOW_STARTUP_SCREEN)
                && (ModelProxy.getInstance().getPipeline().getFunctions().length == 0);
        // if there is something on the pipeline, probably a backup was loaded
        setGlassPane(new MainFrameGlassPane(showStartScreen));
        getGlassPane().setVisible(showStartScreen);

        // initialize tools
        Toolkit tk = Toolkit.getDefaultToolkit();
        toolsImages = new HashMap<Tool, ImageIcon>();

        toolsImages.put(Tool.DEFAULT_MAGIC_TOOL, Resource.CURSOR_ICON
                .getImageIcon("cursor-magic.png", Size.NORMAL));

        toolsImages.put(Tool.SELECTION_TOOL, Resource.CURSOR_ICON.getImageIcon(
                "cursor-select.png", Size.NORMAL));

        toolsImages.put(Tool.VIEW_TOOL, Resource.CURSOR_ICON.getImageIcon(
                "cursor-move.png", Size.NORMAL));

        toolsImages.put(Tool.CONNECTION_TOOL, Resource.CURSOR_ICON
                .getImageIcon("cursor-connect.png", Size.NORMAL));

        toolsCursors = new HashMap<Tool, Cursor>();
        toolsCursors.put(Tool.DEFAULT_MAGIC_TOOL, Cursor.getDefaultCursor());
        for (Entry<Tool, ImageIcon> e : toolsImages.entrySet()) {
            if (e.getKey() != Tool.DEFAULT_MAGIC_TOOL) {
                toolsCursors.put(e.getKey(), tk.createCustomCursor(e.getValue()
                        .getImage(), new Point(0, 0),
                        String.valueOf(e.getKey())));
            }
        }

        // menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(I18N.getInstance().getString(
                "View.Menu.File"));
        fileMenu.add(ActionRegistry.getInstance().get(NewPipelineAction.class));
        fileMenu.add(ActionRegistry.getInstance().get(LoadPipelineAction.class));
        fileMenu.add(ActionRegistry.getInstance().get(SavePipelineAction.class));
        fileMenu.add(ActionRegistry.getInstance().get(
                SaveAsPipelineAction.class));
        fileMenu.add(new JSeparator());
        fileMenu.add(ActionRegistry.getInstance().get(
                ImportPipelineAction.class));
        fileMenu.add(ActionRegistry.getInstance().get(
                ExportPipelineAction.class));
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
                DeleteSelectionAction.class));
        menuBar.add(editMenu);

        JMenu viewMenu = new JMenu(I18N.getInstance().getString(
                "View.Menu.View"));
        viewMenu.add(ActionRegistry.getInstance().get(ResetViewAction.class));
        viewMenu.add(ActionRegistry.getInstance().get(ViewAllAction.class));
        viewMenu.add(new JSeparator());
        viewMenu.add(ActionRegistry.getInstance().get(ZoomInAction.class));
        viewMenu.add(ActionRegistry.getInstance().get(ZoomOutAction.class));
        menuBar.add(viewMenu);

        JMenu pipelineMenu = new JMenu(I18N.getInstance().getString(
                "View.Menu.Pipeline"));
        pipelineMenu.add(ActionRegistry.getInstance().get(
                ArrangePipelineAction.class));
        pipelineMenu.add(ActionRegistry.getInstance().get(
                ChangePipelineSettingsAction.class));
        pipelineMenu.add(new JSeparator());
        pipelineMenu.add(ActionRegistry.getInstance().get(
                GeneratePipelineAction.class));
        pipelineMenu.add(new JSeparator());
        pipelineMenu.add(ActionRegistry.getInstance().get(
                ExecutePipelineAction.class));
        pipelineMenu.add(ActionRegistry.getInstance().get(
                PreviewPipelineAction.class));
        menuBar.add(pipelineMenu);

        JMenu extrasMenu = new JMenu(I18N.getInstance().getString(
                "View.Menu.Extras"));
        extrasMenu.add(ActionRegistry.getInstance().get(
                ChangeSettingsAction.class));
        menuBar.add(extrasMenu);

        JMenu aboutMenu = new JMenu(I18N.getInstance().getString(
                "View.Menu.About"));
        aboutMenu.add(ActionRegistry.getInstance().get(ShowHelpAction.class));
        aboutMenu.add(ActionRegistry.getInstance().get(
                ShowQuickstartAction.class));
        aboutMenu.add(new JSeparator());
        aboutMenu.add(ActionRegistry.getInstance().get(ShowAboutAction.class));
        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);

        // also provide a popupMenu
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(ActionRegistry.getInstance().get(
                DuplicateFunctionAction.class));
        popupMenu.add(ActionRegistry.getInstance().get(
                DeleteSelectionAction.class));
        popupMenu.add(new JSeparator());
        popupMenu.add(ActionRegistry.getInstance().get(UndoAction.class));
        popupMenu.add(ActionRegistry.getInstance().get(RedoAction.class));
        popupMenu.add(new JSeparator());
        popupMenu.add(ActionRegistry.getInstance().get(ResetViewAction.class));
        popupMenu.add(ActionRegistry.getInstance().get(ViewAllAction.class));

        // tool bar with actions
        JToolBar toolBar = new JToolBar(I18N.getInstance().getString(
                "osmembrane"), SwingConstants.HORIZONTAL);
        if (!UIManager.getLookAndFeel().getName().toLowerCase()
                .contains("nimbus")) {
            toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

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
        toolBar.add(ActionRegistry.getInstance().get(
                ArrangePipelineAction.class));
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(ActionRegistry.getInstance().get(ShowHelpAction.class));
        getContentPane().add(toolBar, BorderLayout.NORTH);

        // tools bar with tools for editing the pipeline
        JToolBar toolsBar = new JToolBar(I18N.getInstance().getString(
                "osmembrane"), SwingConstants.HORIZONTAL);
        if (!UIManager.getLookAndFeel().getName().toLowerCase()
                .contains("nimbus")) {
            toolsBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

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
        magicTool.setToolTipText(I18N.getInstance().getString(
                "View.Tools.Magic"));
        magicTool.addActionListener(toolsButtonsActionListener);
        tools.add(magicTool);
        toolsBar.add(magicTool);

        JToggleButton selectTool = new JToggleButton(
                toolsImages.get(Tool.SELECTION_TOOL));
        selectTool.setToolTipText(I18N.getInstance().getString(
                "View.Tools.Select"));
        selectTool.addActionListener(toolsButtonsActionListener);
        tools.add(selectTool);
        toolsBar.add(selectTool);

        JToggleButton viewTool = new JToggleButton(
                toolsImages.get(Tool.VIEW_TOOL));
        viewTool.setToolTipText(I18N.getInstance().getString("View.Tools.View"));
        viewTool.addActionListener(toolsButtonsActionListener);
        tools.add(viewTool);
        toolsBar.add(viewTool);

        JToggleButton connectTool = new JToggleButton(
                toolsImages.get(Tool.CONNECTION_TOOL));
        connectTool.setToolTipText(I18N.getInstance().getString(
                "View.Tools.Connect"));
        connectTool.addActionListener(toolsButtonsActionListener);
        tools.add(connectTool);
        toolsBar.add(connectTool);

        toolsButtons.put(magicTool, Tool.DEFAULT_MAGIC_TOOL);
        toolsButtons.put(selectTool, Tool.SELECTION_TOOL);
        toolsButtons.put(viewTool, Tool.VIEW_TOOL);
        toolsButtons.put(connectTool, Tool.CONNECTION_TOOL);

        toolsBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolsBar.add(ActionRegistry.getInstance().get(ResetViewAction.class));
        toolsBar.add(ActionRegistry.getInstance().get(ViewAllAction.class));
        toolsBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolsBar.add(ActionRegistry.getInstance().get(ZoomInAction.class));
        toolsBar.add(ActionRegistry.getInstance().get(ZoomOutAction.class));
        toolBar.add(toolsBar);

        /* yes the order is important */

        // function inspector
        InspectorPanel functionInspector = new InspectorPanel();

        // pipeline view
        pipelineView = new PipelinePanel(functionInspector, popupMenu);
        JPanel panePipeline = new JPanel(new BorderLayout());
        panePipeline.add(pipelineView, BorderLayout.CENTER);
        panePipeline.add(pipelineView.getVerticalScroll(), BorderLayout.EAST);
        panePipeline
                .add(pipelineView.getHorizontalScroll(), BorderLayout.SOUTH);

        // function library
        LibraryPanel functionLibrary = new LibraryPanel(pipelineView);

        for (AbstractFunctionGroup afg : ModelProxy.getInstance()
                .getFunctions().getFunctionGroups()) {
            LibraryPanelGroup lpg = new LibraryPanelGroup(functionLibrary,
                    pipelineView, afg);
            functionLibrary.addGroup(lpg);
        }
        JScrollPane paneLibrary = new JScrollPane(functionLibrary);
        paneLibrary
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        paneLibrary.getVerticalScrollBar().setUnitIncrement(10);

        // split containers
        JSplitPane splitLibAndView = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, true, paneLibrary, panePipeline);

        JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                true, splitLibAndView, functionInspector);

        splitMain.setResizeWeight(1.0);
        getContentPane().add(splitMain);

        // set the application icon
        Image icon = getToolkit().getImage(
                getClass().getResource(
                        "/de/osmembrane/resources/images/icon.png"));
        setIconImage(icon);

        // center, then maximize
        pack();
        centerWindow();
        if (!showStartScreen) {
            setExtendedState(Frame.MAXIMIZED_BOTH);
        }

        // correct width
        splitMain.setDividerLocation(splitMain.getSize().width
                - splitMain.getInsets().right - splitMain.getDividerSize()
                - 384);
    }

    @Override
    public void setWindowTitle(String title) {
        super.setWindowTitle(title + " - "
                + I18N.getInstance().getString("osmembrane"));
    }

    @Override
    public Object getSelected() {
        return pipelineView.getSelected();
    }

    @Override
    public void setHint(String hint) {
        pipelineView.setHint(hint);
    }

    @Override
    public MainFrameGlassPane getMainGlassPane() {
        return (MainFrameGlassPane) getGlassPane();
    }

    @Override
    public boolean isDragAndDropTarget(Point at) {
        Point framePoint = SwingUtilities.convertPoint(this.getGlassPane(), at,
                this);

        Component target = findComponentAt(framePoint);

        return (pipelineView.getLayeredPane().equals(target))
                || (pipelineView.getLayeredPane().equals(target.getParent()));
    }

    @Override
    public IZoomDevice getZoomDevice() {
        return pipelineView;
    }

    @Override
    public void maximizeWindow() {
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

}
