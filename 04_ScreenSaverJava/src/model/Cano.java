package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Stroke;

public class Cano extends Retangulo {

    private static final int ALTURA_BOCA = 28;   // altura da "boca" (borda larga) do cano
    private static final int RECUO_CORPO = 5;    // o corpo é um pouco mais estreito que a boca

    // Paleta do cano: brilho -> verde claro -> verde -> sombra
    private static final Color[] CORES = {
        new Color(120, 200, 60), new Color(200, 245, 120), new Color(110, 190, 50),
        new Color(75, 150, 35), new Color(45, 105, 20)
    };
    private static final float[] PARADAS = { 0f, 0.22f, 0.45f, 0.8f, 1f };
    private static final Color CONTORNO = new Color(35, 70, 20);

    private final boolean emCima; // true = cano que desce do teto (boca embaixo)

    public Cano(int x, int y, int largura, int altura, int velX, boolean emCima) {
        super(x, y, largura, altura, new Color(110, 190, 50));
        this.emCima = emCima;
        setVelocidadeX(velX);
        setVelocidadeY(0);
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        setX(getX() + getVelocidadeX());
    }

    @Override
    public void desenhar(Graphics2D g) {
        int x = getX(), y = getY(), w = getLargura(), h = getAltura();
        Stroke strokeAntigo = g.getStroke();
        g.setStroke(new BasicStroke(2.5f));

        // 1. Corpo do cano (mais estreito que a boca)
        int corpoX = x + RECUO_CORPO, corpoW = w - 2 * RECUO_CORPO;
        int corpoY = emCima ? y : y + ALTURA_BOCA;
        int corpoH = Math.max(0, h - ALTURA_BOCA);
        pintarCilindro(g, corpoX, corpoY, corpoW, corpoH);

        // 2. Boca do cano, no lado da abertura
        int bocaY = emCima ? y + h - ALTURA_BOCA : y;
        pintarCilindro(g, x, bocaY, w, ALTURA_BOCA);

        // 3. Sombra que a boca projeta no corpo
        g.setColor(new Color(0, 0, 0, 45));
        int sombraY = emCima ? bocaY - 6 : bocaY + ALTURA_BOCA;
        g.fillRect(corpoX + 1, sombraY, corpoW - 1, 6);

        // 4. Faixa de brilho na boca
        g.setColor(new Color(255, 255, 255, 70));
        g.fillRect(x + 3, bocaY + 4, w - 6, 3);

        g.setStroke(strokeAntigo);
    }

    /** Retângulo com degradê horizontal (efeito de tubo arredondado) e contorno escuro. */
    private void pintarCilindro(Graphics2D g, int x, int y, int w, int h) {
        if (w <= 0 || h <= 0) return;
        g.setPaint(new LinearGradientPaint(x, 0, x + w, 0, PARADAS, CORES));
        g.fillRect(x, y, w, h);
        g.setColor(CONTORNO);
        g.drawRect(x, y, w, h);
    }

    public boolean isEmCima() { return emCima; }

    @Override
    public void aoColidir(Passaro passaro) {
        passaro.morrer();
    }
}
