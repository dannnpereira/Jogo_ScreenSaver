package view;

import model.Forma;
import model.Personagem; // <-- Novo import!

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

        /*********************************
        ** ↓ Declare suas formas aqui ↓ **
        *********************************/
        private List<Forma> formas = new ArrayList<>();

        // Flags para rastrear teclas pressionadas
        private boolean space = false;
        private boolean up = false;
        private boolean down = false;
        private boolean right = false;
        private boolean left = false;

        public ScreenSaverPanel() {
            this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            // Fundo azul céu para o Flappy Bird
            this.setBackground(new Color(135, 206, 235));
            this.setFocusable(true);

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) space = true;
                    if (e.getKeyCode() == KeyEvent.VK_UP) up = true;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) down = true;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) space = false;
                    if (e.getKeyCode() == KeyEvent.VK_UP) up = false;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) down = false;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
                }
            });

            /***********************************
            ** ↓ Instancie suas formas aqui ↓ **
            ***********************************/
            // Instancia apenas o nosso personagem usando a imagem padrão logo.png
            formas.add(new Personagem(350, 250, 80, 80, "src/image/logo.png"));

            this.timer = new Timer(DELAY_MS, this);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
        }

        public void startAnimation() {
            this.timer.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /******************************
            ** ↓ Mova suas formas aqui ↓ **
            ******************************/
            for (Forma f : formas) {
                f.Mover(CANVAS_WIDTH, CANVAS_HEIGHT);
            }

            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            /*********************************
            ** ↓ Desenhe suas formas aqui ↓ **
            *********************************/
            for (Forma f : formas) {
                f.desenhar(g2d);
            }
        }
    }
}