package dados;

import org.torax.commons.Exam;

/**
 *
 * @author Anderson
 */
public class ExameSegmentado {

    private final FatiaExameSegmentado[] fatiaExameSegmentados;

    public ExameSegmentado(Exam exam) {
        int tamanho = exam.getNumberOfSlices();
        fatiaExameSegmentados = new FatiaExameSegmentado[tamanho];
        for (int i = 0; i < tamanho; i++) {
            fatiaExameSegmentados[i] = new FatiaExameSegmentado();
        }
    }
    
    public FatiaExameSegmentado getFatiaExameSegmentado(int indice) {
        return fatiaExameSegmentados[indice];
    }
}
