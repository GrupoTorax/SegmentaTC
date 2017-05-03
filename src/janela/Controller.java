package janela;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.torax.orchestration.Structure;

/**
 *
 * @author Rodrigo
 */
public class Controller {

    private File ultimoDiretorio = null;

    private JFileChooser selecaoDirArq = null;

    private final FileFilter filtroArquivosDcm = new FileNameExtensionFilter("Arquivo DICOM", "dcm");
    private final FileFilter filtroArquivosBmp = new FileNameExtensionFilter("Arquivo BMP", "bmp");

    private View janela;
    Model dados;

    public void iniciaAplicacao() {
        prepararSeletorDirArq();
        janela = new View(this);
        janela.exibe();
        load(new File(getClass().getResource("/diacon").getFile()));
    }

    public boolean temExameCarregado() {
        return dados != null;
    }

    void selecionarDiretorio() {
        selecaoDirArq.setDialogTitle("Selecione o diretório onde estão os arquivos DICOM");
        selecaoDirArq.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        selecionarDirArq();
        ultimoDiretorio = selecaoDirArq.getSelectedFile();
    }
    
    void selecionarArquivo() {
        selecaoDirArq.setDialogTitle("Selecione o arquivo DICOM");
        selecaoDirArq.setFileFilter(filtroArquivosDcm);
        selecaoDirArq.setFileSelectionMode(JFileChooser.FILES_ONLY);
        selecionarDirArq();
        ultimoDiretorio = new File(selecaoDirArq.getSelectedFile().getParent());
    }


    private void selecionarDirArq() {
        if (ultimoDiretorio != null) {
            selecaoDirArq.setCurrentDirectory(ultimoDiretorio);
        }
        if (selecaoDirArq.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            load(selecaoDirArq.getSelectedFile());
        }
    }

    private void load(File path) {
        final File itemSelecionado = path;
        try {
            dados = new Model(itemSelecionado.getAbsolutePath());
            janela.limitaSlider(dados.getNumeroFatias());
            janela.atualizaImagem();

        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void salvarFatia() {
        selecaoDirArq.setDialogTitle("Selecione o diretório onde será gravada a imagem da fatia");
        selecaoDirArq.setFileSelectionMode(JFileChooser.FILES_ONLY);
        selecaoDirArq.setCurrentDirectory(ultimoDiretorio);
        selecaoDirArq.setFileFilter(filtroArquivosBmp);
        selecaoDirArq.setSelectedFile(new File(getNomeArquivoBmp(janela.getValorSlider())));
        if (selecaoDirArq.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            final BufferedImage imagem = geraImagem();
            try {
                ImageIO.write(imagem, "bmp", selecaoDirArq.getSelectedFile());
                JOptionPane.showMessageDialog(null, "Arquivo '" + selecaoDirArq.getSelectedFile().getName() + "' salvo com sucesso.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao salvar a fatia.");
            }
        }
    }

    void salvarExame() {
        selecaoDirArq.setDialogTitle("Selecione o diretório onde serão gravadas as imagens do exame");
        selecaoDirArq.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (selecaoDirArq.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                for (int fatia = 0; fatia < dados.getNumeroFatias(); fatia++) {
                    final File fatiaExame = new File(selecaoDirArq.getSelectedFile(), getNomeArquivoBmp(fatia));
                    ImageIO.write(geraImagem(fatia), "bmp", fatiaExame);
                }
                JOptionPane.showMessageDialog(null, "Exame salvo com sucesso.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao salvar o exame.");
            }
        }
    }

    public void comparaExame() {
        String path = getClass().getResource("/gabarito").getFile();
        try {
            boolean sucesso = true;
            for (int fatia = 0; fatia < dados.getNumeroFatias(); fatia++) {
                StringBuilder sb = new StringBuilder("Fatia: ");
                sb.append(fatia);
                try {
                    String nome = path + "/" + getNomeArquivoBmp(fatia);
                    BufferedImage image = geraImagemComparacao(fatia);
                    BufferedImage gabarito = ImageIO.read(new File(nome));
                    for (int x = 0; x < image.getWidth(); x++) {
                        for (int y = 0; y < image.getHeight(); y++) {
                            if (image.getRGB(x, y) != gabarito.getRGB(x, y)) {
                                throw new Exception("Pixel: " + image.getRGB(x, y)
                                        + " Gabarito: " + gabarito.getRGB(x, y)
                                        + " x: " + x + " y:" + y);
                            }
                        }
                    }
                    sb.append(" sucesso!");
                } catch (Exception e) {
                    sb.append(" Erro! ").append(e.getMessage());
                    sucesso = false;
                }
                System.out.println(sb);
            }
            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Comparação realizada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Comparação realizada com inconsistências!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    BufferedImage geraImagem() {
        return geraImagem(janela.getValorSlider());
    }

    private BufferedImage geraImagem(final int fatia) {
        BufferedImage imagem = geraImagemDados(fatia);
//        if (janela.isSegPulmaoEsqMarcado()) {
//            pintaImagem(imagem, dados.getMatrizPulmaoEsq(fatia), Color.yellow);
//        }
//        if (janela.isSegPulmaoDirMarcado()) {
//            pintaImagem(imagem, dados.getMatrizPulmaoDir(fatia), Color.green);
//        }
        for (Structure structure : dados.getStructures()) {
            pintaImagem(imagem, structure.getSlice(fatia).getBinaryLabel(), structure.getType().getColor());
        }
        return imagem;
    }

    BufferedImage geraImagemComparacao(final int fatia) {
        BufferedImage imagem = geraImagemDados(fatia);
        for (Structure structure : dados.getStructures()) {
            pintaImagem(imagem, structure.getSlice(fatia).getBinaryLabel(), structure.getType().getColor());
        }
        return imagem;
    }

    BufferedImage geraImagemDados(final int fatia) {
        BufferedImage imagemOriginal = dados.getImagemFatia(fatia, janela.getWL(), janela.getWW());
        BufferedImage imagem = new BufferedImage(imagemOriginal.getWidth(), imagemOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Faz uma cópia da imagem original pixel a pixel pelo RGB
        for (int ix = 0; ix < imagem.getWidth(); ix++) {
            for (int iy = 0; iy < imagem.getHeight(); iy++) {
                int rgbOriginal = imagemOriginal.getRGB(ix, iy);
                imagem.setRGB(ix, iy, rgbOriginal);
            }
        }
        return imagem;
    }

    private void pintaImagem(BufferedImage imagem, boolean[][] matrizOrgao, Color cor) {
        for (int ix = 0; ix < matrizOrgao.length; ix++) {
            for (int iy = 0; iy < matrizOrgao[0].length; iy++) {
                if (matrizOrgao[ix][iy]) {
                    imagem.setRGB(ix, iy, cor.getRGB());
                }
            }
        }
    }

    private void prepararSeletorDirArq() {
        selecaoDirArq = new JFileChooser();
        selecaoDirArq.setAcceptAllFileFilterUsed(false);
    }

    private String getNomeArquivoBmp(final int fatia) {
        final String nomeArquivo = dados.getNomeArquivo(fatia);
        return nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")) + "_" + (fatia + 1) + ".bmp";
    }

}
