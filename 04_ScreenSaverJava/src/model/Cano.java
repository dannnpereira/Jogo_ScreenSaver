package model;

import java.awt.Color;

public class Cano extends Retangulo {

    public Cano(int x, int y, int largura, int altura) {
        // Chama o construtor do Retangulo passando a cor verde
        super(x, y, largura, altura, new Color(34, 139, 34)); 
        
        // O cano anda apenas para a esquerda
        setVelocidadeX(-4);
        setVelocidadeY(0);
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        // O cano ignora as bordas (não rebate), apenas continua indo para a esquerda
        setX(getX() + getVelocidadeX());
    }

    @Override
    public void aoColidir(Passaro passaro) {
        // Se bater no cano, o jogo fecha instantaneamente por enquanto
        System.out.println("GAME OVER! Você bateu no cano.");
        System.exit(0);
    }
}