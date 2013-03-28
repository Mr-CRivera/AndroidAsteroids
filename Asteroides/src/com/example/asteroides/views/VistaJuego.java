package com.example.asteroides.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.asteroides.R;
import com.example.asteroides.util.Misil;
import com.example.asteroides.util.Nave;
import com.example.asteroides.util.VAsteroides;

//public class VistaJuego extends View implements SensorEventListener{
public class VistaJuego extends View {

	// nave
	public Nave nave;

	// variables para asteroides
	private VAsteroides vAsteroides; // los Asteroides
	private int numAsteroides = 5; // Número inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en que se divide

	// variables para misiles
	private Misil ms;

	// variables de toque en pantalla
	// ubicación del último evento de toque de pantalla
	private float mX = 0, mY = 0;
	// desplazamiento mínimo para el evento de toque
	private static final int DESPLAZAMIENTO_MINIMO = 1;
	// factores de aceleración y giro al tocar la pantalla
	private static final int FACTOR_ACELERACION = 6;
	private static final double FACTOR_GIRO = 1.5;
	private boolean disparo = false;

	// hilo que se ocupará de actualizar las posiciones de los gráficos de la
	// vista Thread encargado de procesar el juego
	private ThreadJuego thread = new ThreadJuego();
	// Cada cuanto queremos procesar cambios (ms)
	private static int PERIODO_PROCESO = 50;
	// Timestamp indicando cuando se realizó el último proceso
	private long ultimoProceso = 0;

	// ===================================================
	// ===================================================
	// Hilo que se creará con la clase y se ejecutará constantemente
	// actualizando la posición de los gráficos
	class ThreadJuego extends Thread {
		@Override
		public void run() {
			while (true) {
				actualizaFisica();
			}
		}
	}

	// ===================================================
	// ===================================================
	public VistaJuego(Context context, AttributeSet attrs) {

		super(context, attrs);

		// crea los asteroides
		vAsteroides = new VAsteroides(this, context.getResources().getDrawable(
				R.drawable.asteroide1), numAsteroides);

		// crea la nave
		nave = new Nave(this, context.getResources().getDrawable(
				R.drawable.nave));

		// crea el misil
		ms = new Misil(this, context.getResources().getDrawable(
				R.drawable.misil1));

	}

	// ===================================================

	// redibuja al tamaño adecuado cuando se cambia (o se conoce por primera
	// vez) el tamaño
	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {

		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

		// Una vez que conocemos nuestro ancho y alto
		// posiciona los asteroides aleatoriamente
		// asegurándonos de que no colisionan con la nave
		vAsteroides.posicionaAsteroides(ancho, alto, nave);

		// y la nave centrada
		nave.centrar(ancho, alto);

		// y lanza el hilo que actualiza los gráficos
		ultimoProceso = System.currentTimeMillis();
		thread.start();
	}

	// ===================================================
	// Dibuja la nave y los asteroides
	@Override
	protected synchronized void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// dibuja cada asteroide del vector
		vAsteroides.dibujar(canvas);

		// dibuja la nave
		nave.dibujaGrafico(canvas);

		// dibuja un misil si está activo
		ms.dibujaGrafico(canvas);

	}

	// ===================================================
	// Actualiza la posición y aceleración de cada gráfico
	protected synchronized void actualizaFisica() {

		long ahora = System.currentTimeMillis();

		// No hagas nada si el período de proceso no se ha cumplido.
		if (ultimoProceso + PERIODO_PROCESO > ahora) {
			return;
		}

		// si el periodo ha sido demasiado grande, se calcula el retardo
		// que indicará si hay que avanzar más de un paso esta vez
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;

		ultimoProceso = ahora; // Para la próxima vez

		// Actualizamos la dirección de la nave a partir de
		// giroNave (según la entrada del jugador)
		nave.actualizaDireccion(retardo);

		// Actualizamos la velocidad de la nave si el módulo de la velocidad
		// no excede el máximo permitido (según la entrada del jugador)
		nave.actualizaVelocidad(retardo);

		// Actualizamos posiciones X e Y de la nave
		nave.incrementaPos(retardo);

		// Actualizamos posiciones X e Y de los asteroides
		vAsteroides.incrementaPos(retardo);

		// Actualizamos posición de misil
		ms.incrementaPos(retardo);
		// y comprobamos su colisión con un asteroide
		ms.colision(vAsteroides);

	}

	// ===================================================
	// Manejador del evento onTouch para girar la nave
	// y acelerarla
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// llama al manejador del evento de la clase padre
		super.onTouchEvent(event);

		// obtiene la posición en que se tocó la pantalla
		float x = event.getX();
		float y = event.getY();

		// dependiendo del tipo de acción realizada realizo los cálculos
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
			// o una aceleración si se trata de un desplazamiento en el eje y
			if (dy < DESPLAZAMIENTO_MINIMO && dx > DESPLAZAMIENTO_MINIMO) {
				nave.setGiroNave(Math.round((x - mX) / FACTOR_GIRO));
				disparo = false;
			} else if (dx < DESPLAZAMIENTO_MINIMO && dy > DESPLAZAMIENTO_MINIMO) {
				nave.setAceleracionNave(Math.round((mY - y)
						/ FACTOR_ACELERACION));
				disparo = false;
			}
			break;
		// al levantar el dedo de la pantalla
		case MotionEvent.ACTION_UP:
			// dejamos de girar y acelerar la nave al levantar el dedo
			nave.setGiroNave(0);
			nave.setAceleracionNave(0);

			// si se trataba de un disparo, dibujamos el misil
			if (disparo) {
				ms.activaMisil(nave, this.getWidth(), this.getHeight());
			}
			break;
		}

		// actualiza los valores de la posición del último evento para la
		// próxima vez
		mX = x;
		mY = y;

		// devuelve true para que el evento sea propagado a otros navegadores si
		// es necesario
		return true;
	}

	// ===================================================
	// ===================================================
}
