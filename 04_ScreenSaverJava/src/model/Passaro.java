package model;

public class Passaro extends Personagem {
    private int gravidade = 1;
    private int forcaPulo = -10;
    
    public Passaro(int x, int y, int largura, int altura, String caminhoImagem) {
        super(x, y, largura, altura, caminhoImagem);
        // O pássaro não anda para os lados, apenas cai e pula
        setVelocidadeX(0); 
        setVelocidadeY(0);
    }

    public void pular() {
        setVelocidadeY(forcaPulo);
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        // Aplica a gravidade continuamente
        setVelocidadeY(getVelocidadeY() + gravidade);
        setY(getY() + getVelocidadeY());

        // Cria um "teto" invisível para o pássaro não sumir por cima da tela
        if (getY() < 0) {
            setY(0);
            setVelocidadeY(0);
        }
        
        // Impede que passe do chão da tela temporariamente (até criarmos o Game Over)
        if (getY() + getAltura() > alturaTela) {
            setY(alturaTela - getAltura());
            setVelocidadeY(0);
        }
    }
}