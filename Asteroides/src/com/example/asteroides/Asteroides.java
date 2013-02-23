package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Asteroides extends Activity implements OnClickListener {

	private Button bSalir;
	private Button bHolamundo;
	private Button bAcercaDe;

	// ===================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// establezco los listener de los botones
		bSalir = (Button) findViewById(R.id.buttonSalir);
		bHolamundo = (Button) findViewById(R.id.buttonHolamundo);
		bAcercaDe = (Button) findViewById(R.id.buttonAcercaDe);
		bSalir.setOnClickListener(this);
		bHolamundo.setOnClickListener(this);
		bAcercaDe.setOnClickListener(this);
	}

	// ===================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// ===================================================
	/**
	 * Evento on click que ejecutará la tarea correspondiente
	 * en función del botón pulsado
	 */
	@Override
	public void onClick(View v) {
		if (v == null || bHolamundo == null || bSalir == null
				|| bAcercaDe == null) {
			Log.e(this.getClass().getName(),
					"Uno de los parámetros del evento OnClick es null");
		} else if (v.getId() == bSalir.getId()) {
			this.finalizar();
		} else if (v.getId() == bHolamundo.getId()) {
			this.lanzarHolamundo(v);
		} else if (v.getId() == bAcercaDe.getId()) {
			this.lanzarAcercaDe(v);
		} else {
			Log.w(this.getClass().getName(),
					"Lanzado evento onClick no esperado en " + v.getId());
		}

	}

	// ===================================================
	/**
	 * Lanza la actividad Holamundo
	 * 
	 * @param view
	 */
	private void lanzarHolamundo(View view) {
		Intent i = new Intent(this, AsteroidesHolamundo.class);
		startActivity(i);
	}

	// ===================================================
	/**
	 * Lanza la actividad AcercaDe
	 * 
	 * @param view
	 */
	private void lanzarAcercaDe(View view) {
		startActivity(new Intent(this, AcercaDe.class));
	}

	// ===================================================
	/**
	 * Finaliza la aplicación
	 */
	private void finalizar() {
		this.finish();
	}
	// ===================================================
	// ===================================================
}// de la clase
