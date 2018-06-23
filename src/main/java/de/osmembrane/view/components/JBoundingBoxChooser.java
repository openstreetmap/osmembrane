package de.osmembrane.view.components;

import de.osmembrane.model.pipeline.BoundingBox;
import de.osmembrane.tools.I18N;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListDataListener;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class JBoundingBoxChooser extends JPanel {

    private static final Color SELECTION_FILL_COLOR = new Color(128, 192, 255, 128);
    private static final Color SELECTION_FRAME_COLOR = new Color(0, 0, 255, 128);

    private final JXMapViewer mapViewer;
    private final JLabel description;
    private GeoPosition firstPoint, secondPoint;

    public JBoundingBoxChooser() {
        firstPoint = new GeoPosition(0.0, 0.0);
        secondPoint = new GeoPosition(0.0, 0.0);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(8, 8, 8, 8);

        description = new JLabel();
        add(description, gbc);

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        TileFactoryInfoComboModel comboModel = new TileFactoryInfoComboModel();
        JComboBox<String> tileFactoryChooser = new JComboBox<>(comboModel);
        add(tileFactoryChooser, gbc);

        mapViewer = new JXMapViewer();
        comboModel.setMapViewer(mapViewer);
        comboModel.setSelectedItem(comboModel.getElementAt(0));

        addInteractions(mapViewer);
        addSelectionAdapter(mapViewer);

        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(mapViewer, gbc);

        setPreferredSize(new Dimension(800, 600));
    }

    private void initializeZoom() {
        Set<GeoPosition> fit = new HashSet<>();
        if (firstPoint.getLatitude() != secondPoint.getLatitude() && firstPoint.getLongitude() != secondPoint.getLongitude()) {
            fit.add(firstPoint);
            fit.add(secondPoint);
            mapViewer.zoomToBestFit(fit, 0.8);
        } else {
            mapViewer.setZoom(mapViewer.getTileFactory().getInfo().getMaximumZoomLevel());
            mapViewer.setAddressLocation(new GeoPosition(0.0, 0.0));
        }
    }

    private void addInteractions(JXMapViewer mapViewer) {
        MouseInputListener mouseInputListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseInputListener);
        mapViewer.addMouseMotionListener(mouseInputListener);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
    }

    private void addSelectionAdapter(JXMapViewer mapViewer) {
        SelectionAdapter selectionAdapter = new SelectionAdapter();
        Painter<Object> painter = new Painter<Object>() {
            @Override
            public void paint(Graphics2D g, Object object, int width, int height) {
                Rectangle selection = getSelectionRectangle();
                if (selection.width > 0 && selection.height > 0) {
                    g.setColor(SELECTION_FRAME_COLOR);
                    g.draw(selection);
                    g.setColor(SELECTION_FILL_COLOR);
                    g.fill(selection);
                }
            }
        };
        mapViewer.addMouseListener(selectionAdapter);
        mapViewer.addMouseMotionListener(selectionAdapter);
        mapViewer.setOverlayPainter(painter);
    }

    public BoundingBox getBoundingBox() {
        double firstLatitude = Math.min(firstPoint.getLatitude(), secondPoint.getLatitude());
        double secondLatitude = Math.max(firstPoint.getLatitude(), secondPoint.getLatitude());
        double firstLongitude = Math.min(firstPoint.getLongitude(), secondPoint.getLongitude());
        double secondLongitude = Math.max(firstPoint.getLongitude(), secondPoint.getLongitude());

        return new BoundingBox(firstLatitude, firstLongitude, secondLatitude, secondLongitude);
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.firstPoint = new GeoPosition(boundingBox.getNWLatitude(), boundingBox.getNWLongitude());
        this.secondPoint = new GeoPosition(boundingBox.getSELatitude(), boundingBox.getSELongitude());
        initializeZoom();
        repaintUI();
    }

    private void clickBoundingBoxStart(int x, int y) {
        this.firstPoint = mapViewer.getTileFactory().pixelToGeo(new Point2D.Double(x, y), mapViewer.getZoom());
        this.secondPoint = this.firstPoint;
        repaintUI();
    }

    private void dragBoundingBoxEnd(int x, int y) {
        this.secondPoint = mapViewer.getTileFactory().pixelToGeo(new Point2D.Double(x, y), mapViewer.getZoom());
        repaintUI();
    }

    private void repaintUI() {
        description.setText(I18N.getInstance().getString("View.BoundingBoxDialog.Description",
                latitudeToString(firstPoint.getLatitude()), longitudeToString(firstPoint.getLongitude()),
                latitudeToString(secondPoint.getLatitude()), longitudeToString(secondPoint.getLongitude())));
        mapViewer.repaint();
    }

    private String latitudeToString(double latitude) {
        return String.format("%.4f %s", latitude, latitude < 0 ? "S" : "N");
    }

    private String longitudeToString(double longitude) {
        return String.format("%.4f %s", longitude, longitude < 0 ? "W" : "E");
    }

    private Rectangle getSelectionRectangle() {
        Point2D first = mapViewer.getTileFactory().geoToPixel(firstPoint, mapViewer.getZoom());
        Point2D second = mapViewer.getTileFactory().geoToPixel(secondPoint, mapViewer.getZoom());

        int x1 = (int) Math.min(first.getX(), second.getX());
        int x2 = (int) Math.max(first.getX(), second.getX());
        int y1 = (int) Math.min(first.getY(), second.getY());
        int y2 = (int) Math.max(first.getY(), second.getY());

        Rectangle viewport = mapViewer.getViewportBounds();
        return new Rectangle(x1 - viewport.x, y1 - viewport.y, x2 - x1, y2 - y1);
    }

    private class SelectionAdapter extends MouseAdapter {
        private boolean dragging;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                Rectangle viewport = mapViewer.getViewportBounds();
                clickBoundingBoxStart(e.getX() + viewport.x, e.getY() + viewport.y);
                dragging = true;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (dragging) {
                Rectangle viewport = mapViewer.getViewportBounds();
                dragBoundingBoxEnd(e.getX() + viewport.x, e.getY() + viewport.y);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragging && e.getButton() == MouseEvent.BUTTON3) {
                dragging = false;
            }
        }
    }

    private class TileFactoryInfoComboModel implements ComboBoxModel<String> {

        private Map<String, TileFactoryInfo> factories;
        private List<String> values;
        private int selection;
        private JXMapViewer mapViewer;

        private TileFactoryInfoComboModel() {
            selection = 0;
            factories = new HashMap<>();
            values = new ArrayList<>();
            addFactory(new OSMTileFactoryInfo(), null);
            addFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP), "Map");
            addFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID), "Hybrid");
            addFactory(new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE), "Satellite");
        }

        private void addFactory(TileFactoryInfo info, String additionalString) {
            String title;
            if (additionalString == null) {
                title = info.getName();
            } else {
                title = info.getName() + " (" + additionalString + ")";
            }
            factories.put(title, info);
            values.add(title);
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        @Override
        public void setSelectedItem(Object anItem) {
            if (!values.contains(anItem))
                throw new IllegalStateException("ComboBoxModel cannot select non-existing object");
            selection = values.indexOf(anItem);
            if (mapViewer != null) {
                TileFactoryInfo factoryToUse = factories.get(anItem);
                DefaultTileFactory factory = new DefaultTileFactory(factoryToUse);
                mapViewer.setTileFactory(factory);
                factory.setThreadPoolSize(Runtime.getRuntime().availableProcessors());
            }
        }

        @Override
        public Object getSelectedItem() {
            return values.get(selection);
        }

        @Override
        public int getSize() {
            return values.size();
        }

        @Override
        public String getElementAt(int index) {
            return values.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }

        public void setMapViewer(JXMapViewer mapViewer) {
            this.mapViewer = mapViewer;
        }
    }
}
