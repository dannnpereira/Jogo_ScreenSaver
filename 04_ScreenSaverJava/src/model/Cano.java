package model;
import java.awt.Color;

public class Cano extends Retangulo {

    // Recebe a velocidadeX por parâmetro para permitir que o jogo fique mais rápido
    public Cano(int x, int y, int largura, int altura, int velX) {
        super(x, y, largura, altura, new Color(34, 139, 34)); 
        setVelocidadeX(velX);
        setVelocidadeY(0);
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        setX(getX() + getVelocidadeX());
    }

    @Override
    public void aoColidir(Passaro passaro) {
        // Em vez de fechar o jogo, agora o cano altera o estado do pássaro
        passaro.setVivo(false);
    }
}