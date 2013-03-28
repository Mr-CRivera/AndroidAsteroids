package com.example.asteroides.util;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Misil {
	private Grafico grafico;
	private static int PASO_VELOCIDAD_MISIL = 13;
	private boolean misilActivo = false;
	private int tiempoMisil;

	// ===================================================
	// ===================================================

	public Misil(View vista, Drawable drawableNave) {
		grafico = new Grafico(vista, drawableNave);
	}

	// ===================================================
	public void dibujaGrafico(Canvas canvas) {
		if (misilActivo) {
			grafico.dibujaGrafico(canvas);
		}
	}

	// ===================================================
	public void incrementaPos(double retardo) {
		if (misilActivo) {
			grafico.incrementaPos(retardo);
			tiempoMisil -= retardo;
			if (tiempoMisil < 0) {
				misilActivo = false;
			}

		}
	}

	// ===================================================
	public void colision(VAsteroides vAsteroides) {
		if (misilActivo) {
			// comprueba la colisión y elimina el asteroide
			if (vAsteroides.colision(grafico)) {
				misilActivo = false;
			}
		}
	}

	// ===================================================
	// activa el misil
	public synchronized void activaMisil(Nave nave, int width, int height) {
		grafico.setPosX(nave.getCentroAncho() - grafico.getAncho() / 2);
		grafico.setPosY(nave.getCentroAlto() - grafico.getAlto() / 2);
		grafico.setAngulo(nave.getAngulo());
		grafico.setIncX(Math.cos(Math.toRadians(grafico.getAngulo()))
				* PASO_VELOCIDAD_MISIL);
		grafico.setIncY(Math.sin(Math.toRadians(grafico.getAngulo()))
				* PASO_VELOCIDAD_MISIL);
		// calcula el tiempo de duración del misil
		tiempoMisil = (int) Math.min(width / Math.abs(grafico.getIncX()),
				height / Math.abs(grafico.getIncY())) - 2;
		misilActivo = true;
	}

	// ===================================================
	// ===================================================
}
