package org.hackatron.ocrsudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SudokuActivity extends Activity {
	final static String TAG = "Sudoku";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG,"SudokuActivity Started...");
        
        Intent svc = new Intent(this, SolverService.class);
        startService( svc );
        
        setContentView(R.layout.main);
    }
}