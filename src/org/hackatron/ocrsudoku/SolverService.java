package org.hackatron.ocrsudoku;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SolverService extends Service {
	final static String TAG = "Sudoku";

	@Override
	public void onCreate() {
		Log.d(TAG,"SolverService Started...");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
        
		return null;
	}
}