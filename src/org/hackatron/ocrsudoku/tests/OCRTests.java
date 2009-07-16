package org.hackatron.ocrsudoku.tests;

import org.hackatron.ocrsudoku.OCR;
import org.hackatron.ocrsudoku.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import junit.framework.TestCase;

public class OCRTests extends TestCase {

	public void testDoLearn() {

	//	Bitmap test = BitmapFactory.decodeResource(Service. , R.drawable.character3);
				
		
//		  int width = bitmapOrg.width();
//	        int height = bitmapOrg.height();
//	        int newWidth = 200;
//	        int newHeight = 200;
//	       
//	        // calculate the scale - in this case = 0.4f
//	        float scaleWidth = ((float) newWidth) / width;
//	        float scaleHeight = ((float) newHeight) / height;
//	       
//	        // createa matrix for the manipulation
//	        Matrix matrix = new Matrix();
//	        // resize the bit map
//	        matrix.postScale(scaleWidth, scaleHeight);
//	        // rotate the Bitmap
//	        matrix.postRotate(45);
//
//	        // recreate the new Bitmap
//	        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
//	                          width, height, matrix, true); 
		
		
		OCR ocr = new OCR(50,50);	
		
		int[][] input = new int[50][50];
		ocr.doLearn(input, '1');
		
		assert(true);
	}
}
