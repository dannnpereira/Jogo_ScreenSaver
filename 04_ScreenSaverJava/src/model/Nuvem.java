package model;

import java.awt.Color;

public class Nuvem extends Elipse {

    public Nuvem(int x, int y, int largura, int altura, int velX) {
        // Uma nuvem é uma elipse branca com um pouco de transparência
        super(x, y, largura, altura, new Color(255, 255, 255, 200));
        setVelocidadeX(velX);
        setVelocidadeY(0);
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        // A nuvem anda para a esquerda
        setX(getX() + getVelocidadeX());
        
        // Se sair totalmente pela esquerda, reaparece na direita em uma altura aleatória
        if (getX() + getLargura() < 0) {
            setX(larguraTela);
            setY(30 + (int)(Math.random() * 150)); // Céu
        }
    }

    @Override
    public void aoColidir(Passaro passaro) {
        // Polimorfismo puro: A nuvem não causa Game Over! Ela simplesmente não faz nada.
    }
}