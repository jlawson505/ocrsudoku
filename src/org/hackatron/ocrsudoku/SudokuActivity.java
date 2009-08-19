package org.hackatron.ocrsudoku;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.opengl.GLSurfaceView;

public class SudokuActivity extends Activity {
	final static String TAG = "Sudoku";
	private ISolverService mSolverService = null;
	private HUDView _hudView;

    private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.d(TAG,"Service Connected");
			mSolverService = ISolverService.Stub.asInterface(service);
			
			try {
				mSolverService.startSolving();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.d(TAG,"Service Disconnected");
			mSolverService = null;
		}
    };
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG,"SudokuActivity Started...");
        
        Intent svc = new Intent();
        svc.setClassName("org.hackatron.ocrsudoku", "org.hackatron.ocrsudoku.SolverService");
        Boolean result = bindService(svc, mConnection, BIND_AUTO_CREATE);
        Log.d(TAG,"Bind = "+result);
        
        //setContentView(R.layout.main);
        
        _hudView = new HUDView(this);
        setContentView(_hudView);
        
    }
	    
}