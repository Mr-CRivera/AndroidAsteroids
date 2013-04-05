package com.example.asteroides.util;

import android.content.Context;
import android.media.MediaPlayer;


public class HiloMusica extends Thread {
	private static MediaPlayer mp = null;
	
	public HiloMusica (Context c, int r){
		mp = MediaPlayer.create(c, r);
		mp.setLooping(true);
	}

	public void mp_start(){
		if (mp!=null) {mp.start();}
	}
	
	public void mp_stop(){
		if (mp!=null) {mp.stop();}
	}
	
	@Override
	public void run(){
		if (mp!=null) {mp.start();}
		while (true);
	}
	
	@Override
	public void interrupt() {
		if (mp!=null) {mp.stop();}
		super.interrupt();
	}
}
