package gabaritos;

import com.google.gson.GsonBuilder;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Dao layer for the template model
 */
public class TemplateModelDao {
    
    /**
     * Loads the template model
     * 
     * @return Template model
     */
    public static TemplateModel load() {
        Reader reader = new InputStreamReader(TemplateModelDao.class.getResourceAsStream("/gabarito/templates.json"));
        return new GsonBuilder().create().fromJson(reader, TemplateModel.class);
    }
    
}
