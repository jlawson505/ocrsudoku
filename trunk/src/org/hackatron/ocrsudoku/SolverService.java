package org.hackatron.ocrsudoku;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 *  CameraView             +---------> ImageDecode
 *       |                 |
 * SudokuActivity -> SolverService --> OCR -> Neuron  
 *       |                 |
 *   HUDView               +---------> Solver
 * 
 * 
 * 
 * SolverService
 *  - Tråd för hantering av kameran
 *  - CameraView-objekt måste skickas som parameter i start-intent:et
 *  - Skickar Broadcast Intent när en giltig bild har detekterats med ImageDecode
 *  - Skickar Broadcast Intents när pusslet processas
 *  - Skickar Broadcast Intent när algoritmen
 *  - Lyssnar på knapptryckningar
 *
 * ImageDecode 
 *  
 *  
 *  
 * @author jonas
 *
 */

public class SolverService extends Service {
	final static String TAG = "Sudoku";

	private OCR _ocr;

	@Override
	public void onCreate() {
		Log.d(TAG, "SolverService Started...");

		initOCR();
	}

	void initOCR() {
		_ocr = new OCR(20, 25);

		Bitmap character3Bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.character3);		
		_ocr.doLearn(character3Bitmap, '3');

		Bitmap character9Bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.character9);		
		_ocr.doLearn(character9Bitmap, '9');
		
		//char character3BitmapBestMatch = _ocr.getBestMatch(character3Bitmap);
		//char character9BitmapBestMatch = _ocr.getBestMatch(character9Bitmap);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub

		Log.d(TAG,"Service onBind!");
		
		return mBinder;
	}
    /**
     * The Solver interface is defined through IDL
     */
    private final ISolverService.Stub mBinder = new ISolverService.Stub() {
        public void startSolving() {
        	Log.d(TAG,"Service.startSolving was called");
        }
    };

}