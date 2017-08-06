package janela;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import org.torax.orchestration.StructureType;

/**
 *
 * @author Rodrigo
 */
public class View {

    private static final String LITERAL_OSSOS = "Ossos";
    private static final String LITERAL_MEDIASTINO = "Mediastino";
    private static final String LITERAL_PULMOES = "Pulmões";
    private static final String LITERAL_CUSTOMIZADO = "Customizado";

    private final Controller controller;
    
    private JFrame janela;
    private JMenuBar menu;
    private JPanel painelInfo;
    private JPanel painelImagem;
    private JPanel painelCentral;
    private JPanel painelRodape;
    private JPanel painelPrincipal;

    private JFormattedTextField windowLevel;
    private JFormattedTextField windowWidth;
    private JComboBox templateWLWW;
    private ExamSliceView sliceCorrente;
    private JSlider slider;

    private JLabel labelNomeArquivo;
    private JLabel labelIndiceImagem;
    private JLabel espessuraFatia;
    private JLabel labelCoordenadas;
    private JLabel labelHU;
    private JLabel labelRGB;

    /** Selected structures */
    private final Set<StructureType> selected;
    
    private ExportInterface expInt;
    
    public View(Controller controller) {
        this.controller = controller;
        selected = new HashSet<>(Arrays.asList(StructureType.values()));
    }

    public void exibe() {
        janela = new JFrame();
        janela.setTitle("Ferramenta para apoio ao diagnóstico em imagens de TC do tórax");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setBounds(1, 1, 774, 665);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());

        criaMenus();

