package processamento;

import dados.ExameSegmentado;
import org.torax.commons.Exam;

/**
 *
 * @author Rodrigo
 */
public class SegmentaEstruturas {
    private final Exam exam;

    public SegmentaEstruturas(Exam exam) {
        this.exam = exam;
    }

    public ExameSegmentado segmenta() {        
        ExameSegmentado exameSegmentado = new ExameSegmentado(exam);

        SegmentaPulmoes segPul = new SegmentaPulmoes(exam, exameSegmentado);
        segPul.segmenta();
        
        return exameSegmentado;
        //executa todos os processos de segmentação
        
      //  new VisualizaImagem(exame.getFatia(30).getMatrizPulmoes());
        
    }
    
}
