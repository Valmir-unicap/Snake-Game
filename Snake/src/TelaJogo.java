import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TelaJogo extends JPanel implements Action {
    private static final int LARGURA_TELA= 1300;
    private static final int ALTURA_TELA= 750;
    private static final int TAMANHO_BLOCO= 50;
    private static final int UNIDADES = LARGURA_TELA * ALTURA_TELA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    private static final int INTERVALO=150;
    private static final String NOME_FONTE= "Ink Free";
    private final int[] eixoX= new int [UNIDADES];
    private final int[] eixoY= new int [UNIDADES];
    private int corpoCobra = 6;
    private int blocoComidos;
    private int blocoX;
    private int blocoY;
    private char direcao = 'D';// C - Cima, B - Baixo, E - Esquerda, D - Direita
    private boolean estaRodando = false;
    Timer timer;
    Random random;

    TelaJogo (){
        random = new Random();
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setBackground(Color.BLACK);//cor de fundo da tela
        setFocusable(true);
        addKeyListener(new LeitorDeTeclasAdapter());
        iniciarJogo();
    }

    public void iniciarJogo(){
        criarBloco();
        estaRodando = true;
        timer = new Timer(INTERVALO, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        desenharTela(g);
    }

    public void desenharTela(Graphics g){
        if(estaRodando){
            g.setColor(Color.BLUE);//cor da maçã
            g.fillOval(blocoX, blocoY, TAMANHO_BLOCO, TAMANHO_BLOCO);
            int i;
            for(i=0;i< corpoCobra; i=i+1){
                if(i == 0){
                    g.setColor(Color.GRAY);
                    g.fillRect(eixoX[i], eixoY[i], TAMANHO_BLOCO, TAMANHO_BLOCO);

                }else{
                    g.setColor(Color.ORANGE);
                    g.fillRect(eixoX[i], eixoY[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
                }
            }
            g.setColor(Color.PINK);//cor da pontuação
            g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Pontos: "+blocoComidos, (LARGURA_TELA - metrics.stringWidth("Pontos: "+blocoComidos)) /2, g.getFont().getSize());

        }else{
            fimDeJogo(g);
        }
    }

    private void criarBloco(){
        blocoX = random.nextInt(LARGURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
        blocoY = random.nextInt(ALTURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
    }

    public void fimDeJogo(Graphics g){
        g.setColor(Color.PINK);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 42));
        FontMetrics fontePontuacao = getFontMetrics(g.getFont());
        g.drawString("Pontos: "+blocoComidos, (LARGURA_TELA - fontePontuacao.stringWidth("Pontos: "+blocoComidos)) /2 , g.getFont().getSize());
        g.setColor(Color.ORANGE);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 42));
        FontMetrics fonteFinal = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (LARGURA_TELA - fonteFinal.stringWidth("\"Game Over!")) / 2 , ALTURA_TELA / 2);
    }


    public void actionPerformed(ActionEvent e) {
        if (estaRodando) {
            andar();
            alcancarBloco();
            validarLimites();
        }
        repaint();
    }

    private void andar() {
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }

        switch (direcao){
            case 'C':
                eixoY[0] = eixoY[0] - TAMANHO_BLOCO;
                break;
            case 'B':
                eixoY[0] = eixoY[0] + TAMANHO_BLOCO;
                break;
            case 'E':
                eixoX[0] = eixoX[0] - TAMANHO_BLOCO;
                break;
            case 'D':
                eixoX[0] = eixoX[0] + TAMANHO_BLOCO;
                break;
            default:
                break;
        }
    }

    private void alcancarBloco() {
        if (eixoX[0] == blocoX && eixoY[0] == blocoY) {
            corpoCobra++;
            blocoComidos++;
            criarBloco();
        }
    }

    private void validarLimites() {
        //A cabeça bateu no corpo?
        int i;
        for (i = corpoCobra; i > 0; i--) {
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                estaRodando = false;
                break;
            }
        }

        //A cabeça tocou uma das bordas Direita ou esquerda?
        if (eixoX[0] < 0 || eixoX[0] > LARGURA_TELA) {
            estaRodando = false;
        }

        //A cabeça tocou o piso ou o teto?
        if (eixoY[0] < 0 || eixoY[0] > ALTURA_TELA) {
            estaRodando = false;
        }

        if (!estaRodando) {
            timer.stop();
        }
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public void putValue(String key, Object value) {

    }

    public class LeitorDeTeclasAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') {
                        direcao = 'E';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'E') {
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direcao != 'B') {
                        direcao = 'C';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'C') {
                        direcao = 'B';
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