        //painel da imagem, fica dentro do painel central
        painelImagem = new JPanel(new BorderLayout());
        //painel central, contem o painel com a imagem e com as informacoes
        painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelImagem, BorderLayout.WEST);
        //painel do rodape, contem os campos do rodape da janela
        JPanel bottomPanel = new JPanel(new BorderLayout());
        painelRodape = new JPanel();
        painelRodape.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        bottomPanel.add(painelRodape);
        bottomPanel.add(buildPanelStructureSelection(), BorderLayout.EAST);
        //painel principal, contem todos outros paineis (com excecao do painel do frame
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(bottomPanel, BorderLayout.SOUTH);
        //adiciona o painel principal no painel do frame
        janela.getContentPane().add(painelPrincipal, BorderLayout.CENTER);

        criaRodape();

        slider = new JSlider(JSlider.VERTICAL, 0, 10, 0);
        slider.setInverted(true);
        slider.setToolTipText("Seleciona a fatia do exame que será utilizada");
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.addChangeListener((ChangeEvent e) -> {
            atualizaImagem();
        });
        janela.getContentPane().add(slider, BorderLayout.WEST);

        sliceCorrente = new ExamSliceView(null, (type) -> selected.contains(type), getWL(), getWW());

        sliceCorrente.setPreferredSize(new Dimension(512, 512));
        sliceCorrente.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                atualizaCoordenadas(e.getX(), e.getY());
            }
        });
        painelImagem.add(sliceCorrente);

        criaInformacoes();

        janela.pack();
        janela.setVisible(true);
    }

    private void criaMenus() {
        menu = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        menu.add(menuArquivo);
        JMenuItem submenuSelecionarDiretorio = new JMenuItem("Selecionar diretório");
        submenuSelecionarDiretorio.addActionListener((ActionEvent e) -> {
            controller.selecionarDiretorio();
        });
        menuArquivo.add(submenuSelecionarDiretorio);
        JMenuItem submenuSelecionarArquivo = new JMenuItem("Selecionar arquivo");
        submenuSelecionarArquivo.addActionListener((ActionEvent e) -> {
            controller.selecionarArquivo();
        });
        menuArquivo.add(submenuSelecionarArquivo);
        JMenuItem submenuSalvarF = new JMenuItem("Salvar fatia");
        submenuSalvarF.addActionListener((ActionEvent e) -> {
            if (!controller.temExameCarregado()) {
                JOptionPane.showMessageDialog(null, "Não há nenhum exame carregado.");
                return;
            }
            controller.salvarFatia();
        });
        menuArquivo.add(submenuSalvarF);
        JMenuItem submenuSalvarE = new JMenuItem("Salvar exame");
        submenuSalvarE.addActionListener((ActionEvent e) -> {
            if (!controller.temExameCarregado()) {
                JOptionPane.showMessageDialog(null, "Não há nenhum exame carregado.");
                return;
            }
            controller.salvarExame();
        });
        menuArquivo.add(submenuSalvarE);
        JMenuItem comparaExame = new JMenuItem("Comparar exame");
        comparaExame.addActionListener((ActionEvent e) -> {
            if (!controller.temExameCarregado()) {
                JOptionPane.showMessageDialog(null, "Não há nenhum exame carregado.");
                return;
            }
            controller.comparaExame();
        });
        menuArquivo.add(comparaExame);
        JMenuItem submenuSair = new JMenuItem("Sair");
        submenuSair.addActionListener((ActionEvent e) -> {
            janela.dispose();
        });
        menuArquivo.add(submenuSair);
        
        JMenu menuEditar = new JMenu("Editar");
        menu.add(menuEditar);
        JMenuItem submenuEditarEstruturas = new JMenuItem("Editar estruturas");
        submenuEditarEstruturas.addActionListener((ActionEvent e) -> {
            new StructureDrawPanel(controller.dados.getExamResult().getSlice(getValorSlider()), getWL(), getWW()).showDialog();
            atualizaImagem();
        });
        menuEditar.add(submenuEditarEstruturas);  
        JMenu menuExportar = new JMenu("Exportar");
        menu.add(menuExportar);
        JMenuItem submenuExportarPaciente = new JMenuItem("Paciente");
        submenuExportarPaciente.addActionListener((ActionEvent e) -> {   
            if(expInt == null) {
                expInt = new ExportInterface();
            }
            expInt.insertPatient();   
        });
        menuExportar.add(submenuExportarPaciente);
        JMenuItem submenuExportarLaudo = new JMenuItem("Laudo");
        submenuExportarLaudo.addActionListener((ActionEvent e) -> {     
            if(expInt == null) {
                expInt = new ExportInterface();
            }
            expInt.insertReport(controller.dados.getExamResult(), getWL(), getWW());
        });
        menuExportar.add(submenuExportarLaudo);
        JMenuItem submenuExportarConferencia = new JMenuItem("Conferência");
        submenuExportarConferencia.addActionListener((ActionEvent e) -> {     
            if(expInt == null) {
                expInt = new ExportInterface();
            }
            expInt.fazConferencia();
        });
        menuExportar.add(submenuExportarConferencia);
        JMenu menuSobre = new JMenu("Sobre");
        menu.add(menuSobre);
        JMenuItem submenuSobre = new JMenuItem("Sobre");
        submenuSobre.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(null, "Universidade Feevale\n\n"
                    + "Ciência da Computação\n\n"
                    + "Trabalho de Conclusão de Curso\n\n"
                    + "Aluno: Rodrigo Freiberger Rönnau\n\n"
                    + "Orientadora: Marta Rosecler Bez\n\n"
                    + "Coorientador: Jéferson Cristiano Flores", "Sobre", JOptionPane.INFORMATION_MESSAGE);
        });
        menuSobre.add(submenuSobre);
        janela.setJMenuBar(menu);
    }

    private void criaRodape() {
        painelRodape.setLayout(new GridLayout(0, 2));

        painelRodape.add(new JLabel("Nível de janela (WL)"));
        windowLevel = new JFormattedTextField();
        windowLevel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                atualizaImagem();
            }
        });
        painelRodape.add(windowLevel);

        ActionListener listenerCheckBoxes = (ActionEvent e) -> {
            atualizaImagem();
        };

        painelRodape.add(new JLabel("Largura de janela (WW)"));
        windowWidth = new JFormattedTextField();
        windowWidth.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                atualizaImagem();
            }
        });
        painelRodape.add(windowWidth);

        painelRodape.add(new JLabel("Templates"));
        templateWLWW = new JComboBox();
        templateWLWW.addItem(LITERAL_OSSOS);
        templateWLWW.addItem(LITERAL_MEDIASTINO);
        templateWLWW.addItem(LITERAL_PULMOES);
        templateWLWW.addItem(LITERAL_CUSTOMIZADO);
        templateWLWW.setSelectedItem(LITERAL_PULMOES);
        trataTrocaTemplateWLWW();
        templateWLWW.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                trataTrocaTemplateWLWW();
                atualizaImagem();
            }
        });
        painelRodape.add(templateWLWW);

    }
    
    /**
     * Builds the panel for selecting visible structures
     * 
     * @return JComponent
     */
    private JComponent buildPanelStructureSelection() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (StructureType value : StructureType.values()) {
            panel.add(buildCheckStructureSelection(value));
        }
        return new JScrollPane(panel);
    }

    /**
     * Builds the check-box for showing a structure type
     * 
     * @param type
     * @return JComponent
     */
    private JComponent buildCheckStructureSelection(StructureType type) {
        JCheckBox check = new JCheckBox(type.toString(), selected.contains(type));
        check.addChangeListener((evt) -> {
            if (check.isSelected() && !selected.contains(type)) {
                selected.add(type);
                sliceCorrente.repaint();
            }
            if (!check.isSelected() && selected.contains(type)) {
                selected.remove(type);
                sliceCorrente.repaint();
            }
        });
        return check;
    }
    
    private void criaInformacoes() {
        JScrollPane sPainelInfo = new JScrollPane();
        painelCentral.add(sPainelInfo, BorderLayout.EAST);

        painelInfo = new JPanel(new GridLayout(0, 1));
        sPainelInfo.setViewportView(painelInfo);

        painelInfo.add(new JLabel("                 INFORMAÇÕES                 "));

        labelNomeArquivo = new JLabel();
        painelInfo.add(labelNomeArquivo);

        labelIndiceImagem = new JLabel();
        painelInfo.add(labelIndiceImagem);

        espessuraFatia = new JLabel();
        painelInfo.add(espessuraFatia);

        labelCoordenadas = new JLabel();
        painelInfo.add(labelCoordenadas);

        labelHU = new JLabel();
        painelInfo.add(labelHU);

        labelRGB = new JLabel();
        painelInfo.add(labelRGB);

    }

    private void atualizaCoordenadas(final int x, final int y) {
        //se não tem exame carregado
        if (!controller.temExameCarregado()) {
            labelCoordenadas.setText("");
            labelHU.setText("");
            labelRGB.setText("");
            return;
        }

        //atualiza as infromações do pixel selecionado
        labelCoordenadas.setText("  Coordenadas: " + x + ", " + y);
        //MELHORAR ESTE PONTO, A FORMA COMO OBTEM OS DADOS!
        labelHU.setText("  Valor em HU: " + controller.dados.getMatrizOriginal(getValorSlider())[x][y]);

//        BufferedImage imagem = new BufferedImage(sliceCorrente.getIcon().getIconWidth(),
//                sliceCorrente.getIcon().getIconHeight(),
//                BufferedImage.TYPE_INT_RGB);
//
//        sliceCorrente.getIcon().paintIcon(null, imagem.getGraphics(), 0, 0);

//        Color c = new Color(imagem.getRGB(x, y));

//        labelRGB.setText("  Valor RGB: " + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());

    }

    private void trataTrocaTemplateWLWW() {
        String opcaoSelecionada = (String) templateWLWW.getSelectedItem();
        switch (opcaoSelecionada) {
            case LITERAL_OSSOS:
                windowLevel.setText("300");
                windowWidth.setText("1500");
                break;
            case LITERAL_MEDIASTINO:
                windowLevel.setText("40");
                windowWidth.setText("400");
                break;
            case LITERAL_PULMOES:
                windowLevel.setText("-400");
                windowWidth.setText("1500");
                break;
        }
    }

    void atualizaImagem() {
        sliceCorrente.setSlice(controller.dados.getExamResult().getSlice(getValorSlider()));
        sliceCorrente.setWl(getWL());
        sliceCorrente.setWw(getWW());

        labelNomeArquivo.setText("  Arquivo: " + controller.dados.getNomeArquivo(getValorSlider()));
        labelIndiceImagem.setText("  Fatia: " + (getValorSlider() + 1) + " / " + controller.dados.getNumeroFatias());
        espessuraFatia.setText("  Espessura: " + controller.dados.getEspessuraFatia(getValorSlider()) + " mm");

    }

    int getValorSlider() {
        return slider.getValue();
    }

    int getWW() {
        return Integer.parseInt(windowWidth.getText());
    }

    int getWL() {
        return Integer.parseInt(windowLevel.getText());
    }

    void limitaSlider(int numeroFatias) {
        slider.setMaximum(numeroFatias - 1);
    }

}
