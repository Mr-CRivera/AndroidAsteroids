package com.example.asteroides;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.asteroides.services.AsteroidesService;
import com.example.asteroides.util.AlmacenPuntuaciones;
import com.example.asteroides.util.AlmacenPuntuacionesArray;
import com.example.asteroides.util.HiloMusica;

public class Asteroides extends Activity implements OnClickListener {

	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
	private MediaPlayer mp;
//	private HiloMusica hiloMusica = null;

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
		findViewById(R.id.buttonPuntuaciones).setOnClickListener(this);
		findViewById(R.id.buttonArrancar).setOnClickListener(this);
		// reproduzco la m�sica de fondo
		
//		hiloMusica = new HiloMusica(this, R.raw.galaxias);
//		hiloMusica.start();
		mp = MediaPlayer.create(this, R.raw.galaxias);
		mp.start();
	}

	// ===================================================
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (mp!=null) { mp.pause();}
//	}

	// ===================================================
	@Override
	protected void onResume() {
		super.onResume();
//		hiloMusica.mp_start();
		if (mp==null) { mp = MediaPlayer.create(this, R.raw.galaxias); }
		mp.start();
	}

	// ===================================================
	@Override
	protected void onDestroy() {
		if (mp!=null) { mp.stop();}
//		hiloMusica.mp_stop();
		super.onDestroy();
	}

	// ===================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true; // consumimos el item, no se propaga
	}
	
	// ===================================================
	
	@Override
	protected void onSaveInstanceState(Bundle estadoGuardado) {
		super.onSaveInstanceState(estadoGuardado);
		if (mp != null) {
			int pos = mp.getCurrentPosition();
			estadoGuardado.putInt("posicion", pos);
		}
		// Toast.makeText(this, "onSaveInstanceState",
		// Toast.LENGTH_SHORT).show();
	}

	// ===================================================
	@Override
	protected void onRestoreInstanceState(Bundle estadoGuardado) {
		super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null) {
               int pos = estadoGuardado.getInt("posicion");
               mp.seekTo(pos);
        }
		// Toast.makeText(this, "onRestoreInstanceState",
		// Toast.LENGTH_SHORT).show();
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
			// bot�n de preferencias
			AsteroidesService.LanzarPreferencias(v, this);
			break;
		case R.id.buttonPuntuaciones:
			// bot�n de puntuaciones
			AsteroidesService.LanzarPuntuaciones(v, this);
			break;
		case R.id.buttonArrancar:
			// bot�n de jugar
			AsteroidesService.LanzarJuego(v, this);
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
			AsteroidesService.LanzarPreferencias(null, this);
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
