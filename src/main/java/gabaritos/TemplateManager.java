package gabaritos;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Manages the templates
 */
public class TemplateManager extends JPanel {
   
    /** Template model */
    private final TemplateModel model;
    /** Template list */
    private JPanel panelTemplateList;

    /**
     * Creates the template manager
     */
    public TemplateManager() {
        super();
        model = TemplateModelDao.load();
        initGui();
    }

    /**
     * Initializes the interface
     */
    private void initGui() {
        setLayout(new BorderLayout());
        add(new JScrollPane(buildTemplateListPanel()));
    }
    
    private JComponent buildTemplateListPanel() {
        panelTemplateList = new JPanel();
        panelTemplateList.setLayout(new BoxLayout(panelTemplateList, BoxLayout.Y_AXIS));
        panelTemplateList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        updateTemplateList();
        return panelTemplateList;
    }

    /**
     * Updates the template list on the interface
     */
    private void updateTemplateList() {
        for (Template template : model.getTemplates()) {
            panelTemplateList.add(buildPanelTemplate(template));
            panelTemplateList.add(Box.createVerticalStrut(5));
        }
    }

    /**
     * Creates the template panel
     * 
     * @param template
     * @return JComponent
     */
    private JComponent buildPanelTemplate(Template template) {
        JPanel templatePanel = new JPanel(new GridBagLayout());
        templatePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        templatePanel.add(new JLabel(String.valueOf(template.getId())), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        templatePanel.add(new JLabel(template.getAuthor()), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        templatePanel.add(buildEditTemplateButton(template), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0;
        templatePanel.add(new JButton("X"), gbc);
        return templatePanel;
    }
    
    private JComponent buildEditTemplateButton(Template template) {
        JButton button = new JButton("E");
        button.addActionListener((e) -> {
            TemplateEditorPanel.showDialog(model, template);
        });
        return button;
    }

    /**
     * Shows the panel in a dialog
     */
    public static void showDialog() {
        JDialog dialog = new JDialog((Window)null, "Template manager");
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(new TemplateManager());
        SwingUtilities.invokeLater(() -> {
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }
    
}
