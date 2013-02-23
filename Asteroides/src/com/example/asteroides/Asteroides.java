package com.example.asteroides;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.asteroides.services.AsteroidesService;

public class Asteroides extends Activity implements OnClickListener {

	// ===================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// establezco los listener de los botones
		findViewById(R.id.buttonSalir).setOnClickListener(this);
		findViewById(R.id.buttonHolamundo).setOnClickListener(this);
		findViewById(R.id.buttonAcercaDe).setOnClickListener(this);
		findViewById(R.id.buttonConfigurar).setOnClickListener(this);
	}

	// ===================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true; // consumimos el item, no se propaga
	}

	// ===================================================
	/**
	 * Evento on click que ejecutar� la tarea correspondiente en funci�n del
	 * bot�n pulsado
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSalir:
			// bot�n salir
			AsteroidesService.finalizar(v, this);
			break;
		case R.id.buttonHolamundo:
			// bot�n holamundo
			AsteroidesService.lanzarHolamundo(v, this);
			break;
		case R.id.buttonAcercaDe:
			// bot�n acerca de
			AsteroidesService.lanzarAcercaDe(v, this);
			break;
		case R.id.buttonConfigurar:
			//bot�n de preferencias
			AsteroidesService.LanzarPreferencias(v,this);
			break;
		default:
			// click no esperado
			Log.w(this.getClass().getName(),
					"Lanzado evento onClick no esperado en " + v.getId());
			break;
		}

	}

	// ===================================================
	/**
	 * controla la selecci�n de opciones del men�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_acerca_de:
			// opci�n de men� acerca de
			AsteroidesService.lanzarAcercaDe(null, this);
			break;
		case R.id.menu_settings:
			AsteroidesService.LanzarPreferencias(null,this);
			break;
		default:
			// opci�n de men� no esperada
			Log.w(this.getClass().getName(),
					"Lanzado evento de men� no esperado en " + item.getTitle());
			break;
		}

		return true; // consumimos el item, no se propaga

	}
	// ===================================================
	// ===================================================
}// de la clase
