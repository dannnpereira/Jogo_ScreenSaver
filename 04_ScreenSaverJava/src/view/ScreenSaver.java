package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.Cano;
import model.Forma;
import model.Passaro;

public class ScreenSaver extends JFrame {

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int DELAY_MS = 16; 

    private ScreenSaverPanel canvas;

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
        this.canvas.requestFocusInWindow();
    }

    private class ScreenSaverPanel extends JPanel implements ActionListener {

        private Timer timer;
        private List<Forma> formas = new ArrayList<>();
        private Passaro jogador;
        private boolean up = false;
        private boolean puloPronto = true;
        
        // Variáveis de Jogo
        private int contadorFrames = 0; 
        private int pontuacao = 0;
        private int recorde = 0;
        private int meiosPontos = 0; // Para contar 1 ponto apenas a cada 2 canos (cima e baixo)
        private int velocidadeGlobal = -4; // Inicia em -4 e vai diminuindo (ficando mais rápido para a esquerda)
        private boolean gameOver = false;

        public ScreenSaverPanel() {
            this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            this.setBackground(new Color(135, 206, 235));
            this.setFocusable(true);

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                        if (gameOver && puloPronto) { // puloPronto impede de reiniciar no instante em que morre
                            reiniciarJogo();
                        } else {
                            up = true;
                        }
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) up = false;
                }
            });

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) { 
                    if (gameOver && puloPronto) reiniciarJogo();
                    else up = true; 
                }
                @Override
                public void mouseReleased(MouseEvent e) { up = false; }
            });

            reiniciarJogo();
            this.timer = new Timer(DELAY_MS, this);
        }

        private void reiniciarJogo() {
            formas.clear();
            jogador = new Passaro(100, 250, 60, 45, "src/image/flipbird.png");
            formas.add(jogador);
            
            pontuacao = 0;
            meiosPontos = 0;
            velocidadeGlobal = -4;
            contadorFrames = 0;
            gameOver = false;
            puloPronto = false; // Exige soltar o botão antes de jogar novamente
            up = false;
        }

        private void gerarParDeCanos() {
            int espacoAbertura = 160; 
            int larguraCano = 70;
            int alturaCanoCima = 50 + (int)(Math.random() * 300);
            
            Cano canoCima = new Cano(CANVAS_WIDTH, 0, larguraCano, alturaCanoCima, velocidadeGlobal);
            Cano canoBaixo = new Cano(CANVAS_WIDTH, alturaCanoCima + espacoAbertura, larguraCano, CANVAS_HEIGHT - alturaCanoCima - espacoAbertura, velocidadeGlobal);
            
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
            
            if (!up) puloPronto = true; // Botão solto, prepara o próximo pulo

            if (gameOver) {
                this.repaint();
                return; // Congela a física do jogo
            }

            if (up && puloPronto) {
                jogador.pular();
                puloPronto = false;
            }

            // Acelera a geração de canos conforme o jogo fica mais rápido
            contadorFrames++;
            int limiteFrames = 100 + (velocidadeGlobal * 3); // Quanto mais negativo, menor o limite (nasce mais rápido)
            if (contadorFrames >= limiteFrames) {
                gerarParDeCanos();
                contadorFrames = 0;
            }

            for (int i = formas.size() - 1; i >= 0; i--) {
                Forma f = formas.get(i);
                f.Mover(CANVAS_WIDTH, CANVAS_HEIGHT);
                
                if (f != jogador) {
                    if (jogador.intersects(f)) f.aoColidir(jogador);
                    
                    // Pontuação genérica (funciona para qualquer forma que passe da tela)
                    if (!f.isUltrapassada() && f.getX() + f.getLargura() < jogador.getX()) {
                        f.setUltrapassada(true);
                        meiosPontos++;
                        
                        if (meiosPontos % 2 == 0) { // A cada par (cima e baixo), ganha 1 ponto
                            pontuacao++;
                            if (pontuacao % 5 == 0) {
                                velocidadeGlobal--; // Aumenta a velocidade do jogo a cada 5 pontos
                            }
                        }
                    }

                    if (f.getX() + f.getLargura() < 0) formas.remove(i);
                }
            }

            // Verifica se o Game Over foi ativado
            if (!jogador.isVivo()) {
                gameOver = true;
                if (pontuacao > recorde) recorde = pontuacao;
            }

            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (Forma f : formas) { f.desenhar(g2d); }

            // Desenha a Interface Visual (HUD)
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("Pontos: " + pontuacao, 20, 40);
            g2d.drawString("Recorde: " + recorde, 20, 70);

            if (gameOver) {
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT); // Tela escurecida
                
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 60));
                g2d.drawString("GAME OVER", 200, 250);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("Pressione ESPAÇO para reiniciar", 230, 320);
            }
        }
    }
}