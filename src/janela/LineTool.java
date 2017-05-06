package janela;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import org.torax.orchestration.StructureSlice;

/**
 * Line tool
 */
public class LineTool extends DrawingTool {

    /** X Points */
    private final List<Integer> xPoints;
    /** Y Points */
    private final List<Integer> yPoints;
    
    /**
     * Creates the line tool
     * 
     * @param slice 
     */
    public LineTool(StructureSlice slice) {
        super(slice);
        xPoints = new ArrayList<>();
        yPoints = new ArrayList<>();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        xPoints.add(x);
        yPoints.add(y);
        if (xPoints.size() > 2 && e.getPoint().distance(xPoints.get(0), yPoints.get(0)) < 5) {
            close();
        }
    }
    
    /**
     * Closes the shape
     */
    private void close() {
        System.out.println("close");
        Polygon polygon = new Polygon(array(xPoints), array(yPoints), xPoints.size());
        Rectangle bounds = polygon.getBounds();
        for (int x = bounds.x; x < bounds.x + bounds.width; x++) {
            for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
                if (polygon.contains(x, y)) {
                    slice.getBinaryLabel()[x][y] = true;
                }
            }
        }
        xPoints.clear();
        yPoints.clear();
    }
    
    @Override
    public void paint(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        g2d.drawPolyline(array(xPoints), array(yPoints), xPoints.size());
        g2d.dispose();
    }
    
    /**
     * Converts a list do an array
     * 
     * @param ints
     * @return int[]
     */
    private int[] array(List<Integer> ints) {
        int[] array = new int[ints.size()];
        for (int i = 0; i < ints.size(); i++) {
            array[i] = ints.get(i);
        }
        return array;
    }
    
}
