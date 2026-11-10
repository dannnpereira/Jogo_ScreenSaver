package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Forma {
    private int x;
    private int y;
    private int largura;
    private int altura;
    private int velocidadeX;
    private int velocidadeY;
    private Color cor;

    public Forma(int x, int y, int largura, int altura, Color cor) {
        this.x = x;
        this.y = y;
        setLargura(largura);
        setAltura(altura);
        this.cor = cor;
        this.velocidadeX = 5; 
        this.velocidadeY = 5;
    }

    public abstract void desenhar(Graphics2D g);

    public void Mover(int larguraTela, int alturaTela) {
        setX(getX() + getVelocidadeX());
        setY(getY() + getVelocidadeY());

        if (getX() < 0 || getX() + getLargura() > larguraTela) {
            setVelocidadeX(getVelocidadeX() * -1);
        }
        if (getY() < 0 || getY() + getAltura() > alturaTela) {
            setVelocidadeY(getVelocidadeY() * -1);
        }
    }

    public boolean intersects(Forma outra) {
        Rectangle minhaCaixa = new Rectangle(this.getX(), this.getY(), this.getLargura(), this.getAltura());
        Rectangle outraCaixa = new Rectangle(outra.getX(), outra.getY(), outra.getLargura(), outra.getAltura());
        return minhaCaixa.intersects(outraCaixa);
    }

    // NOVO: Método polimórfico para tratar colisões.
    // Por padrão, uma forma genérica não faz nada ao ser tocada pelo pássaro.
    public void aoColidir(Passaro passaro) {
        // Vazio de propósito
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getLargura() { return largura; }
    public void setLargura(int largura) {
        if (largura > 0) this.largura = largura;
    }

    public int getAltura() { return altura; }
    public void setAltura(int altura) {
        if (altura > 0) this.altura = altura;
    }

    public int getVelocidadeX() { return velocidadeX; }
    public void setVelocidadeX(int velocidadeX) { this.velocidadeX = velocidadeX; }

    public int getVelocidadeY() { return velocidadeY; }
    public void setVelocidadeY(int velocidadeY) { this.velocidadeY = velocidadeY; }

    public Color getCor() { return cor; }
    public void setCor(Color cor) { this.cor = cor; }
}