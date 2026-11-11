package model;

import java.awt.Color;

public class Quadrado extends Retangulo {
    public Quadrado(int x, int y, int tamanho, Color cor) {
        super(x, y, tamanho, tamanho, cor);
    }

    @Override
    public void setLargura(int largura) {
        super.setLargura(largura);
        super.setAltura(largura); // Quadrado sempre tem lados iguais
    }

    @Override
    public void setAltura(int altura) {
        super.setLargura(altura);
        super.setAltura(altura);
    }
}
