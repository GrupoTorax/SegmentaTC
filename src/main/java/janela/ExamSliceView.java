/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janela;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.function.Predicate;
import javax.swing.JComponent;
import org.paim.commons.BinaryImage;
import org.paim.orchestration.ExamResultSlice;
import org.paim.orchestration.StructureSlice;
import org.paim.orchestration.StructureType;

/**
 * Exam slice view
 */
public class ExamSliceView extends JComponent {

    /** View of a slice of the exam */
    private ExamResultSlice slice;
    /** Filter for showing structures */
    private Predicate<StructureType> structureFilter;
    private int wl;
    private int ww;

    /**
     * Creates a new exam slice view
     *
     * @param slice
     * @param structureFilter
     * @param wl
     * @param ww
     */
    public ExamSliceView(ExamResultSlice slice, Predicate<StructureType> structureFilter, int wl, int ww) {
        this.slice = slice;
        this.structureFilter = structureFilter;
        this.wl = wl;
        this.ww = ww;
    }

    @Override
    public Dimension getPreferredSize() {
        if (slice == null) {
            return new Dimension(512, 512);
        }
        return slice.getExam().getSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        if (slice != null) {
            paintExam(g2d);
            paintStructures(g2d);
        }
        g2d.dispose();
    }

    /**
     * Draws the exam
     *
     * @param g2d
     */
    private void paintExam(Graphics2D g2d) {
        g2d.drawImage(slice.getExam().getBufferedImageWithWLWW(wl, ww), 0, 0, this);
    }

    /**
     * Draws the structures
     *
     * @param g2d
     */
    private void paintStructures(Graphics2D g2d) {
        for (Map.Entry<StructureType, StructureSlice> entry : slice.getStructures().entrySet()) {
            if (structureFilter.test(entry.getKey())) {
                paintStructure(g2d, entry.getKey(), entry.getValue());
            }
        }
    }

    private void paintStructure(Graphics2D g2d, StructureType type, StructureSlice value) {
        BinaryImage matrix = value.getBinaryLabel();
        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int ix = 0; ix < matrix.getWidth(); ix++) {
            for (int iy = 0; iy < matrix.getHeight(); iy++) {
                if (matrix.get(ix, iy)) {
                    image.setRGB(ix, iy, type.getColor().getRGB());
                }
            }
        }
        g2d.drawImage(image, 0, 0, this);
    }

    /**
     * Returns the slice
     *
     * @return ExamResultSlice
     */
    public ExamResultSlice getSlice() {
        return slice;
    }

    /**
     * Sets the slice
     *
     * @param slice
     */
    public void setSlice(ExamResultSlice slice) {
        this.slice = slice;
        revalidate();
        repaint();
    }

    /**
     * Returns the structure filter
     *
     * @return Predicate
     */
    public Predicate<StructureType> getStructureFilter() {
        return structureFilter;
    }

    /**
     * Sets the structure filter
     *
     * @param structureFilter
     */
    public void setStructureFilter(Predicate<StructureType> structureFilter) {
        this.structureFilter = structureFilter;
        repaint();
    }

    public int getWl() {
        return wl;
    }

    public void setWl(int wl) {
        this.wl = wl;
        repaint();
    }

    public int getWw() {
        return ww;
    }

    public void setWw(int ww) {
        this.ww = ww;
        repaint();
    }

}
