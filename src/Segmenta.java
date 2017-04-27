import janela.Controller;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Rodrigo
 */
public class Segmenta {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        defineLookAndFeel();
        Controller janela = new Controller();
        janela.iniciaAplicacao();
    }

    private static void defineLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Segmenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
