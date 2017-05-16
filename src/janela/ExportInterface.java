package janela;

import org.ronnau.fhirclient.Client;
import org.torax.commons.*;
import org.torax.orchestration.*;
import org.torax.pdi.*;
import testes.VisualizaImagem;

/**
 *
 * Information export interface for HL7 FHIR local base
 */
public class ExportInterface {
    
    private final static Client client = new Client();
       
    public void insertPatient() {
        /*
        client.insertPatient(1);
        client.insertPatient(2);
        client.insertPatient(3);
        client.insertPatient(4);
        client.insertPatient(5);
        */
        
        //System.out.println(client.readPatient("1"));
        
        client.insertDiagnosticReport("5", 10, 20);
                
        //System.out.println(client.readDiagnosticReport("4957"));
        
        System.out.println("feito!");
    }
    
    public void insertCalcification(ExamResult exame, int WL, int WW) {
        
        ExamResultSlice slice = exame.getSlice(0);
        boolean[][] pericardio = slice.getStructure(StructureType.HEART).getBinaryLabel();
        int[][] matrizHU = slice.getExam().getCoefficientMatrix();
        
        int menor = 0;
        int maior = 0;
        int quantidade = 0;
        
        // pontos com cálcio são todos aqueles entre 130 e 400 HU, somente dentro da área do pericardio
        int[][] trabalhoBin = copyArray(matrizHU);
        for (int x = 0; x < matrizHU.length; x++) {
            for (int y = 0; y < matrizHU[0].length; y++) {
                if (trabalhoBin[x][y] < menor){
                    menor = trabalhoBin[x][y];
                }
                if (trabalhoBin[x][y] > maior){
                    maior = trabalhoBin[x][y];
                }
                
                //if ((trabalhoBin[x][y] < 130) || (trabalhoBin[x][y] > 400) || !pericardio[x][y]) {
                if ((trabalhoBin[x][y] < 199) || !pericardio[x][y]) {
                    trabalhoBin[x][y] = 0;
                }else{
                    trabalhoBin[x][y] = 1;
                    quantidade++;
                }
            }
        }
        
        // Minimum Region of Calcification - 0.1% de área de calcificação em relação a área total do pericárdio
        int mrc = (int) (slice.getStructure(StructureType.HEART).getArea() * 0.001); 
        
        System.out.println("Menor: " + menor);
        System.out.println("Maior: " + maior);
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Área pericárdio: " + slice.getStructure(StructureType.HEART).getArea());        
        System.out.println("MRC: " + mrc);        
        
        //define os pontos de calcificação agrupando os interligados
        Image image = ImageHelper.create(trabalhoBin, new org.torax.commons.Range<>(-4000, 4000));
        BinaryLabelingProcess binaryLabelingProcess = new BinaryLabelingProcess(image);
        binaryLabelingProcess.process();
        System.out.println("Maior label: " + binaryLabelingProcess.getLastLabel());
        int qtd = 0, area = 0;
        for (int i = 0; i < 1000; i++) {
            if (binaryLabelingProcess.getSize(i) > mrc) {
                System.out.println("Label: " + i + " Tamanho: " + binaryLabelingProcess.getSize(i) + " u.a");  
                qtd++;
                area+= binaryLabelingProcess.getSize(i);
                boolean[][] mtz = binaryLabelingProcess.getMatrix(i);
                for (int ix = 0; ix < mtz.length; ix++) {
                    for (int iy = 0; iy < mtz[0].length; iy++) {
                        if (mtz[ix][iy]) {
                            trabalhoBin[ix][iy] = 255;
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println("Qtd total: " + qtd + " Tamanho total: " + area + " u.a");        
        System.out.println();
        new VisualizaImagem(trabalhoBin);
        
    }
    
    public static int[][] copyArray(final int[][] array) {
        final int[][] copy = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = new int[array[i].length];
            System.arraycopy(array[i], 0, copy[i], 0, array[i].length);
        }
        return copy;
    }
}
