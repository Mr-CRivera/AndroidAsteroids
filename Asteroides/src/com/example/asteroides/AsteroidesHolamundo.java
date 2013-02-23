package com.example.asteroides;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AsteroidesHolamundo extends Activity implements OnClickListener {

	private Button bHolamundo;

	// ===================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.holamundo);
		bHolamundo = (Button) findViewById(R.id.buttonPantallaHolamundo);
		bHolamundo.setOnClickListener(this);
	}

	// ===================================================
	/**
	 * Evento on click que ejecutará la tarea del botón Holamundo
	 */
	@Override
	public void onClick(View v) {
		if (v == null || bHolamundo == null) {
			Log.e(this.getClass().getName(),
					"Uno de los parámetros del evento OnClick es null");
		} else if (v.getId() == bHolamundo.getId()) {
			this.holamundoToast(v);
		} else {
			Log.w(this.getClass().getName(),
					"Lanzado evento onClick no esperado en " + v.getId());
		}

	}

	// ===================================================
	/**
	 * Muestra un Toast con un Holamundo en pantalla
	 * 
	 * @param view
	 */
	private void holamundoToast(View view) {
		Toast.makeText(this, R.string.ToastHolaMundo, Toast.LENGTH_SHORT)
				.show();
	}

	// ===================================================
	// ===================================================

}// de la clase
