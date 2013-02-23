package com.example.asteroides.services;

import android.content.Intent;
import android.view.View;

import com.example.asteroides.AcercaDe;
import com.example.asteroides.Asteroides;
import com.example.asteroides.AsteroidesHolamundo;
import com.example.asteroides.Preferencias;

public class AsteroidesService{

	// ===================================================
	/**
	 * Lanza la actividad Holamundo
	 * 
	 * @param view
	 */
	public static void lanzarHolamundo(View view, Asteroides activity) {
		Intent i = new Intent(activity, AsteroidesHolamundo.class);
		activity.startActivity(i);
	}

	// ===================================================
	/**
	 * Lanza la actividad AcercaDe
	 * 
	 * @param view
	 */
	public static void lanzarAcercaDe(View view, Asteroides activity) {
		activity.startActivity(new Intent(activity, AcercaDe.class));
	}
	
	//===================================================
	/**
	 * Lanza la actividad Preferencias
	 * 
	 * @param view
	 */
	public static void LanzarPreferencias(View view, Asteroides activity) {
		activity.startActivity(new Intent(activity, Preferencias.class));
	}
	
	// ===================================================
	/**
	 * Finaliza la aplicación
	 */
	public static void finalizar(View view, Asteroides activity) {
		activity.finish();
	}
	
	// ===================================================
	// ===================================================
}// de la clase
