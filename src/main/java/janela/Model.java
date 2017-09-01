package janela;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.paim.commons.Exam;
import org.paim.examsio.ExamLoader;
import org.paim.orchestration.ExamResult;
import org.paim.orchestration.Orchestration;
import org.paim.orchestration.Structure;

/**
 *
 * @author Rodrigo
 */
public class Model {

    private final Exam exam;
    private final ExamResult exameSegmentado;

    public Model(String localizacao) throws Exception {

        exam = ExamLoader.load(localizacao);
        Orchestration segEst = new Orchestration(exam);
        exameSegmentado = segEst.segmenta();
        
    }

    public String getNomeArquivo(final int fatia) {
        return exam.getExamSlice(fatia).getSourceFile().getName();
    }

    BufferedImage getImagemFatia(int indice, int WL, int WW) {
        return exam.getExamSlice(indice).getBufferedImageWithWLWW(WL, WW);
    }

    int getNumeroFatias() {
        return exam.getNumberOfSlices();
    }

    /**
     * Return the structures
     * 
     * @return {@code List<Structure>}
     */
    public List<Structure> getStructures() {
        return new ArrayList<>(exameSegmentado.getStructures().values());
    }
    
    /**
     * Returns the exam result
     * 
     * @return ExamResult
     */
    public ExamResult getExamResult() {
        return exameSegmentado;
    }
    
    int[][] getMatrizOriginal(int indice) {
        return exam.getExamSlice(indice).getCoefficientMatrix();
    }

    float getEspessuraFatia(int indice) {
        return exam.getExamSlice(indice).getSliceThickness();
    }

}
