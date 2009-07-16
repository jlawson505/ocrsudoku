package org.hackatron.ocrsudoku.tests;

import org.hackatron.ocrsudoku.OCR;
import org.hackatron.ocrsudoku.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import junit.framework.TestCase;

public class OCRTests extends TestCase {

	public void testDoLearn() {
	
		OCR ocr = new OCR(50,50);	
		
		int[][] input = new int[50][50];
		ocr.doLearn(input, '1');
		
		assert(true);
	}
}
