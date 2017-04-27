package testes;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import org.torax.commons.Exam;

/**
 *
 * @author Rodrigo
 */
public class VisualizaImagem {

    public VisualizaImagem(int[][] matrizImagem) {
        BufferedImage imagem = new BufferedImage(matrizImagem.length, matrizImagem[0].length, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < imagem.getWidth(); x++) {
            for (int y = 0; y < imagem.getHeight(); y++) {
                imagem.setRGB(x, y, getPixelValue(matrizImagem[x][y]));
            }
        }
        exibeImagem(imagem);
    }

    public VisualizaImagem(int indice, Exam exam, int[][] matrizImagem, int WL, int WW) {
        exibeImagem(exam.getExamSlice(indice).getBufferedImageWithWLWW(WL, WW));
    }

    private int getPixelValue(int pixel) {
        return (int) pixel << 16 | (int) pixel << 8 | (int) pixel;
    }
    
    private void exibeImagem(BufferedImage imagem) {
        JDialog janela = new JDialog();
        JLabel label = new JLabel(new ImageIcon(imagem));
        label.setBounds(10, 10, imagem.getWidth(), imagem.getHeight());
        janela.setSize(imagem.getWidth() + 35, imagem.getHeight() + 60);
        janela.setLocationRelativeTo(null);
        janela.getContentPane().setLayout(null);
        janela.getContentPane().add(label);
        janela.setModal(true);
        janela.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        janela.setVisible(true);
    }

}
