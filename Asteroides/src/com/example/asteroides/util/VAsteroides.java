package com.example.asteroides.util;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class VAsteroides {

	private Vector<Grafico> asteroides; // Vector con los gráficos de Asteroides

	// ===================================================

	public VAsteroides(View vista, Drawable drawableAsteroide, int numAsteroides) {
		// crea y rellena el vector de asteroides
		// con una velocidad y ángulo aleatorios
		asteroides = new Vector<Grafico>();

		for (int i = 0; i < numAsteroides; i++) {
			Grafico asteroide = new Grafico(vista, drawableAsteroide);
			asteroide.setIncY(Math.random() * 3 - 1);
			asteroide.setIncX(Math.random() * 3 - 1);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			asteroides.add(asteroide);
		}// del for
	}

	// ===================================================
	public void posicionaAsteroides(double ancho, double alto, Nave nave) {
		// Una vez que conocemos nuestro ancho y alto
		// posiciona los asteroides aleatoriamente
		// asegurándonos de que no colisionan con la nave
		for (Grafico asteroide : asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (nave.distancia(asteroide) < (ancho + alto) / 5);
		}// del for
	}

	// ===================================================
	public void dibujar(Canvas canvas) {
		for (Grafico asteroide : asteroides) {
			asteroide.dibujaGrafico(canvas);
		}
	}

	// ===================================================
	public void incrementaPos(double retardo) {
		for (Grafico asteroide : asteroides) {
			asteroide.incrementaPos(retardo);
		}
	}

	// ===================================================
	public boolean colision(Grafico g) {
		for (int i = 0; i < asteroides.size(); i++)
			if (g.verificaColision(asteroides.elementAt(i))) {
				// elmina el asteroide indicado
				asteroides.remove(i);
				return true;
			}
		return false;
	}
	// ===================================================
	// ===================================================
}
