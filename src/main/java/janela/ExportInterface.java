package janela;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import org.paim.commons.BinaryImage;
import org.paim.commons.Image;
import org.paim.commons.ImageHelper;
import org.paim.orchestration.ExamResult;
import org.paim.orchestration.ExamResultSlice;
import org.paim.orchestration.StructureType;
import org.paim.pdi.BinaryLabelingProcess;
import org.ronnau.fhirclient.Client;
import testes.VisualizaImagem;

/**
 *
 * Information export interface for HL7 FHIR local base
 */
public class ExportInterface {
    
    private final static Client client = new Client();
       
    public void fazConferencia() {

        System.out.println("ATENÇÃO: o recurso de conferência foi desabilitado para evitar modificar o repositório!");        
        /*
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
        */
    }
    
    public void geraPreLaudo() throws FileNotFoundException, IOException {

        System.out.println("Geração de laudo preliminar iniciada...");

        String paciente = JOptionPane.showInputDialog("Paciente: ");

        PrintWriter pw = new PrintWriter(System.getProperty("user.home") + "/Desktop/Laudo_" + paciente + ".txt");      
        
        pw.println("Laudo parcial\n");
        pw.println();
        pw.println(client.readPatient(paciente));
        pw.println();
        pw.println("Observações:");
        
        String DRs[] = client.readDRs(paciente);
       
        for (int i = 0; i < 5; i++) {
            if (DRs[i] == null) {
                break;
            }
            
            int calcPDI = client.readCalcioPDI(DRs[i]);
            if (calcPDI != 999) {
                if (calcPDI == 0) {
                    pw.println("Não foram detectados pontos de calcificação na região cardíaca.");
                } else {
                    pw.println("Foram detectados pontos de calcificação na região cardíaca.");
                }
            }
            
            double temp = client.readObservationTemp(DRs[i]);
            if (temp != 999) {
                if (temp <= 37.5) {
                pw.println("Temperatura normal (" + temp + " ºC).");
                } else {
                pw.println("Febre (" + temp + " ºC).");                    
                }
            }
            
            String pres = client.readObservationPress(DRs[i]);
            if (!pres.equals("999")) {
                String[] valores = pres.split("/");
                int systolic = Integer.parseInt(valores[0]);
                int diastolic = Integer.parseInt(valores[1]);
                if (systolic > 180 || diastolic > 110) {
                    pw.println("Crise hipertensiva (" + pres + " mmHg).");
                } else {
                    if (systolic >= 160 || diastolic >= 100) {
                        pw.println("Hipertensão estágio 2 (" + pres + " mmHg).");
                    } else {
                        if (systolic >= 140 || diastolic >= 90) {
                            pw.println("Hipertensão estágio 1 (" + pres + " mmHg).");                    
                        } else {
                            if (systolic >= 120 || diastolic >= 80) {
                                pw.println("Pré-hipertensão (" + pres + " mmHg).");                    
                            } else {
                                pw.println("Pressão arterial normal (" + pres + " mmHg).");
                            }
                        }
                    }
                }                
            }
            
            int freq = client.readObservationFreq(DRs[i]);
            if (freq != 999) {
                pw.println("Frequência cardíaca: " + freq + " bpm.");
            }            
        }

        pw.flush();
        pw.close();
       
        System.out.println("\n\nGeração de laudo preliminar finalizada...");
        
    }        
    
    public void insertPatient() {
        System.out.println("ATENÇÃO: nenhum paciente será inserido pois o recurso foi desabilitado para evitar modificar o repositório!");        
        /*
        System.out.println("Inserção de pacientes iniciada...");
        client.insertPatient(1);
        client.insertPatient(2);
        client.insertPatient(3);
        client.insertPatient(4);
        client.insertPatient(5);
        System.out.println("Inserção de pacientes finalizada...");
        */
    }
    
    public void insertReportProt1(ExamResult exame, int WL, int WW) {
        System.out.println("ATENÇÃO: não será inserido o DiagnosticReport do protótipo 1 pois o recurso foi desabilitado para evitar modificar o repositório!");
        /*
        System.out.println("Inserção de laudo pr 1 iniciada...");
        //int paciente = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        //int calc_pdi = defineCalcPDI(exame, WL, WW);
        //int calc_pln = defineCalcPLN(paciente);
        
        int paciente = 35054;
        int calc_pdi = 0;
        int calc_pln = 0;
        
        client.insertDiagnosticReport(Integer.toString(paciente), calc_pdi, calc_pln);
        System.out.println("Inserção de laudo finalizada...");       
        */
    }
    
    public void insertReportProt2() {
        System.out.println("ATENÇÃO: não será inserido o DiagnosticReport do protótipo 2 pois o recurso foi desabilitado para evitar modificar o repositório!");
        /*
        System.out.println("Inserção de laudo pr 2 iniciada...");

        //int paciente = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        //client.insertDiagnosticReport(Integer.toString(paciente), 10, 20, 30);              
        
        client.insertDiagnosticReport("30054", 36.4, "166/121", 87);       
        client.insertDiagnosticReport("35052", 37.1, "145/85", 71);       
        client.insertDiagnosticReport("30055", 36.8, "119/80", 57);       
               
        System.out.println("Inserção de laudo finalizada...");
        */
    }      
        
    private int defineCalcPDI(ExamResult exame, int WL, int WW){
        
        ExamResultSlice slice = exame.getSlice(0);
        BinaryImage pericardio = slice.getStructure(StructureType.HEART).getBinaryLabel();
        int[][] matrizHU = slice.getExam().getCoefficientMatrix();
        
        int menor = 0;
        int maior = 0;
        int quantidade = 0;
        
        // pontos com cálcio são todos aqueles acima de 130 HU, somente dentro da área do pericardio
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
                if ((trabalhoBin[x][y] < 130) || !pericardio.get(x, y)) {
                    trabalhoBin[x][y] = 0;
                }else{
                    trabalhoBin[x][y] = 1;
                    quantidade++;
                }
            }
        }
        
        // Minimum Region of Calcification - 0.1% de área de calcificação em relação a área total do pericárdio
        int mrc = (int) (slice.getStructure(StructureType.HEART).getArea() * 0.001); 
        mrc = 2;
        
        System.out.println("Menor: " + menor);
        System.out.println("Maior: " + maior);
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Área pericárdio: " + slice.getStructure(StructureType.HEART).getArea());        
        System.out.println("MRC: " + mrc);        
        
        //define os pontos de calcificação agrupando os interligados
        Image image = ImageHelper.create(trabalhoBin, new org.paim.commons.Range<>(-4000, 4000));
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
        
        return qtd;
    }
    
    private int defineCalcPLN(int paciente){
        
        String exame = new StringBuilder().append("D:\\Prototypes\\Laudos_PLN\\laudo").append(paciente).append(".xml").toString();

        System.out.println("Exame: " + exame);            
        
        // - Todas as frases que não contém o termo “calci” são desconsideradas
        // - Nas frases restantes, os substantivos são verificados, buscando validar se estão relacionados às regiões anatômicas que devem ser consideradas. Essa ação é realizada através do léxico UMLS Metathesaurus Browser, onde são retornados termos equivalentes ao avaliado, reduzindo assim a lista de regiões anatômicas de interesse que a aplicação precisa manter
        // - A última etapa consiste em verificar se a informação não está sendo negada. Para isso, são executadas buscas por termos que indicam essa condição nas frases restantes, como, por exemplo, “sem”, “não”, “ausência”, “ausente”, entre outros. Se o trecho não estiver sendo negado, a referência à presença de calcificação é considerada válida e essa informação é inserida no repositório de características

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
