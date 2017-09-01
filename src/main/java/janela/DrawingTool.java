package janela;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.paim.orchestration.StructureSlice;

/**
 * Drawing tool
 */
public abstract class DrawingTool implements MouseListener, MouseMotionListener {

    /** Structure slice */
    protected final StructureSlice slice;

    /**
     * Creates a new drawing tool
     * 
     * @param slice 
     */
    public DrawingTool(StructureSlice slice) {
        this.slice = slice;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    /**
     * Draws the tool GUI
     * 
     * @param g2d 
     */
    public void paint(Graphics2D g2d) {
    }
    
}
