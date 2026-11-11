package model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Retangulo extends Forma {
    public Retangulo(int x, int y, int largura, int altura, Color cor) {
        super(x, y, largura, altura, cor);
    }

    @Override
    public void desenhar(Graphics2D g) {
        g.setColor(getCor());
        g.fillRect(getX(), getY(), getLargura(), getAltura());
    }
}