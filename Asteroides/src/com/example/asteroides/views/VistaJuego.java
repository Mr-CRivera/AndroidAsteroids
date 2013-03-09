package com.example.asteroides.views;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.asteroides.R;
import com.example.asteroides.util.Grafico;

public class VistaJuego extends View {

	// variables para la nave
	private Grafico nave;// Gr�fico de la nave
	private int giroNave; // Incremento de direcci�n
	private float aceleracionNave; // aumento de velocidad
	private static final int PASO_GIRO_NAVE = 5; // incremento est�ndar de giro
	private static final float PASO_ACELERACION_NAVE = 0.5f; // incremento
																// est�ndar de
																// aceleraci�n

	// variables para asteroides
	private Vector<Grafico> Asteroides; // Vector con los gr�ficos de Asteroides
	private int numAsteroides = 5; // N�mero inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en que se divide

	// drawables para la nave y cada uno de los misiles y asteroides
	private Drawable drawableNave, drawableAsteroide, drawableMisil;

	// ===================================================
	public VistaJuego(Context context, AttributeSet attrs) {

		super(context, attrs);

		// crea y rellena el vector de asteroides
		// con una velocidad y �ngulo aleatorios
		drawableAsteroide = context.getResources().getDrawable(
				R.drawable.asteroide1);
		Asteroides = new Vector<Grafico>();

		for (int i = 0; i < numAsteroides; i++) {
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroide);
		}// del for

		// crea un gr�fico para la nave
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		nave = new Grafico(this, drawableNave);

	}

	// ===================================================
	// redibuja al tama�o adecuado cuando se cambia (o se conoce por primera
	// vez) el tama�o
	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {

		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

		// Una vez que conocemos nuestro ancho y alto
		// posiciona los asteroides aleatoriamente
		// asegur�ndonos de que no colisionan con la nave
		for (Grafico asteroide : Asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
		}// del for

		// y la nave centrada
		this.nave.setPosX(ancho / 2);
		this.nave.setPosY(alto / 2);
	}

	// ===================================================
	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// dibuja cada asteroide del vector
		for (Grafico asteroide : Asteroides) {
			asteroide.dibujaGrafico(canvas);
		}

		// dibuja la nave
		this.nave.dibujaGrafico(canvas);
	}

	// ===================================================
	// ===================================================
}
