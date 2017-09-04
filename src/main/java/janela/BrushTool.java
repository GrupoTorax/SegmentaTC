package janela;

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.paim.orchestration.StructureSlice;

/**
 * Brush tool
 */
public class BrushTool extends DrawingTool {

    /** Brush size */
    private int size;
    
    /**
     * Creates the brush tool
     * 
     * @param slice 
     */
    public BrushTool(StructureSlice slice) {
        super(slice);
        size = 5;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        for (int i = -size/2; i < size - (size/2); i++) {
            for (int j = -size/2; j < size - (size/2); j++) {
                int x = (int) p.getX() + i;
                int y = (int) p.getY() + j;
                if (x < 0 || y < 0 || x >= slice.getWidth() || y >= slice.getHeight()) {
                    continue;
                }
                slice.getBinaryLabel().set(x, y, true);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        for (int i = -size/2; i < size - (size/2); i++) {
            for (int j = -size/2; j < size - (size/2); j++) {
                int x = (int) p.getX() + i;
                int y = (int) p.getY() + j;
                if (x < 0 || y < 0 || x >= slice.getWidth() || y >= slice.getHeight()) {
                    continue;
                }
                slice.getBinaryLabel().set(x, y, true);
            }
        }
    }

    /**
     * Returns the size of the brush
     * 
     * @return int
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of the brush
     * 
     * @param size 
     */
    public void setSize(int size) {
        this.size = size;
    }
    
}
