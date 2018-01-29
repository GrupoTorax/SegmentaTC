package gabaritos;

import java.util.Arrays;
import java.util.List;
import org.paim.commons.Exam;
import org.paim.commons.ExamSlice;
import org.paim.examsio.ExamLoader;
import org.paim.examsio.ExamLoaderException;

/**
 * Model of available test slices
 */
public class TestSlicesModel {
    
    /** Exam test slices */
    private final List<ExamSlice> slices;

    /**
     * Creates a new test model
     * 
     * @param slices 
     */
    private TestSlicesModel(ExamSlice... slices) {
        this.slices = Arrays.asList(slices);
    }

    /**
     * Loads the test model
     * 
     * @return TestSlicesModel
     * @throws ExamLoaderException 
     */
    public static TestSlicesModel load() throws ExamLoaderException {
        Exam exam = ExamLoader.load(TestSlicesModel.class.getResource("/DICOM/").getFile());
        ExamSlice[] slices = new ExamSlice[exam.getNumberOfSlices()];
        for (int i = 0; i < slices.length; i++) {
            slices[i] = exam.getExamSlice(i);
        }
        return new TestSlicesModel(slices);
    }

    /**
     * Exam test slices
     * 
     * @return ExamSlices
     */
    public List<ExamSlice> getSlices() {
        return slices;
    }    
    
}
