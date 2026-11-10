package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Forma {
    // Encapsulamento: atributos privados
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
        setLargura(largura); // Usa o setter para aproveitar a validação
        setAltura(altura);
        this.cor = cor;
        this.velocidadeX = 5; // Velocidade padrão do protetor de tela
        this.velocidadeY = 5;
    }

    public abstract void desenhar(Graphics2D g);

    public void Mover(int larguraTela, int alturaTela) {
        setX(getX() + getVelocidadeX());
        setY(getY() + getVelocidadeY());

        // Mantendo a lógica base do protetor de tela (rebatendo nas bordas)
        if (getX() < 0 || getX() + getLargura() > larguraTela) {
            setVelocidadeX(getVelocidadeX() * -1);
        }
        if (getY() < 0 || getY() + getAltura() > alturaTela) {
            setVelocidadeY(getVelocidadeY() * -1);
        }
    }

    // Método de detecção de colisão por Bounding Box
    public boolean intersects(Forma outra) {
        Rectangle minhaCaixa = new Rectangle(this.getX(), this.getY(), this.getLargura(), this.getAltura());
        Rectangle outraCaixa = new Rectangle(outra.getX(), outra.getY(), outra.getLargura(), outra.getAltura());
        return minhaCaixa.intersects(outraCaixa);
    }

    // Getters e Setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getLargura() { return largura; }
    public void setLargura(int largura) {
        if (largura > 0) { // Validação
            this.largura = largura;
        }
    }

    public int getAltura() { return altura; }
    public void setAltura(int altura) {
        if (altura > 0) { // Validação
            this.altura = altura;
        }
    }

    public int getVelocidadeX() { return velocidadeX; }
    public void setVelocidadeX(int velocidadeX) { this.velocidadeX = velocidadeX; }

    public int getVelocidadeY() { return velocidadeY; }
    public void setVelocidadeY(int velocidadeY) { this.velocidadeY = velocidadeY; }

    public Color getCor() { return cor; }
    public void setCor(Color cor) { this.cor = cor; }
}