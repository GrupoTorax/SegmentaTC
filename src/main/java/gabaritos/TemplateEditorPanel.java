package gabaritos;

import janela.StructureDrawPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import org.paim.commons.BinaryImage;
import org.paim.commons.ExamSlice;
import org.paim.commons.ImageFactory;
import org.paim.examsio.ExamLoaderException;
import org.paim.orchestration.ExamResultSlice;
import org.paim.orchestration.StructureSlice;
import org.paim.orchestration.StructureType;

/**
 * Template editor panel
 */
public class TemplateEditorPanel extends JPanel {

    /** Template */
    private final Template template;
    /** Test model */
    private TestSlicesModel testModel;
    /** Container for the StructureDrawPanel */
    private JPanel panelStructureDraw;
    /** Current slice */
    private SliceTemplate currentSlice;
    /** Current exam result slice */
    private ExamResultSlice currentResultSlice;
    
    public TemplateEditorPanel(Template template) {
        super();
        this.template = template;
        try {
            testModel = TestSlicesModel.load();
        } catch(ExamLoaderException e) {
            e.printStackTrace();
        }
        initGui();
    }

    /**
     * Initializes the interface
     */
    private void initGui() {
        setLayout(new BorderLayout());
        add(buildPanelInfoTemplate(), BorderLayout.NORTH);
        add(buildMainPanel());
        add(buildButtonsPanel(), BorderLayout.SOUTH);
    }

    /**
     * Builds the template information panel
     * 
     * @return JComponent
     */
    private JComponent buildPanelInfoTemplate() {
        return new JPanel();
    }

    /**
     * Builds the main panel
     * 
     * @return JComponent
     */
    private JComponent buildMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(buildPanelSlicesGrid(), BorderLayout.WEST);
        panel.add(buildPanelSliceEdit());
        return panel;
    }

    /**
     * Builds the slices selection grid
     * 
     * @return JComponent
     */
    private JComponent buildPanelSlicesGrid() {
        Object[][] data = new Object[testModel.getSlices().size()][1];
        for (int i = 0; i < data.length; i++) {
           data[i][0] = testModel.getSlices().get(i).getSourceFile();
        }
        JTable table = new JTable(data, new Object[] {"Exam"});
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener((evt) -> {
            if (!evt.getValueIsAdjusting()) {
                updateCurrentSlice(table.getSelectedRow());
            }
        });
        return new JScrollPane(table);
    }

    /**
     * Builds the panel edit slice
     * 
     * @return JComponent
     */
    private JComponent buildPanelSliceEdit() {
        panelStructureDraw = new JPanel(new BorderLayout());
        updateCurrentSlice(0);
        return panelStructureDraw;
    }
    
    /**
     * Updates the current slice
     * 
     * @param index 
     */
    private void updateCurrentSlice(int index) {
        SwingUtilities.invokeLater(() -> {
            panelStructureDraw.removeAll();
            ExamSlice slice = testModel.getSlices().get(index);
            Map<StructureType, StructureSlice> map = new HashMap<>();
            BinaryImage image = ImageFactory.buildBinaryImage(slice.getColumns(), slice.getRows());
            if (index <= template.getSlices().size() - 1) {
                currentSlice = template.getSlices().get(index);
                currentSlice.getStructures().get(StructureType.HEART);
            }
            map.put(StructureType.HEART, new StructureSlice(image));
            currentResultSlice = new ExamResultSlice(map, slice);
            panelStructureDraw.add(new StructureDrawPanel(currentResultSlice, -400, 1500));
            panelStructureDraw.revalidate();
            panelStructureDraw.repaint();
        });
    }
    
    /**
     * Builds the button panel
     * 
     * @return JComponent
     */
    private JComponent buildButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());
        panel.add(buildSaveButton());
        return panel;
    }
    
    /**
     * Builds the save button
     * 
     * @return JComponent
     */
    private JComponent buildSaveButton() {
        JButton button = new JButton("Save");
        button.addActionListener((t) -> {
            
        });
        return button;
    }
    
    /**
     * Shows the panel in a dialog
     * 
     * @param template
     */
    public static void showDialog(Template template) {
        JDialog dialog = new JDialog((Window)null, "Template edit");
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(new TemplateEditorPanel(template));
        SwingUtilities.invokeLater(() -> {
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }

}
