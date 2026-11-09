package model;

public class Quadrado extends Retangulo{
    //Construtor
    public Quadrado(int ScreenWidth, int ScreenHeigth){
        super(ScreenWidth, ScreenHeigth);
        width = height;
    }
    
}

