package org.hackatron.ocrsudoku;

import javax.microedition.khronos.opengles.GL;

import org.hackatron.ocrsudoku.R;
import org.hackatron.ocrsudoku.R.layout;
import org.hackatron.ocrsudoku.graphics.MatrixTrackingGL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.opengl.GLSurfaceView;

public class SudokuActivity extends Activity {
	final static String TAG = "Sudoku";
	
	private GLSurfaceView _glView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG,"SudokuActivity Started...");
        
        Intent svc = new Intent(this, SolverService.class);
        startService( svc );
        
        //setContentView(R.layout.main);
        
        _glView = new GLSurfaceView(this);
        _glView.setGLWrapper(new GLSurfaceView.GLWrapper() {
            public GL wrap(GL gl) {
                return new MatrixTrackingGL(gl);
            }});
        _glView.setRenderer(new HUDView(this));
        setContentView(_glView);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        _glView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _glView.onResume();
    }

}