package janela;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.paim.commons.ImageFactory;
import org.paim.orchestration.ExamResultSlice;
import org.paim.orchestration.StructureSlice;
import org.paim.orchestration.StructureType;

/**
 * Panel for manually drawing structures
 */
public class StructureDrawPanel extends JPanel {

    /** Slice of the exam */
    private final ExamResultSlice slice;
    /** Type of structure that is being drawn */
    private StructureType type;
    /** Exam slice view */
    private ExamSliceView view;
    /** Drawing tool */
    private DrawingTool drawingTool;
    private JPanel toolsPanel;
    private int wl;
    private int ww;
    
    /**
     * Creates a new panel for manually drawing structures
     * 
     * @param slice 
     * @param wl 
     * @param ww 
     */
    public StructureDrawPanel(ExamResultSlice slice, int wl, int ww) {
        super();
        this.slice = slice;
        this.type = StructureType.values()[0];
        this.drawingTool = new BrushTool(slice.getStructure(type));
        this.wl = wl;
        this.ww = ww;
        initGui();
    }
    
    /**
     * Initializes the interface
     */
    private void initGui() {
        setLayout(new BorderLayout());
        add(buildDrawingPanel());
        add(buildToolsPanel(), BorderLayout.EAST);
        add(buildStructureSelector(), BorderLayout.SOUTH);
    }

    /**
     * Creates the drawing panel
     * 
     * @return JComponent
     */
    private JComponent buildDrawingPanel() {
        view = new ExamSliceView(slice, (structure) -> structure == type, wl, ww) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                drawingTool.paint(g2d);
                g2d.dispose();
            }
        };
        DrawingMouseListener listener = new DrawingMouseListener();
        view.addMouseListener(listener);
        view.addMouseMotionListener(listener);
        return view;
    }

    /**
     * Creates the tools panel
     * 
     * @return JComponent
     */
    private JComponent buildToolsPanel() {
        toolsPanel = new JPanel();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.Y_AXIS));
        updateToolsPanel();
        return toolsPanel;
    }
    
    /**
     * Update the tools panel
     */
    private void updateToolsPanel() {
        toolsPanel.removeAll();
        toolsPanel.add(new JButton(new ChangeToolAction(new BrushTool(slice.getStructure(type)))));
        toolsPanel.add(new JButton(new ChangeToolAction(new LineTool(slice.getStructure(type)))));
        toolsPanel.add(new JButton(new ClearAction(slice.getStructure(type))));
        toolsPanel.revalidate();
        drawingTool = new BrushTool(slice.getStructure(type));
    }
    
    /**
     * Creates the structure type selector
     * 
     * @return JComponent
     */
    private JComponent buildStructureSelector() {
        JComboBox<StructureType> selector = new JComboBox<>(StructureType.values());
        selector.addItemListener((evt) -> {
            this.type = (StructureType) selector.getSelectedItem();
            updateToolsPanel();
            view.setStructureFilter((structure) -> structure == type);
            view.repaint();
        });
        return selector;
    }
    
    /**
     * Show the panel in a different dialog
     */
    public void showDialog() {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setContentPane(this);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Clear the whole drawing
     */
    public class ClearAction extends AbstractAction {

        /** Slice to be cleared */
        private final StructureSlice slice;
        
        /**
         * Creates the action
         * 
         * @param slice 
         */
        public ClearAction(StructureSlice slice) {
            super("Clear");
            this.slice = slice;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            slice.setBinaryLabel(ImageFactory.buildBinaryImage(slice.getWidth(), slice.getHeight()));
        }
        
    }

    /**
     * Action for changing the tool
     */
    private class ChangeToolAction extends AbstractAction {

        /** Tool */
        private final DrawingTool tool;

        /**
         * Creates the action
         * 
         * @param tool 
         */
        public ChangeToolAction(DrawingTool tool) {
            super(tool.getClass().getSimpleName());
            this.tool = tool;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawingTool = tool;
            view.repaint();
        }
    }

    /**
     * Mouse listener for the drawings
     */
    private class DrawingMouseListener implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            drawingTool.mouseClicked(e);
            view.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            drawingTool.mousePressed(e);
            view.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            drawingTool.mouseReleased(e);
            view.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            drawingTool.mouseEntered(e);
            view.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            drawingTool.mouseExited(e);
            view.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            drawingTool.mouseDragged(e);
            view.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            drawingTool.mouseMoved(e);
            view.repaint();
        }
    }
    
}
