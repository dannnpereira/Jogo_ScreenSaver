package model;

public class Circulo extends Elipse{
    //Construtor
    public Circulo(int ScreenWidth, int ScreenHeigth){
        super(ScreenWidth, ScreenHeigth);
        width = height;
    }
    
}
