package janela;

import javax.swing.JOptionPane;
import org.ronnau.fhirclient.Client;

/**
 *
 * Information export interface for HL7 FHIR local base
 */
public class ExportInterface {
    
    //private final static Client client = new Client();
       
    public void insertPatient() {
        JOptionPane.showMessageDialog(null, "Paciente", "Exportar", JOptionPane.INFORMATION_MESSAGE);  
        //client.insertPatient();
    }
    
    public void insertCalcification() {
        JOptionPane.showMessageDialog(null, "Calcificação", "Exportar", JOptionPane.INFORMATION_MESSAGE);  
        
        
        
    }
    
}
