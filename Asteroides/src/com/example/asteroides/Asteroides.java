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
	 * Evento on click que ejecutará la tarea correspondiente en función del
	 * botón pulsado
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSalir:
			// botón salir
			AsteroidesService.finalizar(v, this);
			break;
		case R.id.buttonHolamundo:
			// botón holamundo
			AsteroidesService.lanzarHolamundo(v, this);
			break;
		case R.id.buttonAcercaDe:
			// botón acerca de
			AsteroidesService.lanzarAcercaDe(v, this);
			break;
		case R.id.buttonConfigurar:
			//botón de preferencias
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
	 * controla la selección de opciones del menú
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_acerca_de:
			// opción de menú acerca de
			AsteroidesService.lanzarAcercaDe(null, this);
			break;
		case R.id.menu_settings:
			AsteroidesService.LanzarPreferencias(null,this);
			break;
		default:
			// opción de menú no esperada
			Log.w(this.getClass().getName(),
					"Lanzado evento de menú no esperado en " + item.getTitle());
			break;
		}

		return true; // consumimos el item, no se propaga

	}
	// ===================================================
	// ===================================================
}// de la clase
