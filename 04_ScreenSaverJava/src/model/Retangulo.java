package model;

import java.awt.Graphics2D;
public class Retangulo extends Forma{
    //Construtor
    public Retangulo(int ScreenWidth, int ScreenHeigth){
        super(ScreenWidth, ScreenHeigth);
        
    }
    public void desenhar(Graphics2D g){
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}
