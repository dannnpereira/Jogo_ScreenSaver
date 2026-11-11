package model;

public class Passaro extends Personagem {
    private int gravidade = 1;
    private int forcaPulo = -10;
    
    // NOVO: Adicionado estado de vida
    private boolean vivo = true; 
    
    public Passaro(int x, int y, int largura, int altura, String caminhoImagem) {
        super(x, y, largura, altura, caminhoImagem);
        setVelocidadeX(0); 
        setVelocidadeY(0);
    }

    public void pular() {
        if (vivo) { // Só pula se estiver vivo
            setVelocidadeY(forcaPulo);
        }
    }

    @Override
    public void Mover(int larguraTela, int alturaTela) {
        if (!vivo) return; // Se morreu, a gravidade e o movimento param

        setVelocidadeY(getVelocidadeY() + gravidade);
        setY(getY() + getVelocidadeY());

        if (getY() < 0) {
            setY(0);
            setVelocidadeY(0);
        }
        
        // NOVO: O pássaro morre ao bater no chão
        if (getY() + getAltura() >= alturaTela) {
            setY(alturaTela - getAltura());
            setVelocidadeY(0);
            setVivo(false);
        }
    }

    public boolean isVivo() { return vivo; }
    public void setVivo(boolean vivo) { this.vivo = vivo; }
}