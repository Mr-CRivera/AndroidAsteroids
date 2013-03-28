package com.example.asteroides;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.asteroides.views.VistaJuego;

public class Juego extends Activity implements SensorEventListener {

	// valores utilizados en el sensor de posición
	private boolean hayValorInicial = false;
	private float valorInicialX;
	private float valorInicialY;
	private static final int FACTOR_ACELERACION_SENSOR = 2;
	private static final double FACTOR_GIRO_SENSOR = 0.7;

	SensorManager mSensorManager;

	// ===================================================
	// ===================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);

		// establezco el sensor de orientación que se utilizará
		if (mSensorManager == null) {
			mSensorManager = (SensorManager) findViewById(R.id.vistaJuego)
					.getContext().getSystemService(Context.SENSOR_SERVICE);

		}

		List<Sensor> listSensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (!listSensors.isEmpty()) {
			Sensor orientationSensor = listSensors.get(0);
			mSensorManager.registerListener(this, orientationSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
		// Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
	}

	// ===================================================
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		// Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	}

	// ===================================================
	@Override
	protected void onStop() {
		super.onStop();
		mSensorManager.unregisterListener(this);
		// Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
	}

	// ===================================================
	@Override
	protected void onResume() {
		super.onResume();

		// reestablezco los sensores
		List<Sensor> listSensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (!listSensors.isEmpty()) {
			Sensor orientationSensor = listSensors.get(0);
			mSensorManager.registerListener(this, orientationSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
		// Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
	}

	// ===================================================
	@Override
	protected void onSaveInstanceState(Bundle guardarEstado) {
		super.onSaveInstanceState(guardarEstado);
		// guardarEstado.putString("variable", var);
		// guardarEstado.putInt("posicion", pos);
		// Toast.makeText(this, "onSaveInstanceState",
		// Toast.LENGTH_SHORT).show();
	}

	// ===================================================
	@Override
	protected void onRestoreInstanceState(Bundle recEstado) {
		super.onRestoreInstanceState(recEstado);
		// var = recEstado.getString("variable");
		// pos = recEstado.getInt("posicion");
		// Toast.makeText(this, "onRestoreInstanceState",
		// Toast.LENGTH_SHORT).show();
	}

	// ===================================================
	// @Override
	// protected void onDestroy() {
	// mSensorManager.unregisterListener(this);
	// super.onDestroy();
	// }

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
		if ((-1.5 < diferenciaX) && (diferenciaX < 1.5)) {
			diferenciaX = 0;
		}
		
		// desprecio movimientos demasiado pequeños en giro (Y)
		if ((-1.5 < diferenciaY) && (diferenciaY < 1.5)) {
			diferenciaX = 0;
		}


		VistaJuego v = (VistaJuego) findViewById(R.id.vistaJuego);

		v.nave.setGiroNave((diferenciaY) / FACTOR_GIRO_SENSOR);
		v.nave.setAceleracionNave(((diferenciaX) / FACTOR_ACELERACION_SENSOR));

		// descarto lecturas erroneas del sensor
		if ((event.values[0] == event.values[1])
				&& (event.values[0] == event.values[2])) {
			v.nave.setGiroNave(0);
			v.nave.setAceleracionNave(0);
		}

	}

	// ===================================================
	// ===================================================
}
