package janela;

import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JOptionPane;
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
       
    public void fazConferencia() {
        System.out.println("Conferência de laudos iniciada...");

        String laudo = JOptionPane.showInputDialog("Laudo a ser conferido: ");
        
        int calc_pdi = client.readCalcioPDI(laudo);
        int calc_pln = client.readCalcioPLN(laudo);
               
        String aviso;       
        if (calc_pdi == calc_pln){
            if (calc_pdi == 0) {
                aviso = "OK - Não foram citados pontos de calcificação no laudo e não foram identificados pontos de calcificação nas imagens.";
            } else {
                aviso = "OK - Foram citados pontos de calcificação no laudo e foram identificados pontos de calcificação nas imagens.";
            }
        } else {
            if (calc_pdi == 0) {
                aviso = "REVISAR - Foram citados pontos de calcificação no laudo e não foram identificados pontos de calcificação nas imagens.";
            } else {
                if (calc_pln == 0) {
                    aviso = "REVISAR - Não foram citados pontos de calcificação no laudo e foram identificados pontos de calcificação nas imagens.";
                } else {
                    aviso = "REVISAR - A quantidade de pontos de calcificação citados no laudo é diferente da quantidade de pontos encontrada nas imagens.";
                }
            }
        }
       
        String msg = aviso + "\n\nLaudo: " + laudo + "\n" + client.readPatient(client.readPatientDR(laudo));
        
        JOptionPane.showMessageDialog(null, msg);
        
        System.out.println();
        System.out.println("Calcio PDI: " + calc_pdi);
        System.out.println("Calcio PLN: " + calc_pln);        
        
        
        //System.out.println(client.readPatient("1"));
        
        //System.out.println(client.readDiagnosticReport("4957"));
        
        System.out.println("Conferência de laudos finalizada...");
    }
    
    public void insertPatient() {
        System.out.println("Inserção de pacientes iniciada...");
        client.insertPatient(1);
        client.insertPatient(2);
        client.insertPatient(3);
        client.insertPatient(4);
        client.insertPatient(5);
        client.insertPatient(6);        
        System.out.println("Inserção de pacientes finalizada...");
    }
    
    public void insertReport(ExamResult exame, int WL, int WW) {
        System.out.println("Inserção de laudo iniciada...");
        int paciente = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        int calc_pdi = defineCalcPDI(exame, WL, WW);
        int calc_pln = defineCalcPLN(paciente);
        client.insertDiagnosticReport(Integer.toString(paciente), calc_pdi, calc_pln);
        System.out.println("Inserção de laudo finalizada...");       
    }
    
    
    private int defineCalcPDI(ExamResult exame, int WL, int WW){
        
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
        // TODO: Sumiu o método:
//        System.out.println("Maior label: " + binaryLabelingProcess.getLastLabel());
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
        
        return qtd;
    }
    
    private int defineCalcPLN(int paciente){
        return 1;
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
