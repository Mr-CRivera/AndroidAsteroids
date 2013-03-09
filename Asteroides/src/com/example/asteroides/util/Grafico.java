package com.example.asteroides.util;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Grafico {
	
	// ===================================================
	 private Drawable drawable;   //Imagen que dibujaremos
	 private View view; //Donde dibujamos el gráfico (usada en view.ivalidate)
	 
	 private double posX, posY;   //Posición
     private double incX, incY;   //Velocidad desplazamiento
     private int angulo, rotacion;//Ángulo y velocidad rotación
     private int ancho, alto;     //Dimensiones de la imagen
     private int radioColision;   //Para determinar colisión

     public static final int MAX_VELOCIDAD = 20; // Para determinar el espacio a borrar (view.ivalidate)
    
     // ===================================================
     public Grafico(View view, Drawable drawable){

           this.view = view;
           this.drawable = drawable;
           
           ancho = drawable.getIntrinsicWidth();  
           alto = drawable.getIntrinsicHeight();
           radioColision = (alto+ancho)/4;

     }

     // ===================================================
      public void dibujaGrafico(Canvas canvas){

           canvas.save();

           //calculo el centro del gráfico
           int x=(int) (posX+ancho/2);
           int y=(int) (posY+alto/2);

           //lo roto el ángulo actual para dibujar girado el gráfico
           canvas.rotate((float) angulo,(float) x,(float) y);

           //establezco el tamaño del gráfico y lo dibujo en el canvas
           drawable.setBounds((int)posX, (int)posY, (int)posX+ancho, (int)posY+alto);
           drawable.draw(canvas);

           canvas.restore();

           //borro la posición anterior del gráfico
           int rInval = (int) Math.hypot(ancho,alto)/2 + MAX_VELOCIDAD;
           view.invalidate(x-rInval, y-rInval, x+rInval, y+rInval);

     }

     // ===================================================
     public void incrementaPos(double factor){

    	 //primero eje X
   	 	 posX+=incX * factor;

         // Si salimos de la pantalla, corregimos posición
         if(posX<-ancho/2) {posX=view.getWidth()-ancho/2;}
         if(posX>view.getWidth()-ancho/2) {posX=-ancho/2;}


         //ahora eje Y
         posY+=incY * factor;

         // Si salimos de la pantalla, corregimos posición
         if(posY<-alto/2) {posY=view.getHeight()-alto/2;}
         if(posY>view.getHeight()-alto/2) {posY=-alto/2;}

         //y actualizamos ángulo
         angulo += rotacion * factor;

     }

     // ===================================================
     // calcula la distancia respecto a otro gráfico
     public double distancia(Grafico g) {

           return Math.hypot(posX-g.posX, posY-g.posY);

     }


     // ===================================================
     // determina si existe colisión entre este gráfico y otro pasado
     public boolean verificaColision(Grafico g) {

           return(distancia(g) < (radioColision+g.radioColision));

     }

    // ===================================================
    // ===================================================
	// Getter y Setter
     
	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getIncX() {
		return incX;
	}

	public void setIncX(double incX) {
		this.incX = incX;
	}

	public double getIncY() {
		return incY;
	}

	public void setIncY(double incY) {
		this.incY = incY;
	}

	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public int getRotacion() {
		return rotacion;
	}

	public void setRotacion(int rotacion) {
		this.rotacion = rotacion;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getRadioColision() {
		return radioColision;
	}

	public void setRadioColision(int radioColision) {
		this.radioColision = radioColision;
	}

  // ===================================================
  // ===================================================
}
