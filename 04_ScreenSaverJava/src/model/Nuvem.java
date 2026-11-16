package model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Nuvem extends Elipse {

    private static final Color SOMBRA = new Color(205, 225, 240, 200);

    public Nuvem(int x, int y, int largura, int altura, int velX) {
        // Uma nuvem é uma elipse branca com um pouco de transparência
        super(x, y, largura, altura, new Color(255, 255, 255, 230));
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
    public void desenhar(Graphics2D g) {
        // Nuvem "fofinha": várias elipses sobrepostas dentro da bounding box
        int x = getX(), y = getY(), w = getLargura(), h = getAltura();

        g.setColor(SOMBRA);
        g.fillRoundRect(x, y + h / 2, w, h / 2, h / 2, h / 2);

        g.setColor(getCor());
        g.fillOval(x, y + h * 2 / 5, w * 2 / 5, h * 3 / 5 - 4);
        g.fillOval(x + w / 5, y + h / 6, w * 2 / 5, h * 3 / 4);
        g.fillOval(x + w * 2 / 5, y, w * 2 / 5, h * 4 / 5);
        g.fillOval(x + w * 3 / 5, y + h / 3, w * 2 / 5, h * 3 / 5);
        g.fillRoundRect(x + w / 10, y + h / 2, w * 4 / 5, h / 2 - 4, h / 3, h / 3);
    }

    @Override
    public void aoColidir(Passaro passaro) {
        // Polimorfismo puro: A nuvem não causa Game Over! Ela simplesmente não faz nada.
    }
}
