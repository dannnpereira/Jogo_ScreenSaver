package view;

import java.awt.Color;
import java.awt.Dimension;
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
        
        // NOVO: Contador para saber quando gerar novos canos
        private int contadorFrames = 0; 

        public ScreenSaverPanel() {
            this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            this.setBackground(new Color(135, 206, 235));
            this.setFocusable(true);

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) up = true;
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) up = false;
                }
            });

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) { up = true; }
                @Override
                public void mouseReleased(MouseEvent e) { up = false; }
            });

            // Deixa apenas o pássaro na lista inicial
            jogador = new Passaro(100, 250, 60, 45, "src/image/logo.png");
            formas.add(jogador);

            this.timer = new Timer(DELAY_MS, this);
        }

        private void gerarParDeCanos() {
            int espacoAbertura = 160; // Espaço para o pássaro passar
            int larguraCano = 70;
            
            // Altura aleatória para o cano de cima (entre 50 e 350)
            int alturaCanoCima = 50 + (int)(Math.random() * 300);
            
            Cano canoCima = new Cano(CANVAS_WIDTH, 0, larguraCano, alturaCanoCima);
            Cano canoBaixo = new Cano(CANVAS_WIDTH, alturaCanoCima + espacoAbertura, larguraCano, CANVAS_HEIGHT - alturaCanoCima - espacoAbertura);
            
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
            
            if (up && puloPronto) {
                jogador.pular();
                puloPronto = false;
            } else if (!up) {
                puloPronto = true;
            }

            // Geração periódica de canos (a cada ~100 frames = 1.6 segundos)
            contadorFrames++;
            if (contadorFrames >= 100) {
                gerarParDeCanos();
                contadorFrames = 0;
            }

            // REQUISITO DO PROFESSOR: Laço reverso para atualizar, verificar colisões e remover
            for (int i = formas.size() - 1; i >= 0; i--) {
                Forma f = formas.get(i);
                f.Mover(CANVAS_WIDTH, CANVAS_HEIGHT);
                
                // Ignora colisão do pássaro com ele mesmo
                if (f != jogador) {
                    if (jogador.intersects(f)) {
                        f.aoColidir(jogador);
                    }
                    
                    // Se o obstáculo saiu completamente da tela pela esquerda, remove da lista
                    if (f.getX() + f.getLargura() < 0) {
                        formas.remove(i);
                    }
                }
            }

            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Forma f : formas) { f.desenhar(g2d); }
        }
    }
}