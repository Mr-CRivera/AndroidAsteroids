package com.example.asteroides.views;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.asteroides.R;
import com.example.asteroides.util.Grafico;

public class VistaJuego extends View {

	// variables para la nave
	private Grafico nave;// Gr�fico de la nave
	private long giroNave; // Incremento de direcci�n
	private float aceleracionNave; // aumento de velocidad
	private static final int PASO_GIRO_NAVE = 20; // incremento est�ndar de giro
	// incremento est�ndar de aceleraci�n
	private static final float PASO_ACELERACION_NAVE = 0.2f;
	// ubicaci�n del �ltimo evento de toque de pantalla
	private float mX = 0, mY = 0;
	// desplazamiento m�nimo para el evento de toque
	private static final int DESPLAZAMIENTO_MINIMO = 1;
	// factores de aceleraci�n y giro al tocar la pantalla
	private static final int FACTOR_ACELERACION = 6;
	private static final double FACTOR_GIRO = 1.5;

	// variables para asteroides
	private Vector<Grafico> asteroides; // Vector con los gr�ficos de Asteroides
	private int numAsteroides = 5; // N�mero inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en que se divide

	// variables para misiles
	private boolean disparo = false;

	// drawables para la nave y cada uno de los misiles y asteroides
	private Drawable drawableNave, drawableAsteroide, drawableMisil;

	// hilo que se ocupar� de actualizar las posiciones de los gr�ficos de la
	// vista Thread encargado de procesar el juego
	private ThreadJuego thread = new ThreadJuego();
	// Cada cuanto queremos procesar cambios (ms)
	private static int PERIODO_PROCESO = 15;
	// Timestamp indicando cuando se realiz� el �ltimo proceso
	private long ultimoProceso = 0;

	// ===================================================
	// ===================================================
	// Hilo que se crear� con la clase y se ejecutar� constantemente
	// actualizando la posici�n de los gr�ficos
	class ThreadJuego extends Thread {
		@Override
		public void run() {
			while (true) {
				// try{
				// sleep(PERIODO_PROCESO/2);
				// }
				// catch (InterruptedException e){
				// Log.e(getName(), "Se ha producido una excepci�n", e);
				// }
				actualizaFisica();
			}
		}
	}

	// ===================================================
	// ===================================================
	public VistaJuego(Context context, AttributeSet attrs) {

		super(context, attrs);

		// crea y rellena el vector de asteroides
		// con una velocidad y �ngulo aleatorios
		drawableAsteroide = context.getResources().getDrawable(
				R.drawable.asteroide1);
		asteroides = new Vector<Grafico>();

		for (int i = 0; i < numAsteroides; i++) {
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			asteroide.setIncY(Math.random() * 3 - 1);
			asteroide.setIncX(Math.random() * 3 - 1);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			asteroides.add(asteroide);
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
		for (Grafico asteroide : asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
		}// del for

		// y la nave centrada
		this.nave.setPosX(ancho / 2);
		this.nave.setPosY(alto / 2);

		// y lanza el hilo que actualiza los gr�ficos
		ultimoProceso = System.currentTimeMillis();
		thread.start();
	}

	// ===================================================
	// Dibuja la nave y los asteroides
	@Override
	protected synchronized void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// dibuja cada asteroide del vector
		for (Grafico asteroide : asteroides) {
			asteroide.dibujaGrafico(canvas);
		}

		// dibuja la nave
		this.nave.dibujaGrafico(canvas);

	}

	// ===================================================
	// Actualiza la posici�n y aceleraci�n de cada gr�fico
	protected synchronized void actualizaFisica() {

		long ahora = System.currentTimeMillis();

		// No hagas nada si el per�odo de proceso no se ha cumplido.
		if (ultimoProceso + PERIODO_PROCESO > ahora) {
			return;
		}

		// si el periodo ha sido demasiado grande, se calcula el retardo
		// que indicar� si hay que avanzar m�s de un paso esta vez
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;

		ultimoProceso = ahora; // Para la pr�xima vez

		// Actualizamos la direcci�n de la nave a partir de
		// giroNave (seg�n la entrada del jugador)
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		// descarta el giro hasta que se produzca un nuevo toque
		giroNave = 0;

		// Actualizamos la velocidad de la nave si el m�dulo de la velocidad
		// no excede el m�ximo permitido (seg�n la entrada del jugador)
		double nIncX = nave.getIncX() + aceleracionNave
				* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave
				* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
		//descarta la aceleraci�n hasta que se produzca un nuevo toque
		aceleracionNave = 0;

		if (Math.hypot(nIncX, nIncY) <= Grafico.MAX_VELOCIDAD) {
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}

		// Actualizamos posiciones X e Y de la nave y los asteroides
		nave.incrementaPos(retardo);
		for (Grafico asteroide : asteroides) {
			asteroide.incrementaPos(retardo);
		}

	}

	// ===================================================
	// Manejador del evento onTouch para girar la nave
	// y acelerarla
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// llama al manejador del evento de la clase padre
		super.onTouchEvent(event);

		// obtiene la posici�n en que se toc� la pantalla
		float x = event.getX();
		float y = event.getY();

		// dependiendo del tipo de acci�n realizada realizo los c�lculos
		// pertinentes
		switch (event.getAction()) {
		// al tocar la pantalla
		case MotionEvent.ACTION_DOWN:
			// en principio es un disparo
			disparo = true;
			break;
		// al deslizar el dedo por la pantalla
		case MotionEvent.ACTION_MOVE:
			// calcula los desplazamientos
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			// si hay un desplazamiento mayor de 6, en lugar de un disparo es un
			// giro de nave si se trata de un desplazamiento en el eje x
			// o una aceleraci�n si se trata de un desplazamiento en el eje y
			if (dy < DESPLAZAMIENTO_MINIMO && dx > DESPLAZAMIENTO_MINIMO) {
				giroNave = Math.round((x - mX) / FACTOR_GIRO);
				disparo = false;
			} else if (dx < DESPLAZAMIENTO_MINIMO && dy > DESPLAZAMIENTO_MINIMO) {
				aceleracionNave = Math.round((mY - y) / FACTOR_ACELERACION);
				disparo = false;
			}
			break;
		// al levantar el dedo de la pantalla
		case MotionEvent.ACTION_UP:
			// dejamos de girar y acelerar la nave al levantar el dedo
			giroNave = 0;
			aceleracionNave = 0;

			// si se trataba de un disparo, dibujamos el misil
			if (disparo) {
				// ActivaMisil();
			}
			break;
		}

		// actualiza los valores de la posici�n del �ltimo evento para la
		// pr�xima vez
		mX = x;
		mY = y;

		// devuelve true para que el evento sea propagado a otros navegadores si
		// es necesario
		return true;
	}

	// ===================================================
	// ===================================================
}