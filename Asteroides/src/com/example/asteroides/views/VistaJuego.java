package com.example.asteroides.views;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.asteroides.R;
import com.example.asteroides.util.Misil;
import com.example.asteroides.util.Nave;
import com.example.asteroides.util.VAsteroides;

public class VistaJuego extends View implements SensorEventListener{

	// nave
	private Nave nave;

	// variables para asteroides
	private VAsteroides vAsteroides; // los Asteroides
	private int numAsteroides = 5; // N�mero inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en que se divide

	// variables para misiles
	private Misil ms;

	// variables de toque en pantalla
	// ubicaci�n del �ltimo evento de toque de pantalla
	private float mX = 0, mY = 0;
	// desplazamiento m�nimo para el evento de toque
	private static final int DESPLAZAMIENTO_MINIMO = 1;
	// factores de aceleraci�n y giro al tocar la pantalla
	private static final int FACTOR_ACELERACION = 6;
	private static final double FACTOR_GIRO = 1.5;
	private boolean disparo = false;

	// valores utilizados en el sensor de posici�n
	private boolean hayValorInicial = false;
	private float valorInicialX;
	private float valorInicialY;
	private static final int FACTOR_ACELERACION_SENSOR = 2;
	private static final double FACTOR_GIRO_SENSOR = 0.7;

	// hilo que se ocupar� de actualizar las posiciones de los gr�ficos de la
	// vista Thread encargado de procesar el juego
	private ThreadJuego thread = new ThreadJuego();
	// Cada cuanto queremos procesar cambios (ms)
	private static int PERIODO_PROCESO = 50;
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
				actualizaFisica();
			}
		}
	}

	// ===================================================
	// ===================================================
	public VistaJuego(Context context, AttributeSet attrs) {

		super(context, attrs);

		// establezco el sensor de orientaci�n que se utilizar�
		SensorManager mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> listSensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		// .getSensorList(Sensor.TYPE_ORIENTATION);
		if (!listSensors.isEmpty()) {
			Sensor orientationSensor = listSensors.get(0);
			mSensorManager.registerListener(this, orientationSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}

		// crea los asteroides
		vAsteroides = new VAsteroides(this, context.getResources().getDrawable(
				R.drawable.asteroide1), numAsteroides);

		// crea la nave
		nave = new Nave(this, context.getResources().getDrawable(
				R.drawable.nave));

		// crea el misil
		ms = new Misil(this,  context.getResources().getDrawable(
				R.drawable.misil1));

	}
	
	// ===================================================
	// no utilizo este m�todo porque el sensor tendr� siempre la misma precisi�n
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	// ===================================================
	// m�todo que gestiona las lecturas del sensor para el giro
	@Override
	public void onSensorChanged(SensorEvent event) {
		float valorX = event.values[0];
		float valorY = event.values[1];
		if (!hayValorInicial) {
			valorInicialX = valorX;
			valorInicialY = valorY;
			hayValorInicial = true;
		}

		float diferenciaX = valorInicialX - valorX;
		float diferenciaY = valorY - valorInicialY;

		// desprecio movimientos demasiado peque�os en aceleraci�n (X)
		if ((-1 < diferenciaX) && (diferenciaX < 1)) {
			diferenciaX = 0;
		}

		nave.setGiroNave((diferenciaY) / FACTOR_GIRO_SENSOR);
		nave.setAceleracionNave(((diferenciaX) / FACTOR_ACELERACION_SENSOR));

		// descarto lecturas erroneas del sensor
		if ((event.values[0] == event.values[1])
				&& (event.values[0] == event.values[2])) {
			nave.setGiroNave(0);
			nave.setAceleracionNave(0);
		}

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
		vAsteroides.posicionaAsteroides(ancho, alto, nave);

		// y la nave centrada
		nave.centrar(ancho, alto);

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
		vAsteroides.dibujar(canvas);

		// dibuja la nave
		nave.dibujaGrafico(canvas);

		// dibuja un misil si est� activo
		ms.dibujaGrafico(canvas);

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
		nave.actualizaDireccion(retardo);

		// Actualizamos la velocidad de la nave si el m�dulo de la velocidad
		// no excede el m�ximo permitido (seg�n la entrada del jugador)
		nave.actualizaVelocidad(retardo);

		// Actualizamos posiciones X e Y de la nave
		nave.incrementaPos(retardo);

		// Actualizamos posiciones X e Y de los asteroides
		vAsteroides.incrementaPos(retardo);

		// Actualizamos posici�n de misil
		ms.incrementaPos(retardo);
		// y comprobamos su colisi�n con un asteroide
		ms.colision(vAsteroides);

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
