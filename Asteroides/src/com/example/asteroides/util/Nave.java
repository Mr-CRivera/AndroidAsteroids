package com.example.asteroides.util;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Nave {

	// variables para la nave
	private Grafico grafico;// Gráfico de la nave
	private double giroNave; // Incremento de dirección
	private float aceleracionNave; // aumento de velocidad

	// ===================================================
	public Nave(View vista, Drawable drawableNave) {
		grafico = new Grafico(vista, drawableNave);
	}

	// ===================================================
	public void centrar(double ancho, double alto) {
		grafico.setPosX(ancho / 2);
		grafico.setPosY(alto / 2);
	}

	// ===================================================
	public void dibujaGrafico(Canvas canvas) {
		grafico.dibujaGrafico(canvas);
	}

	// ===================================================
	public void actualizaDireccion(double retardo) {
		grafico.setAngulo((int) (grafico.getAngulo() + giroNave * retardo));
		// descarta el giro hasta que se produzca un nuevo cambio de dirección
		giroNave = 0;
	}

	// ===================================================
	public void actualizaVelocidad(double retardo) {
		double nIncX = grafico.getIncX() + aceleracionNave
				* Math.cos(Math.toRadians(grafico.getAngulo())) * retardo;
		double nIncY = grafico.getIncY() + aceleracionNave
				* Math.sin(Math.toRadians(grafico.getAngulo())) * retardo;
		if (Math.hypot(nIncX, nIncY) <= Grafico.MAX_VELOCIDAD) {
			grafico.setIncX(nIncX);
			grafico.setIncY(nIncY);
		}
		// descarta la aceleración hasta que se produzca un nuevo toque
		aceleracionNave = 0;
	}

	// ===================================================
	public double getCentroAncho() {
		return grafico.getPosX() + grafico.getAncho() / 2;
	}

	// ===================================================
	public double getCentroAlto() {
		return grafico.getPosY() + grafico.getAlto() / 2;
	}

	// ===================================================
	public void incrementaPos(double retardo) {
		grafico.incrementaPos(retardo);
	}

	// ===================================================
	public double distancia(Grafico g) {
		return g.distancia(grafico);
	}

	// ===================================================
	// ===================================================
	public int getAngulo() {
		return grafico.getAngulo();
	}

	public double getGiroNave() {
		return giroNave;
	}

	public void setGiroNave(double giroNave) {
		this.giroNave = giroNave;
	}

	public float getAceleracionNave() {
		return aceleracionNave;
	}

	public void setAceleracionNave(float aceleracionNave) {
		this.aceleracionNave = aceleracionNave;
	}
	// ===================================================
	// ===================================================
}
