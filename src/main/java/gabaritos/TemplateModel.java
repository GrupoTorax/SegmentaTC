package gabaritos;

import java.util.ArrayList;
import java.util.List;

/**
 * Template model
 */
public class TemplateModel {
    
    /** Templates */
    private List<Template> templates;

    /**
     * Creates the template model
     */
    public TemplateModel() {
        this.templates = new ArrayList<>();
    }

    /**
     * Adds a template in the templae list
     */
    public void addTemplate(Template template) {
        templates.add(template);
    }
    
    /**
     * Gets the template list
     * 
     * @return List
     */
    public List<Template> getTemplates() {
        return templates;
    }
    
}
