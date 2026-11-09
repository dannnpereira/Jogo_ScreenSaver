package model;

import java.awt.Graphics2D;

public class Elipse extends Forma{
    //Construtor
    public Elipse(int ScreenWidth, int ScreenHeigth){
        super(ScreenWidth, ScreenHeigth);
    }
    
    public void desenhar(Graphics2D g){
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }
    
}
