package model;

import java.awt.Color;

public class Circulo extends Elipse {
    public Circulo(int x, int y, int diametro, Color cor) {
        super(x, y, diametro, diametro, cor);
    }

    @Override
    public void setLargura(int largura) {
        super.setLargura(largura);
        super.setAltura(largura);
    }

    @Override
    public void setAltura(int altura) {
        super.setLargura(altura);
        super.setAltura(altura);
    }
}