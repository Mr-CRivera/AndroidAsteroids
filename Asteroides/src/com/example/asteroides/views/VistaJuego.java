package com.example.asteroides.views;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.asteroides.R;
import com.example.asteroides.util.Grafico;

public class VistaJuego extends View implements SensorEventListener {

	// variables para la nave
	private Grafico nave;// Gráfico de la nave
	private double giroNave; // Incremento de dirección
	private float aceleracionNave; // aumento de velocidad
	private static final int PASO_GIRO_NAVE = 20; // incremento estándar de giro
	// incremento estándar de aceleración
	private static final float PASO_ACELERACION_NAVE = 0.2f;
	// ubicación del último evento de toque de pantalla
	private float mX = 0, mY = 0;
	// desplazamiento mínimo para el evento de toque
	private static final int DESPLAZAMIENTO_MINIMO = 1;
	// factores de aceleración y giro al tocar la pantalla
	private static final int FACTOR_ACELERACION = 6;
	private static final double FACTOR_GIRO = 1.5;

	// variables para asteroides
	private Vector<Grafico> asteroides; // Vector con los gráficos de Asteroides
	private int numAsteroides = 5; // Número inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en que se divide

	// variables para misiles
	private boolean disparo = false;
	private Grafico misil;
	private static int PASO_VELOCIDAD_MISIL = 13;
	private boolean misilActivo = false;
	private int tiempoMisil;

	// drawables para la nave y cada uno de los misiles y asteroides
	private Drawable drawableNave, drawableAsteroide, drawableMisil;

	// hilo que se ocupará de actualizar las posiciones de los gráficos de la
	// vista Thread encargado de procesar el juego
	private ThreadJuego thread = new ThreadJuego();
	// Cada cuanto queremos procesar cambios (ms)
	private static int PERIODO_PROCESO = 50;
	// Timestamp indicando cuando se realizó el último proceso
	private long ultimoProceso = 0;

	// valores utilizados en los sensores
	private boolean hayValorInicial = false;
	private float valorInicialX;
	private float valorInicialY;
	private static final int FACTOR_ACELERACION_SENSOR = 2;
	private static final double FACTOR_GIRO_SENSOR = 0.7;

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

		// establezco el sensor de orientación que se utilizará
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

		// crea y rellena el vector de asteroides
		// con una velocidad y ángulo aleatorios
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

		// crea un gráfico para la nave
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		nave = new Grafico(this, drawableNave);

		// crea un drawable para los misiles
		drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
		misil = new Grafico(this, drawableMisil);

	}

	// ===================================================
	// no utilizo este método porque el sensor tendrá siempre la misma precisión
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	// ===================================================
	// método que gestiona las lecturas del sensor para el giro
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
		
		// desprecio movimientos demasiado pequeños en aceleración (X)
		if ((-1 < diferenciaX) && (diferenciaX < 1)) {
			diferenciaX = 0;
		}

		giroNave = (diferenciaY) / FACTOR_GIRO_SENSOR;
		aceleracionNave = (float) ((diferenciaX) / FACTOR_ACELERACION_SENSOR);

		// descarto lecturas erroneas del sensor
		if ((event.values[0] == event.values[1])
				&& (event.values[0] == event.values[2])) {
			giroNave = 0;
			aceleracionNave = 0;
		}

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
		for (Grafico asteroide : asteroides) {
			asteroide.dibujaGrafico(canvas);
		}

		// dibuja la nave
		this.nave.dibujaGrafico(canvas);

		// dibuja un misil si está activo
		if (misilActivo) {
			this.misil.dibujaGrafico(canvas);
		}

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
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		// descarta el giro hasta que se produzca un nuevo toque
		giroNave = 0;

		// Actualizamos la velocidad de la nave si el módulo de la velocidad
		// no excede el máximo permitido (según la entrada del jugador)
		double nIncX = nave.getIncX() + aceleracionNave
				* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave
				* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
		// descarta la aceleración hasta que se produzca un nuevo toque
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

		// Actualizamos los misiles
		// Actualizamos posición de misil
		if (misilActivo) {
			misil.incrementaPos(retardo);
			tiempoMisil -= retardo;
			if (tiempoMisil < 0) {
				misilActivo = false;
			} else {
				for (int i = 0; i < asteroides.size(); i++)
					if (misil.verificaColision(asteroides.elementAt(i))) {
						destruyeAsteroide(i);
						break;
					}
			}
		}

	}

	// ===================================================
	// elmina el asteroide indicado
	private void destruyeAsteroide(int i) {
		asteroides.remove(i);
		misilActivo = false;
	}

	// ===================================================
	// activa el misil
	private synchronized void ActivaMisil() {
		misil.setPosX(nave.getPosX() + nave.getAncho() / 2 - misil.getAncho()
				/ 2);
		misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAlto() / 2);
		misil.setAngulo(nave.getAngulo());
		misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);
		misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);
		tiempoMisil = (int) Math.min(
				this.getWidth() / Math.abs(misil.getIncX()), this.getHeight()
						/ Math.abs(misil.getIncY())) - 2;
		misilActivo = true;
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
				ActivaMisil();
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
