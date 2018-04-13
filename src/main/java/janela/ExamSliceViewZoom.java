package janela;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.function.Predicate;
import org.paim.commons.ExamSlice;
import org.paim.orchestration.ExamResultSlice;
import org.paim.orchestration.StructureType;

/**
 * Exam slice view
 */
public class ExamSliceViewZoom extends ExamSliceView {

    /** The zoom scale */
    private final float ZOOM_SCALE = 0.5f;
    /** Image view */
    private float zoom = 1;

    /**
     * Creates a new exam slice view
     *
     * @param slice
     * @param structureFilter
     * @param wl
     * @param ww
     */
    public ExamSliceViewZoom(ExamResultSlice slice, Predicate<StructureType> structureFilter, int wl, int ww) {
        super(slice, structureFilter, wl, ww);
        initEvents();
    }

    private void initEvents() {
        addMouseListener(new ZoomListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoom, zoom);
        super.paintComponent(g);
    }

    private class ZoomListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isControlDown()) {
                zoom -= ZOOM_SCALE;
                if (zoom < 1) {
                    zoom = 1;
                }
            } else {
                zoom += ZOOM_SCALE;
            }
            BufferedImage image = getExamImage();
            int width = (int) (image.getWidth() * zoom);
            int height = (int) (image.getHeight() * zoom);
            setPreferredSize(new Dimension(width, height));
            repaint();
            revalidate();
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
    }

}
