package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public abstract class Forma {
    //Atributos
    protected int x;
    protected int y;
    protected int speedx;
    protected int speedy;
    protected int width;
    protected int height;
    protected Color color;

    //Construtor
    public Forma(int ScreenWidth, int ScreenHeigth){
        Random r = new Random();
        width = r.nextInt(150) + 50;
        height = r.nextInt(150) + 50;
        x= r.nextInt(ScreenWidth-width);
        y= r.nextInt(ScreenHeigth-height);
        speedx = r.nextInt(5) + 1;
        if(r.nextBoolean()){
            speedx*= -1;
        }
        color = ColorGenerate();
    }
    
    //Métodos
    private Color ColorGenerate(){
        Random r = new Random();
        return new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
    }
    public void Mover(int ScreenWidth, int ScreenHeigth){
        if (x <= 0 || x >= ScreenWidth-width) {
            speedx*= -1;
            color = ColorGenerate();
        }
        if (y <= 0 || y>= ScreenHeigth-height) {
            speedy*= -1;
            color = ColorGenerate();
        }
        x += speedx;
        y += speedy;
    }
    public abstract void desenhar(Graphics2D g);

}