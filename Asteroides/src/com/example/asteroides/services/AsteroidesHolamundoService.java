package com.example.asteroides.services;

import android.view.View;
import android.widget.Toast;

import com.example.asteroides.AsteroidesHolamundo;
import com.example.asteroides.R;

public class AsteroidesHolamundoService {

	// ===================================================
	/**
	 * Muestra un Toast con un Holamundo en pantalla
	 * 
	 * @param view
	 */
	public static void holamundoToast(View view, AsteroidesHolamundo activity) {
		Toast.makeText(activity, R.string.ToastHolaMundo, Toast.LENGTH_SHORT)
				.show();
	}

	// ===================================================
	// ===================================================

}// de la clase
