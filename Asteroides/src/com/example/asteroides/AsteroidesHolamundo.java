package com.example.asteroides;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.asteroides.services.AsteroidesHolamundoService;

public class AsteroidesHolamundo extends Activity implements OnClickListener {

	// ===================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.holamundo);
		// establezco los listener de los botones
		findViewById(R.id.buttonPantallaHolamundo).setOnClickListener(this);
	}

	// ===================================================
	/**
	 * Evento on click que llamará a las tareas correspondientes de la clase de
	 * servicio Holamundo
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonPantallaHolamundo:
			// pulsado botón holamundo
			AsteroidesHolamundoService.holamundoToast(v, this);
			break;
		default:
			// click no esperado
			Log.w(this.getClass().getName(),
					"Lanzado evento onClick no esperado en " + v.getId());
			break;
		}

	}

	// ===================================================
	// ===================================================

}// de la clase
