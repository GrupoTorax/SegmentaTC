package gabaritos;

import java.util.HashMap;
import java.util.Map;
import org.paim.commons.BinaryImage;
import org.paim.commons.ExamSlice;
import org.paim.commons.ImageFactory;
import org.paim.examsio.ExamLoaderException;
import org.paim.orchestration.ExamResultSlice;
import org.paim.orchestration.StructureSlice;
import org.paim.orchestration.StructureType;

/**
 * Controller for the template editor panel
 */
public class TemplateEditorController {

    /** Template model */
    private final TemplateModel model;
    /** Template */
    private final Template template;
    /** Test model */
    private TestSlicesModel testModel;
    /** Current slice */
    private SliceTemplate currentSlice;
    /** Current exam result slice */
    private ExamResultSlice currentResultSlice;
    
    public TemplateEditorController(TemplateModel model, Template template) {
        super();
        this.model = model;
        this.template = template;
        try {
            testModel = TestSlicesModel.load();
        } catch(ExamLoaderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save changes
     */
    public void saveChanges() {
        String currentSliceFile = currentResultSlice.getExam().getSourceFile().getName();
        SliceTemplate slice = template.getOrCreateSlice(currentSliceFile);
        for (StructureType type : StructureType.values()) {
            StructureSlice struct = currentResultSlice.getStructures().get(type);
            if (struct != null) {
                slice.putStructure(type, struct.getBinaryLabel());
            }
        }
        TemplateModelDao.persist(model);
    }

    /**
     * Creates the slice table data
     * 
     * @return Object[][]
     */
    public Object[][] getSlicesTableData() {
        Object[][] data = new Object[testModel.getSlices().size()][1];
        for (int i = 0; i < data.length; i++) {
           data[i][0] = testModel.getSlices().get(i).getSourceFile().getName();
        }
        return data;
    }
    
    /**
     * Updates the current slice
     * 
     * @param index
     * @return ExamResultSlice
     */
    public ExamResultSlice updateCurrentSlice(int index) {
        ExamSlice slice = testModel.getSlices().get(index);
        String sliceName = slice.getSourceFile().getName();
        Map<StructureType, StructureSlice> map = new HashMap<>();
        for (StructureType type : StructureType.values()) {
            BinaryImage image = ImageFactory.buildBinaryImage(slice.getColumns(), slice.getRows());
            if (template.getSlices().containsKey(sliceName)) {
                currentSlice = template.getSlices().get(sliceName);
                BinaryImage theImage = currentSlice.getStructures().get(type);
                if (theImage != null) {
                    image = new BinaryImage(theImage);
                }
            }
            map.put(type, new StructureSlice(image));
        }
        currentResultSlice = new ExamResultSlice(map, slice);
        return currentResultSlice;
    }
    
    /**
     * Returns the template being edited
     * 
     * @return Template
     */
    public Template getTemplate() {
        return template;
    }
    
}
