package model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Passaro extends Personagem {
    // Física em double para permitir uma gravidade "leve" (valores fracionários)
    private static final double GRAVIDADE = 0.4;          // antes: 1 (mais pesado)
    private static final double FORCA_PULO = -7.0;        // antes: -10
    private static final double VELOCIDADE_MAX_QUEDA = 8; // limita a queda para não despencar

    private double posY;
    private double velY = 0;
    private boolean vivo = true;

    public Passaro(int x, int y, int largura, int altura, String caminhoImagem) {
        super(x, y, largura, altura, caminhoImagem);
        this.posY = y;
        setVelocidadeX(0);
        setVelocidadeY(0);
    }

    public void pular() {
        if (vivo) velY = FORCA_PULO;
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        if (!vivo) return;

        velY = Math.min(velY + GRAVIDADE, VELOCIDADE_MAX_QUEDA);
        posY += velY;

        if (posY < 0) {
            posY = 0;
            velY = 0;
        }

        if (posY + getAltura() >= alturaTela) {
            posY = alturaTela - getAltura();
            velY = 0;
            morrer();
        }

        // Sincroniza com os atributos inteiros herdados de Forma (usados na colisão)
        setY((int) Math.round(posY));
        setVelocidadeY((int) Math.round(velY));
    }

    @Override
    public void desenhar(Graphics2D g) {
        // Salva a configuração atual da tela
        AffineTransform configuracaoAntiga = g.getTransform();

        // Calcula a inclinação (limita entre virado para cima e bico para baixo)
        double angulo = Math.toRadians(Math.max(-30, Math.min(velY * 6, 70)));

        // Rotação centralizada no meio do pássaro
        g.rotate(angulo, getX() + (getLargura() / 2.0), getY() + (getAltura() / 2.0));

        // Pinta a imagem rotacionada
        super.desenhar(g);

        // Restaura a tela para não girar os outros elementos (como os canos)
        g.setTransform(configuracaoAntiga);
    }

    public boolean isVivo() { return vivo; }
    public void morrer() { vivo = false; }
}
