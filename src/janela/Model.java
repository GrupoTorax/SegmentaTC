package janela;

import java.awt.image.BufferedImage;
import org.torax.commons.Exam;
import org.torax.examsio.ExamLoader;
import org.torax.orchestration.Orchestration;
import org.torax.orchestration.ExamResult;

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

    boolean[][] getMatrizPulmaoEsq(int indice) {
        return exameSegmentado.getFatiaExameSegmentado(indice).getPulmaoEsq();
    }

    int getTamanhoPulmaoEsq(int indice) {
        return exameSegmentado.getFatiaExameSegmentado(indice).getTamanhoPulmaoEsq();
    }

    boolean[][] getMatrizPulmaoDir(int indice) {
        return exameSegmentado.getFatiaExameSegmentado(indice).getPulmaoDir();
    }

    int getTamanhoPulmaoDir(int indice) {
        return exameSegmentado.getFatiaExameSegmentado(indice).getTamanhoPulmaoDir();
    }

    int[][] getMatrizOriginal(int indice) {
        return exam.getExamSlice(indice).getCoefficientMatrix();
    }

    float getEspessuraFatia(int indice) {
        return exam.getExamSlice(indice).getSliceThickness();
    }

}
