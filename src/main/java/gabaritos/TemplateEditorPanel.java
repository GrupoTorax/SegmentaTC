package gabaritos;

import janela.StructureDrawPanel;
import java.awt.BorderLayout;
import java.awt.Window;
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
import org.paim.orchestration.ExamResultSlice;

/**
 * Template editor panel
 */
public class TemplateEditorPanel extends JPanel {

    /** Controller */
    private final TemplateEditorController controller;
    /** Container for the StructureDrawPanel */
    private JPanel panelStructureDraw;
    
    public TemplateEditorPanel(TemplateModel model, Template template) {
        super();
        this.controller = new TemplateEditorController(model, template);
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
        JTable table = new JTable(controller.getSlicesTableData(), new Object[] {"Exam"});
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
            ExamResultSlice slice = controller.updateCurrentSlice(index);
            panelStructureDraw.add(new StructureDrawPanel(slice, -400, 1500));
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
            controller.saveChanges();
        });
        return button;
    }
    
    /**
     * Shows the panel in a dialog
     * 
     * @param model
     * @param template
     */
    public static void showDialog(TemplateModel model, Template template) {
        JDialog dialog = new JDialog((Window)null, "Template edit");
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(new TemplateEditorPanel(model, template));
        SwingUtilities.invokeLater(() -> {
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }

}
