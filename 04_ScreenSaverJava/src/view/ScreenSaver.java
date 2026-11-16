package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import audio.MusicaFundo;
import model.Cano;
import model.Forma;
import model.Nuvem;
import model.Passaro;
import util.Recursos;

public class ScreenSaver extends JFrame {

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int CHAO_ALTURA = 40; 
    private static final int DELAY_MS = 16; 

    private ScreenSaverPanel canvas;
    private MusicaFundo musica = new MusicaFundo();

    public ScreenSaver() {
        super("Jogo - Flappy Bird (Base POO)");
        this.canvas = new ScreenSaverPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setContentPane(canvas);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void start() {
        this.setVisible(true);
        this.canvas.startAnimation();
        this.musica.tocar();
        this.canvas.requestFocusInWindow();
    }

    private class ScreenSaverPanel extends JPanel implements ActionListener {

        private Timer timer;
        
        /*********************************
        ** ↓ Declare suas formas aqui ↓ **
        *********************************/
        private List<Forma> formas = new ArrayList<>();
        private List<Forma> nuvens = new ArrayList<>(); 
        
        private Passaro jogador;
        private Image logo;
        
        private boolean up = false;
        private boolean puloPronto = true;
        
        private int contadorFrames = 0; 
        private int pontuacao = 0;
        private int recorde = 0;
        private int meiosPontos = 0; 
        private int velocidadeGlobal = -4; 
        
        private boolean jogoIniciado = false;
        private boolean gameOver = false;
        private boolean novoRecorde = false;

        // Animação do cenário
        private int tick = 0;                 // frames desde que o jogo abriu
        private double deslocamentoChao = 0;  // rolagem do chão (e dos morros, em parallax)

        public ScreenSaverPanel() {
            this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            this.setFocusable(true);

            logo = Recursos.carregarImagem("image/logo.png");

            /***********************************
            ** ↓ Instancie suas formas aqui ↓ **
            ***********************************/
            nuvens.add(new Nuvem(100, 80, 120, 60, -1));
            nuvens.add(new Nuvem(450, 120, 150, 75, -2));
            nuvens.add(new Nuvem(700, 50, 100, 50, -1));

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                        acaoPular();
                    } else if (e.getKeyCode() == KeyEvent.VK_M) {
                        musica.alternarMudo();
                        repaint();
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) up = false;
                }
            });

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) { acaoPular(); }
                @Override
                public void mouseReleased(MouseEvent e) { up = false; }
            });

            reiniciarJogo(); 
            this.timer = new Timer(DELAY_MS, this);
        }

        private void acaoPular() {
            if (!jogoIniciado) {
                jogoIniciado = true;
                up = true;
            } else if (gameOver && puloPronto) {
                reiniciarJogo();
            } else {
                up = true;
            }
        }

        private void reiniciarJogo() {
            formas.clear();
            jogador = new Passaro(150, 250, 48, 32, "image/flipbird.png");
            formas.add(jogador);
            
            pontuacao = 0;
            meiosPontos = 0;
            velocidadeGlobal = -4;
            contadorFrames = 0;
            gameOver = false;
            novoRecorde = false;
            jogoIniciado = false;
            puloPronto = false; 
            up = false;
        }

        private void gerarParDeCanos() {
            int espacoAbertura = 160; 
            int larguraCano = 70;
            int alturaCanoCima = 50 + (int)(Math.random() * (CANVAS_HEIGHT - CHAO_ALTURA - espacoAbertura - 100));
            
            Cano canoCima = new Cano(CANVAS_WIDTH, 0, larguraCano, alturaCanoCima, velocidadeGlobal, true);
            Cano canoBaixo = new Cano(CANVAS_WIDTH, alturaCanoCima + espacoAbertura, larguraCano, CANVAS_HEIGHT - CHAO_ALTURA - alturaCanoCima - espacoAbertura, velocidadeGlobal, false);
            
            formas.add(canoCima);
            formas.add(canoBaixo);
        }

        @Override
        public Dimension getPreferredSize() { return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT); }
        @Override
        public Dimension getMinimumSize() { return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT); }
        public void startAnimation() { this.timer.start(); }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (!up) puloPronto = true; 

            /******************************
            ** ↓ Mova suas formas aqui ↓ **
            ******************************/
            for (Forma n : nuvens) {
                n.Mover(CANVAS_WIDTH, CANVAS_HEIGHT);
            }

            tick++;
            if (!gameOver) deslocamentoChao += jogoIniciado ? velocidadeGlobal : -2;

            if (!jogoIniciado) {
                // Na tela inicial o pássaro fica flutuando suavemente
                jogador.pairar(250 + (int) Math.round(Math.sin(tick * 0.08) * 10));
            }

            if (!jogoIniciado || gameOver) {
                this.repaint();
                return;
            }

            if (up && puloPronto) {
                jogador.pular();
                puloPronto = false;
            }

            contadorFrames++;
            int limiteFrames = 100 + (velocidadeGlobal * 3); 
            if (contadorFrames >= limiteFrames) {
                gerarParDeCanos();
                contadorFrames = 0;
            }

            for (int i = formas.size() - 1; i >= 0; i--) {
                Forma f = formas.get(i);
                
                f.Mover(CANVAS_WIDTH, CANVAS_HEIGHT - CHAO_ALTURA);
                
                if (f != jogador) {
                    if (jogador.intersects(f)) f.aoColidir(jogador);
                    
                    if (!f.isUltrapassada() && f.getX() + f.getLargura() < jogador.getX()) {
                        f.setUltrapassada(true);
                        meiosPontos++;
                        if (meiosPontos % 2 == 0) { 
                            pontuacao++;
                            if (pontuacao % 5 == 0 && velocidadeGlobal > -10) velocidadeGlobal--; 
                        }
                    }

                    if (f.getX() + f.getLargura() < 0) formas.remove(i);
                }
            }

            if (!jogador.isVivo()) {
                gameOver = true;
                if (pontuacao > recorde) {
                    recorde = pontuacao;
                    novoRecorde = true;
                }
            }

            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            desenharCeu(g2d);

            /*********************************
            ** ↓ Desenhe suas formas aqui ↓ **
            *********************************/
            for (Forma n : nuvens) { n.desenhar(g2d); }

            desenharMorros(g2d);

            for (Forma f : formas) { f.desenhar(g2d); }

            desenharChao(g2d);

            if (!jogoIniciado) {
                desenharTelaInicial(g2d);
            } else {
                desenharPlacar(g2d);
                if (gameOver) desenharGameOver(g2d);
            }
        }

        /* ============================ Cenário ============================ */

        private void desenharCeu(Graphics2D g) {
            g.setPaint(new LinearGradientPaint(0, 0, 0, CANVAS_HEIGHT - CHAO_ALTURA,
                    new float[] { 0f, 0.6f, 1f },
                    new Color[] { new Color(78, 173, 230), new Color(150, 215, 240), new Color(225, 245, 235) }));
            g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

            // Sol com brilho em volta
            int sx = 660, sy = 105;
            for (int r = 90; r > 40; r -= 12) {
                g.setColor(new Color(255, 240, 170, 28));
                g.fillOval(sx - r, sy - r, r * 2, r * 2);
            }
            g.setColor(new Color(255, 230, 120));
            g.fillOval(sx - 38, sy - 38, 76, 76);
            g.setColor(new Color(255, 245, 190));
            g.fillOval(sx - 28, sy - 30, 50, 50);
        }

        private void desenharMorros(Graphics2D g) {
            int horizonte = CANVAS_HEIGHT - CHAO_ALTURA;
            // Camadas em parallax: as do fundo andam mais devagar
            desenharOndas(g, horizonte, 150, 45, 0.011, 0.15, new Color(160, 210, 185));
            desenharOndas(g, horizonte, 95, 30, 0.018, 0.35, new Color(115, 185, 125));

            // Fileira de arbustos logo acima do chão
            g.setColor(new Color(80, 155, 75));
            int passo = 46;
            int desloc = Math.floorMod((int) (deslocamentoChao * 0.7), passo);
            for (int x = desloc - passo; x < CANVAS_WIDTH + passo; x += passo) {
                g.fillOval(x, horizonte - 26, 56, 46);
                g.fillOval(x + 22, horizonte - 18, 44, 40);
            }
        }

        private void desenharOndas(Graphics2D g, int base, int altura, int amplitude,
                                   double frequencia, double parallax, Color cor) {
            Path2D.Double morro = new Path2D.Double();
            morro.moveTo(0, base);
            double desloc = deslocamentoChao * parallax;
            for (int x = 0; x <= CANVAS_WIDTH; x += 8) {
                double t = (x - desloc) * frequencia;
                double y = base - altura + Math.sin(t) * amplitude + Math.sin(t * 2.3) * amplitude * 0.35;
                morro.lineTo(x, y);
            }
            morro.lineTo(CANVAS_WIDTH, base);
            morro.closePath();
            g.setColor(cor);
            g.fill(morro);
        }

        private void desenharChao(Graphics2D g) {
            int topo = CANVAS_HEIGHT - CHAO_ALTURA;

            // Terra
            g.setPaint(new GradientPaint(0, topo, new Color(225, 200, 140), 0, CANVAS_HEIGHT, new Color(195, 160, 100)));
            g.fillRect(0, topo, CANVAS_WIDTH, CHAO_ALTURA);

            // Faixa de grama com listras diagonais que rolam junto com o jogo
            int alturaGrama = 14;
            g.setColor(new Color(110, 195, 60));
            g.fillRect(0, topo, CANVAS_WIDTH, alturaGrama);
            Shape clipAntigo = g.getClip();
            g.clipRect(0, topo, CANVAS_WIDTH, alturaGrama);
            g.setColor(new Color(150, 220, 90));
            int passo = 24;
            int desloc = Math.floorMod((int) deslocamentoChao, passo);
            for (int x = desloc - passo; x < CANVAS_WIDTH + passo; x += passo) {
                g.fillPolygon(new int[] { x, x + 12, x + 12 + alturaGrama, x + alturaGrama },
                              new int[] { topo + alturaGrama, topo + alturaGrama, topo, topo }, 4);
            }
            g.setClip(clipAntigo);

            g.setColor(new Color(60, 120, 35));
            g.fillRect(0, topo, CANVAS_WIDTH, 3);
            g.setColor(new Color(150, 115, 60));
            g.fillRect(0, topo + alturaGrama, CANVAS_WIDTH, 3);
        }

        /* ============================ Interface ============================ */

        private void desenharTelaInicial(Graphics2D g) {
            desenharTextoCentralizado(g, "FLAPPY BIRD", 165, new Font("Arial", Font.BOLD, 64),
                    new Color(255, 215, 60), new Color(70, 40, 10), 8);
            desenharTextoCentralizado(g, "Projeto POO", 205, new Font("Arial", Font.BOLD, 22),
                    Color.WHITE, new Color(40, 90, 140), 5);

            // Instrução "piscando" suavemente
            int alfa = 155 + (int) (100 * Math.abs(Math.sin(tick * 0.05)));
            desenharTextoCentralizado(g, "Clique ou pressione ESPAÇO para começar", 350, new Font("Arial", Font.BOLD, 22),
                    new Color(255, 255, 255, alfa), new Color(30, 60, 90, alfa), 5);
            desenharTextoCentralizado(g, "M: música " + (musica.isMudo() ? "desligada" : "ligada"), 385,
                    new Font("Arial", Font.BOLD, 16), Color.WHITE, new Color(30, 60, 90), 4);

            if (logo != null) {
                int lw = 250, lh = 39;
                int cx = (CANVAS_WIDTH - lw) / 2, cy = 470;
                g.setColor(new Color(255, 255, 255, 215));
                g.fillRoundRect(cx - 18, cy - 12, lw + 36, lh + 24, 20, 20);
                g.drawImage(logo, cx, cy, lw, lh, null);
            }
        }

        private void desenharPlacar(Graphics2D g) {
            if (!gameOver) {
                desenharTextoCentralizado(g, String.valueOf(pontuacao), 80, new Font("Arial", Font.BOLD, 56),
                        Color.WHITE, new Color(40, 40, 40), 8);
            }

            // Selo do recorde no canto superior direito
            String texto = "Recorde: " + recorde;
            Font fonte = new Font("Arial", Font.BOLD, 18);
            int largura = g.getFontMetrics(fonte).stringWidth(texto) + 28;
            int x = CANVAS_WIDTH - largura - 16, y = 16;
            g.setColor(new Color(0, 0, 0, 90));
            g.fillRoundRect(x, y, largura, 34, 34, 34);
            g.setFont(fonte);
            g.setColor(Color.WHITE);
            g.drawString(texto, x + 14, y + 23);
        }

        private void desenharGameOver(Graphics2D g) {
            g.setColor(new Color(0, 0, 0, 110));
            g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

            desenharTextoCentralizado(g, "GAME OVER", 175, new Font("Arial", Font.BOLD, 64),
                    new Color(255, 120, 60), Color.WHITE, 8);

            // Painel com a pontuação
            int pw = 320, ph = 160, px = (CANVAS_WIDTH - pw) / 2, py = 215;
            g.setColor(new Color(0, 0, 0, 70));
            g.fillRoundRect(px + 5, py + 7, pw, ph, 28, 28);
            g.setColor(new Color(252, 240, 205));
            g.fillRoundRect(px, py, pw, ph, 28, 28);
            Stroke antigo = g.getStroke();
            g.setStroke(new BasicStroke(4));
            g.setColor(new Color(120, 80, 40));
            g.drawRoundRect(px, py, pw, ph, 28, 28);
            g.setStroke(antigo);

            Font rotulo = new Font("Arial", Font.BOLD, 18);
            Font valor = new Font("Arial", Font.BOLD, 44);
            int colEsq = px + pw / 4, colDir = px + pw * 3 / 4;
            desenharTextoCentralizadoEm(g, "PONTOS", colEsq, py + 42, rotulo, new Color(200, 110, 50));
            desenharTextoCentralizadoEm(g, "RECORDE", colDir, py + 42, rotulo, new Color(200, 110, 50));
            desenharTextoCentralizadoEm(g, String.valueOf(pontuacao), colEsq, py + 100, valor, new Color(70, 45, 20));
            desenharTextoCentralizadoEm(g, String.valueOf(recorde), colDir, py + 100, valor, new Color(70, 45, 20));

            if (novoRecorde) {
                int bw = 160, bx = px + (pw - bw) / 2, by = py + ph - 44;
                g.setColor(new Color(230, 70, 60));
                g.fillRoundRect(bx, by, bw, 30, 30, 30);
                desenharTextoCentralizadoEm(g, "NOVO RECORDE!", bx + bw / 2, by + 21, new Font("Arial", Font.BOLD, 16), Color.WHITE);
            }

            int alfa = 155 + (int) (100 * Math.abs(Math.sin(tick * 0.05)));
            desenharTextoCentralizado(g, "Pressione ESPAÇO para jogar de novo", 430, new Font("Arial", Font.BOLD, 22),
                    new Color(255, 255, 255, alfa), new Color(30, 30, 30, alfa), 5);
        }

        /** Texto centralizado na tela, com contorno (estilo de jogo). */
        private void desenharTextoCentralizado(Graphics2D g, String texto, int y, Font fonte,
                                               Color preenchimento, Color contorno, float espessura) {
            TextLayout layout = new TextLayout(texto, fonte, g.getFontRenderContext());
            double x = (CANVAS_WIDTH - layout.getAdvance()) / 2.0;
            Shape forma = layout.getOutline(AffineTransform.getTranslateInstance(x, y));

            Stroke antigo = g.getStroke();
            g.setStroke(new BasicStroke(espessura, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(contorno);
            g.draw(forma);
            g.setStroke(antigo);
            g.setColor(preenchimento);
            g.fill(forma);
        }

        /** Texto simples centralizado em torno de um ponto x. */
        private void desenharTextoCentralizadoEm(Graphics2D g, String texto, int centroX, int y, Font fonte, Color cor) {
            g.setFont(fonte);
            g.setColor(cor);
            g.drawString(texto, centroX - g.getFontMetrics().stringWidth(texto) / 2, y);
        }
    }
}
